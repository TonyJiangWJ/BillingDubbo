package com.tony.billing.request;

/**
 * @author jiangwj20966 on 2017/8/9.
 */
public class RealNameRequest extends BaseRequest {
    private String realName;
    private String cardNo;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
