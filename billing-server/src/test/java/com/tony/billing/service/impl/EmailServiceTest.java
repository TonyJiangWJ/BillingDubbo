package com.tony.billing.service.impl;

import com.tony.billing.constants.enums.EnumMailTemplateName;
import com.tony.billing.service.BaseServiceNoTransactionalTest;
import com.tony.billing.service.api.EmailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;

/**
 * @author jiangwenjie 2021/2/7
 */
public class EmailServiceTest extends BaseServiceNoTransactionalTest {
    @Autowired
    private EmailService emailService;

    @Test
    public void testSendMail() {
        emailService.sendSimpleMail("781027548@qq.com", "Testing", "<div>Hello, world</div>");
    }

    @Test
    public void testSendHtmlMail() throws MessagingException {
        emailService.sendThymeleafMail("tony-jiang@outlook.com", "Testing", null, EnumMailTemplateName.TEST_MAIL.getTemplateName());
    }
}
