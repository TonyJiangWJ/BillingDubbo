package com.tony.billing.controller;

import com.tony.billing.constants.TradeStatus;
import com.tony.billing.constants.timing.TimeConstants;
import com.tony.billing.dto.CostRecordDTO;
import com.tony.billing.dto.CostRecordDetailDTO;
import com.tony.billing.dto.TagInfoDTO;
import com.tony.billing.entity.CostRecord;
import com.tony.billing.entity.PagerGrid;
import com.tony.billing.entity.TagInfo;
import com.tony.billing.entity.query.CostRecordQuery;
import com.tony.billing.request.BaseRequest;
import com.tony.billing.request.costrecord.CostRecordBatchDeleteRequest;
import com.tony.billing.request.costrecord.CostRecordBatchHideRequest;
import com.tony.billing.request.costrecord.CostRecordDeleteRequest;
import com.tony.billing.request.costrecord.CostRecordDetailRequest;
import com.tony.billing.request.costrecord.CostRecordHideRequest;
import com.tony.billing.request.costrecord.CostRecordPageRequest;
import com.tony.billing.request.costrecord.CostRecordPutRequest;
import com.tony.billing.request.costrecord.CostRecordUpdateRequest;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.response.costrecord.CostRecordDeleteResponse;
import com.tony.billing.response.costrecord.CostRecordDetailResponse;
import com.tony.billing.response.costrecord.CostRecordPageResponse;
import com.tony.billing.service.api.CostRecordService;
import com.tony.billing.service.api.TagInfoService;
import com.tony.billing.util.MoneyUtil;
import com.tony.billing.util.ResponseUtil;
import com.tony.billing.utils.AlipayBillCsvUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
@RestController
@RequestMapping(value = "/bootDemo")
public class CostRecordController {
    private Logger logger = LoggerFactory.getLogger(CostRecordController.class);
    @Reference
    private CostRecordService costRecordService;
    @Reference
    private TagInfoService tagInfoService;

    @Resource
    private AlipayBillCsvUtil alipayBillCsvUtil;

    /**
     * 获取分页数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/cost/record/page/get")
    public CostRecordPageResponse getPage(@ModelAttribute("request") CostRecordPageRequest request) {
        CostRecordPageResponse response = new CostRecordPageResponse();
        try {
            CostRecordQuery costRecord = new CostRecordQuery();
            if (request.getIsDeleted() != null) {
                costRecord.setIsDeleted(request.getIsDeleted());
            }
            if (StringUtils.isNotEmpty(request.getInOutType())) {
                costRecord.setInOutType(request.getInOutType());
            }
            if (StringUtils.isNotEmpty(request.getEndDate())) {
                String endDate = request.getEndDate();
                Integer value = Integer.valueOf(endDate.substring(8));
                endDate = endDate.substring(0, 8) + String.format("%02d", ++value);
                costRecord.setEndDate(endDate);
            }
            if (request.getIsHidden() != null) {
                costRecord.setIsHidden(request.getIsHidden());
            }
            if (StringUtils.isNotEmpty(request.getContent())) {
                costRecord.setContent(request.getContent());
            }
            costRecord.setStartDate(request.getStartDate());
            costRecord.setUserId(request.getUserId());
            PagerGrid<CostRecord> pagerGrid = new PagerGrid<>(costRecord);
            if (request.getPageSize() != null && !request.getPageSize().equals(0)) {
                pagerGrid.setOffset(request.getPageSize());
            }
            pagerGrid.setPage(request.getPageNo() == null ? 0 : request.getPageNo());
            if (StringUtils.isNotEmpty(request.getSort())) {
                pagerGrid.setSort(request.getSort());
            } else {
                pagerGrid.setSort("desc");
            }
            if (StringUtils.isNotEmpty(request.getOrderBy())) {
                pagerGrid.setOrderBy(request.getOrderBy());
            } else {
                pagerGrid.setOrderBy("costCreateTime");
            }

            pagerGrid = costRecordService.page(pagerGrid);
            boolean showTags = Boolean.TRUE.equals(request.getShowTags());
            response.setCostRecordList(formatModelList(pagerGrid.getResult(), showTags));
            response.setCurrentAmount(calculateCurrentAmount(pagerGrid.getResult()));
            response.setPageNo(pagerGrid.getPage());
            response.setPageSize(pagerGrid.getOffset());
            response.setTotalPage(pagerGrid.getTotalPage());
            response.setTotalItem(pagerGrid.getCount());
            ResponseUtil.success(response);

        } catch (Exception e) {
            logger.error("/page/get error", e);
            ResponseUtil.sysError(response);
        }

        return response;
    }

    private String calculateCurrentAmount(List<CostRecord> result) {
        long total = result.stream().map(CostRecord::getMoney).reduce((a, b) -> a + b).orElse(0L);
        return MoneyUtil.fen2Yuan(total);
    }

    /**
     * 获取详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/record/detail/get")
    public CostRecordDetailResponse getDetail(@ModelAttribute("request") @Validated CostRecordDetailRequest request) {
        CostRecordDetailResponse response = new CostRecordDetailResponse();
        ResponseUtil.error(response);
        try {
            CostRecord record = costRecordService.findByTradeNo(request.getTradeNo(), request.getUserId());
            if (record != null) {
                response.setRecordDetail(formatDetailModel(record));
                ResponseUtil.success(response);
            }
        } catch (Exception e) {
            logger.error("/detail/get error", e);
        }
        return response;

    }

    /**
     * 修改消费记录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/record/update")
    public BaseResponse updateRecord(@ModelAttribute("request") @Validated CostRecordUpdateRequest request) {
        BaseResponse response = new BaseResponse();

        CostRecord record = new CostRecord();
        record.setLocation(request.getLocation());
        record.setGoodsName(request.getGoodsName());
        record.setMemo(request.getMemo());
        record.setTradeNo(request.getTradeNo());
        record.setUserId(request.getUserId());
        record.setOrderType(request.getOrderType());
        record.setVersion(request.getVersion());
        if (costRecordService.updateByTradeNo(record) > 0) {
            return ResponseUtil.success(response);
        }
        return ResponseUtil.error(response);
    }

    /**
     * 标记删除
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/record/toggle/delete")
    public CostRecordDeleteResponse deleteRecord(@ModelAttribute("request") @Validated CostRecordDeleteRequest request) {
        CostRecordDeleteResponse response = new CostRecordDeleteResponse();
        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("tradeNo", request.getTradeNo());
            params.put("nowStatus", request.getNowStatus());
            params.put("isDeleted", request.getNowStatus().equals(0) ? 1 : 0);
            params.put("userId", request.getUserId());
            if (costRecordService.toggleDeleteStatus(params) > 0) {
                ResponseUtil.success(response);
            } else {
                ResponseUtil.error(response);
            }
        } catch (Exception e) {
            logger.error("/delete error", e);
            ResponseUtil.sysError(response);
        }
        return response;
    }

    @RequestMapping(value = "/record/toggle/hide")
    public BaseResponse toggleHiddenStatus(@ModelAttribute("request") @Validated CostRecordHideRequest request) {
        BaseResponse response = new BaseResponse();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("tradeNo", request.getTradeNo());
            params.put("nowStatus", request.getNowStatus());
            params.put("isHidden", request.getNowStatus().equals(0) ? 1 : 0);
            params.put("userId", request.getUserId());
            if (costRecordService.toggleHideStatus(params) > 0) {
                ResponseUtil.success(response);
            } else {
                ResponseUtil.error(response);
            }
        } catch (Exception e) {
            logger.error("/toggle/hide error", e);
            ResponseUtil.sysError(response);
        }
        return response;
    }

    @RequestMapping(value = "/record/batch/delete", method = RequestMethod.POST)
    public BaseResponse batchUpdateDeleteStatus(@ModelAttribute("request") @Validated CostRecordBatchDeleteRequest request) {
        try {
            if (costRecordService.batchToggleDeleteStatus(request.getCostIds(), request.getIsDeleted()) > 0) {
                return ResponseUtil.success();
            } else {
                return ResponseUtil.error();
            }
        } catch (Exception e) {
            logger.error("/record/batch/delete error", e);
            return ResponseUtil.sysError();
        }
    }

    @RequestMapping(value = "/record/batch/hide", method = RequestMethod.POST)
    public BaseResponse batchUpdateHiddenStatus(@ModelAttribute("request") @Validated CostRecordBatchHideRequest request) {
        try {
            if (costRecordService.batchToggleHiddenStatus(request.getCostIds(), request.getIsHidden()) > 0) {
                return ResponseUtil.success();
            } else {
                return ResponseUtil.error();
            }
        } catch (Exception e) {
            logger.error("/record/batch/hide error", e);
            return ResponseUtil.sysError();
        }
    }

    /**
     * 添加消费记录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/record/put")
    public BaseResponse putDetail(@ModelAttribute("request") @Validated CostRecordPutRequest request) {

        BaseResponse response = new BaseResponse();
        try {
            CostRecord record = new CostRecord();
            record.setTradeStatus(TradeStatus.TRADE_SUCCESS);
            record.setTradeNo(generateTradeNo(request.getCreateTime()));
            record.setTarget(request.getTarget());
            record.setPaidTime(request.getCreateTime());
            record.setOrderType(request.getOrderType());
            record.setMoney(MoneyUtil.yuan2fen(request.getMoney()));
            record.setCostCreateTime(request.getCreateTime());
            record.setIsDeleted(0);
            record.setOrderStatus(TradeStatus.TRADE_SUCCESS);
            record.setInOutType(request.getInOutType());
            record.setMemo(request.getMemo());
            record.setGoodsName(request.getMemo());
            record.setLocation(request.getLocation());
            record.setUserId(request.getUserId());
            if (costRecordService.orderPut(record) > 0) {
                ResponseUtil.success(response);
            } else {
                ResponseUtil.error(response);
            }
        } catch (Exception e) {
            logger.error("/record/put error", e);
            ResponseUtil.sysError(response);
        }
        return response;
    }

    @RequestMapping("/record/csv/convert")
    public BaseResponse doConvert(@ModelAttribute("file") MultipartFile file, @ModelAttribute("request") BaseRequest request) {

        try {
            if (alipayBillCsvUtil.convertToPOJO(file)) {
                return ResponseUtil.success();
            } else {
                return ResponseUtil.error();
            }
        } catch (Exception e) {
            logger.error("backup/csv/put error", e);
            BaseResponse response = ResponseUtil.error();
            response.setMsg("文件错误请检查");
            return response;
        }
    }

    @RequestMapping("/record/backup/csv/get")
    public void backUp(HttpServletResponse response, @ModelAttribute("request") BaseRequest request) {
        CostRecord requestParam = new CostRecord();
        requestParam.setUserId(request.getUserId());
        List<CostRecord> records = costRecordService.find(requestParam);

        List<String> result = alipayBillCsvUtil.convertPOJO2String(records);

        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + "backup" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv");
            OutputStream outputStream = response.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream, "GBK");
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (String rs : result) {
                bufferedWriter.write(rs);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
        } catch (IOException e) {
            logger.error("创建备份文件失败", e);
        }
    }

    @RequestMapping("/record/backup/csv/put")
    public BaseResponse getFromBackUp(@ModelAttribute("file") MultipartFile file, @ModelAttribute("request") BaseRequest request) {

        try {
            if (alipayBillCsvUtil.getFromBackup(file)) {
                return ResponseUtil.success();
            } else {
                return ResponseUtil.error();
            }
        } catch (Exception e) {
            logger.error("backup/csv/put error", e);
            BaseResponse response = ResponseUtil.error();
            response.setMsg("文件错误请检查");
            return response;
        }
    }

    private String generateTradeNo(String createTime) throws ParseException {
        LocalDateTime datetime = LocalDateTime.parse(createTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String dateCode = datetime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return dateCode + datetime.toInstant(TimeConstants.CHINA_ZONE_OFFSET).toEpochMilli() / 1000 % 1000000000;
    }

    private CostRecordDetailDTO formatDetailModel(CostRecord record) {
        CostRecordDetailDTO model = new CostRecordDetailDTO();
        model.setId(record.getId());
        model.setVersion(record.getVersion());
        model.setCreateTime(record.getCostCreateTime());
        model.setGoodsName(record.getGoodsName());
        model.setInOutType(record.getInOutType());
        model.setIsDeleted(record.getIsDeleted());
        model.setLocation(record.getLocation());
        model.setMemo(record.getMemo());
        model.setMoney(MoneyUtil.fen2Yuan(record.getMoney()));
        model.setModifyTime(record.getCostModifyTime());
        model.setOrderNo(record.getOrderNo());
        model.setOrderStatus(record.getOrderStatus());
        model.setOrderType(record.getOrderType());
        model.setPaidTime(record.getPaidTime());
        model.setRefundMoney(MoneyUtil.fen2Yuan(record.getRefundMoney()));
        model.setServiceCost(MoneyUtil.fen2Yuan(record.getServiceCost()));
        model.setTarget(record.getTarget());
        model.setTradeNo(record.getTradeNo());
        model.setTradeStatus(record.getTradeStatus());
        model.setIsHidden(record.getIsHidden());
        List<TagInfo> tagInfos = tagInfoService.listTagInfoByTradeNo(record.getTradeNo());
        if (!CollectionUtils.isEmpty(tagInfos)) {
            model.setTagInfos(tagInfos.stream().map(tagInfo -> {
                        TagInfoDTO tagInfoDTO = new TagInfoDTO();
                        tagInfoDTO.setTagId(tagInfo.getId());
                        tagInfoDTO.setTagName(tagInfo.getTagName());
                        return tagInfoDTO;
                    }).collect(Collectors.toList())
            );
        }
        return model;
    }


    private List<CostRecordDTO> formatModelList(List<CostRecord> list, boolean showTags) {
        if (!CollectionUtils.isEmpty(list)) {
            List<CostRecordDTO> models = new ArrayList<>();
            CostRecordDTO model;
            List<TagInfo> tagInfos;
            for (CostRecord entity : list) {
                model = new CostRecordDTO();
                model.setId(entity.getId());
                model.setCreateTime(entity.getCostCreateTime());
                model.setGoodsName(entity.getGoodsName());
                model.setInOutType(entity.getInOutType());
                model.setIsDeleted(entity.getIsDeleted());
                model.setMoney(MoneyUtil.fen2Yuan(entity.getMoney()));
                model.setLocation(entity.getLocation());
                model.setOrderStatus(entity.getOrderStatus());
                model.setOrderType(entity.getOrderType());
                model.setTradeNo(entity.getTradeNo());
                model.setTarget(entity.getTarget());
                model.setMemo(entity.getMemo());
                model.setIsHidden(entity.getIsHidden());
                if (showTags) {
                    tagInfos = tagInfoService.listTagInfoByTradeNo(entity.getTradeNo());
                    if (!CollectionUtils.isEmpty(tagInfos)) {
                        model.setTags(tagInfos.stream().map(TagInfo::getTagName).collect(Collectors.toList()));
                    }
                }
                models.add(model);
            }
            return models;
        } else {
            return null;
        }

    }
}
