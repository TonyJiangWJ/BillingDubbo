package com.tony.billing.util;

import com.tony.billing.constants.timing.TimeConstants;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangwj20966 on 2017/11/27.
 */
public class RSAUtil {

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    /**
     * 密文有效期30秒
     */
    @Value("${rsa.valid.time:30000}")
    private long CIPHER_VALID_TIME;

    private final int TIMESTAMP_LENGTH = 13;

    public RSAUtil(String filePath) throws Exception {
        publicKey = RSAEncrypt.loadPublicKeyByByteArray(RSAEncrypt.loadPublicKeyByFile(filePath));
        privateKey = RSAEncrypt.loadPrivateKeyByByteArray(RSAEncrypt.loadPrivateKeyByFile(filePath));
    }

    public RSAUtil(String publicBase64Str, String privateBase64Str) throws Exception {
        publicKey = RSAEncrypt.loadPublicKeyByByteArray(Base64.decodeBase64(publicBase64Str));
        privateKey = RSAEncrypt.loadPrivateKeyByByteArray(Base64.decodeBase64(privateBase64Str));
    }

    private final Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    public String decrypt(String cipher) {
        try {
            // 当base64编码串中包含空格，转换为加号
            cipher = cipher.replaceAll("[ ]", "+");
            byte[] cipherArray = RSAEncrypt.decrypt(privateKey, Base64.decodeBase64(cipher));
            if (cipherArray != null) {
                String cipherStr = new String(cipherArray, 0, cipherArray.length);
                if (cipherStr.length() > TIMESTAMP_LENGTH) {
                    long timestamp = Long.parseLong(cipherStr.substring(0, TIMESTAMP_LENGTH));
                    if (System.currentTimeMillis() - timestamp < CIPHER_VALID_TIME) {
                        return cipherStr.substring(TIMESTAMP_LENGTH);
                    } else {
                        logger.error("验证信息超时：{}", LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeConstants.CHINA_ZONE)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("解析rsa加密内容失败 ", e);
        }
        return null;
    }

    /**
     * 将明文加密 前置添加TIMESTAMP13时间戳 用于校验密文有效性
     *
     * @param content 明文内容
     * @param key     加密秘钥public or private key
     * @return 加密后的内容 Base64处理
     */
    private String encrypt(String content, RSAKey key) {
        if (content != null) {
            long timestamp = System.currentTimeMillis();
            content = timestamp + content;
            try {
                if (key instanceof RSAPrivateKey) {
                    return Base64.encodeBase64String(RSAEncrypt.encrypt((RSAPrivateKey)key, content.getBytes()));
                } else if (key instanceof RSAPublicKey) {
                    return Base64.encodeBase64String(RSAEncrypt.encrypt((RSAPublicKey)key, content.getBytes()));
                }
            } catch (Exception e) {
                logger.error("rsa加密失败 ", e);
            }
        }
        return null;
    }

    public String encryptWithPrivateKey(String content) {
        return encrypt(content, privateKey);
    }

    public String encryptWithPubKey(String content) {
        return encrypt(content, publicKey);
    }
}
