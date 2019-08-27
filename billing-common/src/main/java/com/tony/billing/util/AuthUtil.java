package com.tony.billing.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;


/**
 * @author by TonyJiang on 2017/7/1.
 */
public class AuthUtil {

    private JavaWebToken javaWebToken;

    public AuthUtil(JavaWebToken javaWebToken) {
        this.javaWebToken = javaWebToken;
    }

    private Map<String, Object> getClientLoginInfo(String token) throws Exception {
        Map<String, Object> r;

        if (token != null) {
            r = decodeSession(token);
            return r;
        }
        throw new Exception("token解析错误");
    }

    public String getUserTokenId(String token) throws Exception {
        Map<String, Object> sessionInfoMap = getClientLoginInfo(token);
        return sessionInfoMap == null ? "" : (String) sessionInfoMap.get("tokenId");
    }

    public void setCookieToken(String tokenId, HttpServletResponse response) {
        Map<String, Object> param = new HashMap<>();
        param.put("tokenId", tokenId);
        String token = javaWebToken.createJavaWebToken(param);
        CookieUtil.setCookie(response, "token", token);
    }

    /**
     * session解密
     */
    private Map<String, Object> decodeSession(String token) {
        try {
            return javaWebToken.verifyJavaWebToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    public static class JavaWebToken {
        private Logger log = LoggerFactory.getLogger(JavaWebToken.class);

        private String jwtKey;

        public JavaWebToken(String jwtKey) {
            this.jwtKey = jwtKey;
        }

        private Key getKeyInstance() {
            // We will sign our JavaWebToken with our ApiKey secret
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtKey);
            return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        }

        private String createJavaWebToken(Map<String, Object> claims) {
            return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();
        }

        private Map<String, Object> verifyJavaWebToken(String jwt) {
            try {
                return Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
            } catch (Exception e) {
                log.error("json web token verify failed");
                return null;
            }
        }
    }
}
