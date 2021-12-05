package com.tony.controller;

import com.tony.BaseTest;
import com.tony.billing.util.ResponseUtil;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author jiangwenjie 2021/1/22
 */
public class PreSaleTest extends BaseTest {

    @Test
    public void testQueryPreSale() throws Exception {
        mockMvc.perform(
                post("/bootDemo/fund/pre/sale/page")
                        .cookie(cookies)
        ).andDo(print())
                .andExpect(jsonPath("$.code", is(ResponseUtil.success().getCode())));
    }
}
