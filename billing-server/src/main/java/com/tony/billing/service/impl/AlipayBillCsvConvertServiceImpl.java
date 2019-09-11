package com.tony.billing.service.impl;

import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.enums.EnumHidden;
import com.tony.billing.dao.mapper.CostRecordMapper;
import com.tony.billing.entity.CostRecord;
import com.tony.billing.service.api.AlipayBillCsvConvertService;
import com.tony.billing.service.base.AbstractService;
import com.tony.billing.util.MoneyUtil;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author by TonyJiang on 2017/6/3.
 */
@Service
public class AlipayBillCsvConvertServiceImpl extends AbstractService<CostRecord, CostRecordMapper> implements AlipayBillCsvConvertService {

    private Logger logger = LoggerFactory.getLogger(AlipayBillCsvConvertService.class);

    private final String BACKUP_FLAG = "交易号,订单号,创建时间,支付时间,修改时间,地点,订单类型,交易对方,商品名称,金额,支出/收入,订单状态,服务费,退款,备注,交易状态,是否删除,id,是否隐藏";

    @Override
    public void convertToPOJO(List<String> fixedList) {
        RecordRefUtil<Record> recordRefUtil = new RecordRefUtil<>();
        if (!CollectionUtils.isEmpty(fixedList)) {
            List<CostRecord> insertList = fixedList.stream()
                    .map(csvLine -> {
                        try {
                            return recordRefUtil.convertCsv2POJO(csvLine, Record.class);
                        } catch (IllegalAccessException | InstantiationException e) {
                            logger.error("转换CSV文件内容失败", e);
                        }
                        return null;
                    })
                    .map(this::convertToDoForInsert)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(insertList)) {
                mapper.batchInsert(insertList);
            }
        }

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
    public void getFromBackUp(List<String> fixedList) {

        RecordRefUtil<CostRecord> recordRefUtil = new RecordRefUtil<>();
        if (!CollectionUtils.isEmpty(fixedList)) {
            List<CostRecord> insertList = fixedList.stream()
                    .map(csvLine -> {
                        try {
                            return recordRefUtil.convertCsv2POJO(csvLine, CostRecord.class);
                        } catch (IllegalAccessException | InstantiationException e) {
                            logger.error("转换备份文件内容失败", e);
                        }
                        return null;
                    })
                    .map(this::convertToDBJOAndInsertCostRecord)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(insertList)) {
                mapper.batchInsert(insertList);
            }
        }

    }

    private CostRecord convertToDBJOAndInsertCostRecord(CostRecord entity) {
        if (entity == null) {
            return null;
        }
        Long userId = UserIdContainer.getUserId();
        Integer recordCount = mapper.checkTradeExistence(entity.getTradeNo(), userId);
        if (recordCount == 0) {
            entity.setId(null);
            entity.setUserId(userId);
            entity.setCreateTime(new Date());
            entity.setModifyTime(new Date());
            return entity;
        } else {
            logger.error("record already exist");
        }
        return null;
    }


    private CostRecord convertToDoForInsert(Record entity) {
        if (entity == null) {
            return null;
        }
        Long userId = UserIdContainer.getUserId();
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
            return record;
        } else {
            logger.warn("record already exist");
        }
        return null;
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
                    sb.append(result);
                } else {
                    sb.append(result);
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
