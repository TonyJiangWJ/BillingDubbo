package com.tony.billing.service.impl;

import com.tony.billing.constants.enums.EnumMailTemplateName;
import com.tony.billing.exceptions.BaseBusinessException;
import com.tony.billing.service.api.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.apache.dubbo.config.annotation.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @author jiangwj20966 8/3/2018
 */
@Service
public class EmailServiceImpl implements EmailService {


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine thymeleaf;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleMail(String sendTo, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(sendTo);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendThymeleafMail(String sendTo, String title, Map<String, Object> contents, String templateName) throws MessagingException {
        if (EnumMailTemplateName.getByName(templateName) == null) {
            throw new BaseBusinessException("邮件模板不存在");
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(sendTo);
        messageHelper.setSubject(title);
        Context context = new Context();
        if (contents != null) {
            for (Map.Entry<String, Object> content : contents.entrySet()) {
                context.setVariable(content.getKey(), content.getValue());
            }
        }
        String emailText = thymeleaf.process(templateName, context);
        messageHelper.setText(emailText, true);
        mailSender.send(message);
    }
}
