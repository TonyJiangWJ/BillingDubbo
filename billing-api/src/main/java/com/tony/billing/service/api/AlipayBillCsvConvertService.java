package com.tony.billing.service.api;

import com.tony.billing.entity.CostRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author by TonyJiang on 2017/6/3.
 */
public interface AlipayBillCsvConvertService {
    boolean convertToPOJO(MultipartFile multipartFile, Long userId);

    List<String> convertPOJO2String(List<CostRecord> recordList);

    boolean getFromBackUp(MultipartFile file, Long userId);
}
