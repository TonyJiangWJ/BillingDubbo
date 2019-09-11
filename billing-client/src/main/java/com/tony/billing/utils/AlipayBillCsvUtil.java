package com.tony.billing.utils;

import com.tony.billing.entity.CostRecord;
import com.tony.billing.service.api.AlipayBillCsvConvertService;
import com.tony.billing.util.CsvParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author jiangwenjie 2019/9/10
 */
@Slf4j
@Component
public class AlipayBillCsvUtil {

    @Reference
    private AlipayBillCsvConvertService alipayBillCsvConvertService;

    private final String ALIPAY_RECORD_FLAG = "支付宝交易记录明细查询";

    private final String BACKUP_FLAG = "交易号,订单号,创建时间,支付时间,修改时间,地点,订单类型,交易对方,商品名称,金额,支出/收入,订单状态,服务费,退款,备注,交易状态,是否删除,id,是否隐藏";

    private final int ALIPAY_RECORD_LINES = 7;

    private final String ZIP_NAME_SUFFIX = ".zip";
    private final String CSV_NAME_SUFFIX = ".csv";

    /**
     * 分批发送 避免阻塞太久
     */
    private final int MAX_SENDING = 250;


    public boolean convertToPOJO(MultipartFile multipartFile) {
        if (multipartFile != null) {
            try {
                InputStream inputStream = null;
                // 未解压的支付宝账单
                if (Objects.requireNonNull(multipartFile.getOriginalFilename()).endsWith(ZIP_NAME_SUFFIX)) {
                    ZipInputStream zipInputStream = new ZipInputStream(multipartFile.getInputStream());
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    if (!zipEntry.isDirectory() && zipEntry.getName().endsWith(CSV_NAME_SUFFIX)) {
                        inputStream = zipInputStream;
                    }
                    // 已解压成csv文件的支付宝账单
                } else if (multipartFile.getOriginalFilename().endsWith(CSV_NAME_SUFFIX)) {
                    inputStream = multipartFile.getInputStream();
                }
                if (inputStream != null) {
                    CsvParser csvParser = new CsvParser(inputStream);
                    if (!ALIPAY_RECORD_FLAG.equals(csvParser.getRow(0))) {
                        throw new RuntimeException("Illegal file");
                    }
                    if (!CollectionUtils.isEmpty(csvParser.getList())) {
                        if (csvParser.getRowNum() <= ALIPAY_RECORD_LINES) {
                            throw new RuntimeException("Illegal file");
                        }
                        // alipay format
                        List<String> fixedList = csvParser.getListCustom(5, csvParser.getRowNum() - 7);
                        if (CollectionUtils.isNotEmpty(fixedList)) {
                            while (fixedList.size() > MAX_SENDING) {
                                List<String> sendingList = fixedList.subList(0, MAX_SENDING);
                                alipayBillCsvConvertService.convertToPOJO(sendingList);
                                fixedList = fixedList.subList(MAX_SENDING, fixedList.size());
                            }
                            alipayBillCsvConvertService.convertToPOJO(fixedList);
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                log.error("读取支付宝账单文件出错", e);
            }
        }

        return false;
    }


    public boolean getFromBackup(MultipartFile multipartFile) {
        if (multipartFile != null) {
            try {
                InputStream inputStream = multipartFile.getInputStream();
                CsvParser csvParser = new CsvParser(inputStream);
                if (!BACKUP_FLAG.equals(csvParser.getRow(0))) {
                    throw new RuntimeException("Illegal file");
                }
                if (!org.springframework.util.CollectionUtils.isEmpty(csvParser.getList())) {
                    if (csvParser.getRowNum() <= 1) {
                        throw new RuntimeException("Illegal file");
                    }
                    List<String> fixedList = csvParser.getListCustom(1, csvParser.getRowNum());
                    while (fixedList.size() > MAX_SENDING) {
                        List<String> sendingList = fixedList.subList(0, MAX_SENDING);
                        alipayBillCsvConvertService.getFromBackUp(sendingList);
                        fixedList = fixedList.subList(MAX_SENDING, fixedList.size());
                    }
                    alipayBillCsvConvertService.getFromBackUp(fixedList);
                    return true;
                }
            } catch (IOException e) {
                log.error("读取备份文件出错", e);
            }
        }
        return false;
    }

    public List<String> convertPOJO2String(List<CostRecord> costRecords) {
        return alipayBillCsvConvertService.convertPOJO2String(costRecords);
    }
}
