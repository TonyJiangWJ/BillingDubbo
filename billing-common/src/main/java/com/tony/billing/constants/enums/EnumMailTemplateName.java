package com.tony.billing.constants.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangwenjie 2019-02-02
 */
public enum EnumMailTemplateName {
    /**
     *
     */
    VERIFY_CODE_MAIL("mail.html", "验证码"),
    RESET_PWD_MAIL("resetMail.html", "重置密码");

    private String templateName;
    private String desc;

    EnumMailTemplateName(String templateName, String desc) {
        this.templateName = templateName;
        this.desc = desc;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getDesc() {
        return desc;
    }

    public static EnumMailTemplateName getByName(String templateName) {
        for (EnumMailTemplateName enumEntry : values()) {
            if (StringUtils.equals(templateName, enumEntry.getTemplateName())) {
                return enumEntry;
            }
        }
        return null;
    }

}
