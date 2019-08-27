package com.tony.billing.service.api;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * @author jiangwj20966 8/3/2018
 */
public interface EmailService {
    /**
     * 发送纯文本邮件
     * @param sendTo 邮件地址
     * @param title 标题
     * @param content 内容
     */
    void sendSimpleMail(String sendTo, String title, String content);

    /**
     * 发送基于thymeleaf的邮件
     * @param sendTo 邮件地址
     * @param title 标题
     * @param contents 内容
     * @param templateName 邮件模板名称
     */
    void sendThymeleafMail(String sendTo, String title, Map<String, Object> contents, String templateName) throws MessagingException;
}
