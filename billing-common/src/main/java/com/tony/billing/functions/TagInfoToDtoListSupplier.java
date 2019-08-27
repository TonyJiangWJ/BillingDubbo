package com.tony.billing.functions;

import com.tony.billing.dto.TagInfoDTO;
import com.tony.billing.entity.TagInfo;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author jiangwenjie 2019-03-15
 */
public class TagInfoToDtoListSupplier implements Supplier<List<TagInfoDTO>> {

    private List<TagInfo> tagInfos;

    public TagInfoToDtoListSupplier(List<TagInfo> tagInfos) {
        this.tagInfos = tagInfos;
    }


    @Override
    public List<TagInfoDTO> get() {
        if (CollectionUtils.isNotEmpty(tagInfos)) {
            return tagInfos.stream().map(tagInfo -> {
                TagInfoDTO tagInfoDTO = new TagInfoDTO();
                tagInfoDTO.setTagName(tagInfo.getTagName());
                tagInfoDTO.setTagId(tagInfo.getId());
                return tagInfoDTO;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
