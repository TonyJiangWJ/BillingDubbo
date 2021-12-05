package com.tony;

import com.alibaba.fastjson.JSON;
import com.tony.billing.util.RSAUtil;
import com.tony.billing.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jiangwenjie 2020/11/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DubboConsumerTestApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class BaseTest {

    private String mockCookiePath = "mock-cookie.log";

    @Autowired
    private RSAUtil rsaUtil;
    @Autowired
    public MockMvc mockMvc;

    public MockHttpSession session;

    public Cookie[] cookies;

    private final String ACCOUNT_NO = "justtest";
    private final String PASSWORD = "123456";

    @Before
    public void setup() throws Exception {

        MockCookie mockCookie = getMockCookie();
        this.cookies = new Cookie[]{mockCookie};
        this.mockMvc.perform(post("/bootDemo/login/status").cookie(mockCookie)).andDo(
                checkResult -> {
                    String resultContent = checkResult.getResponse().getContentAsString();
                    String resultCode = "";
                    if (StringUtils.isNotEmpty(resultContent)) {
                        resultCode = JSON.parseObject(resultContent).getString("code");
                        if (ResponseUtil.success().getCode().equals(resultCode)) {
                            return;
                        }
                    }
                    log.info("登录信息已过期，重新登录");
                    this.mockMvc.perform(post("/bootDemo/user/login")
                            .param("userName", ACCOUNT_NO)
                            .param("password", rsaUtil.encryptWithPubKey(PASSWORD)))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code", is(ResponseUtil.success().getCode())))
                            .andDo(print())
                            .andDo((result) -> {
                                session = (MockHttpSession)result.getRequest().getSession();
                                cookies = result.getResponse().getCookies();
                                if (cookies.length > 0) {
                                    Stream.of(cookies).filter(cookie -> cookie.getName().equals("token"))
                                            .findAny().ifPresent(cookie -> {
                                                saveCookie(cookie.getValue());
                                    });
                                }
                            });

                }
        );

    }

    @Test
    public void test() throws Exception {
        log.info("success");
        mockMvc.perform(
                post("/bootDemo/fund/changed/get")
                        .param("assessmentDate", "2020-01-05")
                        .cookie(cookies)
        ).andDo(print()).andExpect(jsonPath("$.code", is(ResponseUtil.success().getCode())));
    }

    private void saveCookie(String cookieContent) {
        File file = new File(mockCookiePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(cookieContent.getBytes());
        } catch (Exception e) {
            log.error("保存cookie信息失败");
        }
    }

    private MockCookie getMockCookie() {
        MockCookie cookie = new MockCookie("token", "");
        cookie.setHttpOnly(true);
        File file = new File(mockCookiePath);
        if (!file.exists()) {
            return cookie;
        }
        try (FileInputStream fis = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String content = reader.readLine();
            if (StringUtils.isNotEmpty(content)) {
                cookie.setValue(content);
            }
        } catch (Exception e) {
            log.error("保存cookie信息失败");
        }
        return cookie;
    }
}
