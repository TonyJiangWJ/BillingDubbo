package com.tony.billing.service.impl;

import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.enums.EnumHidden;
import com.tony.billing.dao.mapper.CostRecordMapper;
import com.tony.billing.entity.CostRecord;
import com.tony.billing.service.api.AlipayBillCsvConvertService;
import com.tony.billing.service.base.AbstractService;
import com.tony.billing.util.CsvParser;
import com.tony.billing.util.MoneyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author by TonyJiang on 2017/6/3.
 */
@Service
public class AlipayBillCsvConvertServiceImpl extends AbstractService<CostRecord, CostRecordMapper> implements AlipayBillCsvConvertService {

    private Logger logger = LoggerFactory.getLogger(AlipayBillCsvConvertService.class);

    private final String ALIPAY_RECORD_FLAG = "支付宝交易记录明细查询";

    private final String BACKUP_FLAG = "交易号,订单号,创建时间,支付时间,修改时间,地点,订单类型,交易对方,商品名称,金额,支出/收入,订单状态,服务费,退款,备注,交易状态,是否删除,id,是否隐藏";

    private final int ALIPAY_RECORD_LINES = 7;

    private final String ZIP_NAME_SUFFIX = ".zip";
    private final String CSV_NAME_SUFFIX = ".csv";

    @Override
    public boolean convertToPOJO(MultipartFile multipartFile, Long userId) {
        if (multipartFile != null) {
            try {
                InputStream inputStream = null;
                // 未解压的支付宝账单
                if (multipartFile.getOriginalFilename().endsWith(ZIP_NAME_SUFFIX)) {
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
                        try {
                            RecordRefUtil<Record> recordRefUtil = new RecordRefUtil<>();
                            List<Record> records = new ArrayList<>();
                            for (String csvLine : fixedList) {
                                records.add(recordRefUtil.convertCsv2POJO(csvLine, Record.class));
                            }
                            if (!CollectionUtils.isEmpty(records)) {
                                List<CostRecord> insertList = new ArrayList<>();
                                for (Record entity : records) {
                                    convertToDBJOAndInsert(entity, userId, insertList);
                                }
                                if (!CollectionUtils.isEmpty(insertList)) {
                                    mapper.batchInsert(insertList);
                                }
                            }
                            return true;
                        } catch (IllegalAccessException | InstantiationException e) {
                            logger.error("转换支付宝账单csv文件出错", e);
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("读取支付宝账单文件出错", e);
            }
        }

        return false;
    }

    @Override
    public List<String> convertPOJO2String(List<CostRecord> recordList) {
        List<String> result = new ArrayList<>();
        RecordRefUtil utl = new RecordRefUtil();
        result.add(BACKUP_FLAG);
        try {
            for (CostRecord costRecord : recordList) {
                result.add(utl.convertPOJO2String(costRecord));
            }
        } catch (IllegalAccessException e) {
            logger.error("转换对象到csv文本失败", e);
        }
        return result;
    }

    @Override
    public boolean getFromBackUp(MultipartFile multipartFile, Long userId) {

        if (multipartFile != null) {
            try {
                InputStream inputStream = multipartFile.getInputStream();
                CsvParser csvParser = new CsvParser(inputStream);
                if (!BACKUP_FLAG.equals(csvParser.getRow(0))) {
                    throw new RuntimeException("Illegal file");
                }
                if (!CollectionUtils.isEmpty(csvParser.getList())) {
                    if (csvParser.getRowNum() <= 1) {
                        throw new RuntimeException("Illegal file");
                    }
                    List<String> fixedList = csvParser.getListCustom(1, csvParser.getRowNum());
                    try {
                        RecordRefUtil recordRefUtil = new RecordRefUtil();
                        List<CostRecord> records = new ArrayList<>();
                        for (String csvLine : fixedList) {
                            records.add((CostRecord) recordRefUtil.convertCsv2POJO(csvLine, CostRecord.class));
                        }
                        if (!CollectionUtils.isEmpty(records)) {
                            List<CostRecord> insertList = new ArrayList<>();
                            for (CostRecord entity : records) {
                                convertToDBJOAndInsertCostRecord(entity, userId, insertList);
                            }
                            if (!CollectionUtils.isEmpty(insertList)) {
                                mapper.batchInsert(insertList);
                            }
                        }
                        return true;
                    } catch (Exception e) {
                        logger.error("从备份文件中恢复出错", e);
                    }
                }
            } catch (IOException e) {
                logger.error("读取备份文件出错", e);
            }
        }

        return false;
    }

    private void convertToDBJOAndInsertCostRecord(CostRecord entity, Long userId, List<CostRecord> insertList) {
        Integer recordCount = mapper.checkTradeExistence(entity.getTradeNo(), userId);
        if (recordCount == 0) {
            entity.setId(null);
            entity.setUserId(userId);

            entity.setCreateTime(new Date());
            entity.setModifyTime(new Date());
            insertList.add(entity);
        } else {
            logger.error("record already exist");
        }
    }


    private void convertToDBJOAndInsert(Record entity, Long userId, List<CostRecord> insertList) {
        Integer recordCount = mapper.checkTradeExistence(entity.getTradeNo(), userId);
        if (recordCount == 0) {
            CostRecord record = new CostRecord();


            record.setIsDeleted(0);
            record.setCostCreateTime(entity.getCreateTime());
            record.setGoodsName(entity.getGoodsName());
            record.setInOutType(entity.getInOutType());
            record.setLocation(entity.getLocation());
            record.setMemo(entity.getMemo());
            record.setCostModifyTime(entity.getModifyTime());
            record.setMoney(MoneyUtil.yuan2fen(entity.getMoney()));
            record.setOrderNo(entity.getOrderNo());
            record.setOrderStatus(entity.getOrderStatus());
            record.setOrderType(entity.getOrderType());
            record.setPaidTime(entity.getPaidTime());
            record.setRefundMoney(MoneyUtil.yuan2fen(entity.getRefundMoney()));
            record.setServiceCost(MoneyUtil.yuan2fen(entity.getServiceCost()));
            record.setTarget(entity.getTarget());
            record.setTradeNo(entity.getTradeNo());
            record.setTradeStatus(entity.getTradeStatus());
            record.setUserId(userId);

            record.setVersion(0);
            record.setIsHidden(EnumHidden.NOT_HIDDEN.val());
            record.setIsDeleted(EnumDeleted.NOT_DELETED.val());
            record.setCreateTime(new Date());
            record.setModifyTime(new Date());
            insertList.add(record);
        } else {
            logger.warn("record already exist");
        }


    }

    private static class RecordRefUtil<T> {
        private T convertCsv2POJO(String csvLine, Class<T> t) throws IllegalAccessException, InstantiationException {
            String[] strings = csvLine.split(",");
            Object record = t.newInstance();
            Class clazz = record.getClass();
            Field[] fields = clazz.getDeclaredFields();
            if (strings.length != fields.length) {
                System.out.println("Error Line");
                return null;
            } else {
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fields[i].set(record, StringUtils.trim(strings[i]));
                }

                return (T) record;
            }
        }


        private String convertPOJO2String(CostRecord costRecord) throws IllegalAccessException {
            Class clz = CostRecord.class;
            StringBuilder sb = new StringBuilder();
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object result = field.get(costRecord);
                if (result == null) {
                    sb.append(' ');
                } else if (result instanceof String) {
                    sb.append(result.toString());
                } else {
                    sb.append(String.valueOf(result));
                }
                sb.append("\t,");
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        }

    }

    static class Record {
        private String tradeNo;
        private String orderNo;
        private String createTime;
        private String paidTime;
        private String modifyTime;
        private String location;
        private String orderType;
        private String target;
        private String goodsName;
        private String money;
        private String inOutType;
        private String orderStatus;
        private String serviceCost;
        private String refundMoney;
        private String memo;
        private String tradeStatus;

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPaidTime() {
            return paidTime;
        }

        public void setPaidTime(String paidTime) {
            this.paidTime = paidTime;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getInOutType() {
            return inOutType;
        }

        public void setInOutType(String inOutType) {
            this.inOutType = inOutType;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getServiceCost() {
            return serviceCost;
        }

        public void setServiceCost(String serviceCost) {
            this.serviceCost = serviceCost;
        }

        public String getRefundMoney() {
            return refundMoney;
        }

        public void setRefundMoney(String refundMoney) {
            this.refundMoney = refundMoney;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(String tradeStatus) {
            this.tradeStatus = tradeStatus;
        }
    }
}
