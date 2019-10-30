package com.example.aes_pos_wallet.vo;


import com.example.aes_pos_wallet.utils.GsonUtils;

public class CodeVo {
    public final static int TYPE_ERROR = 0X00;
    public final static int TYPE_RECEIVE = 0X01;//收款
    public final static int TYPE_PAYMENT = 0X02;//付款
    public final static int TYPE_TRANSFER = 0X03;//转账

    private String amount;
    private String qrCode;
    private int type;
    private String name;
    private String userType;
    private String telPhone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    private CodeVo() {
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public double getDoubleAmount() {
        double amountDouble = 0.00;
        try {
            amountDouble = Double.valueOf(getAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amountDouble;
    }

    public String toString() {
        if (type == CodeVo.TYPE_PAYMENT) {
            return "{\"qrCode\":\"" + qrCode + "\",\"type\":" + TYPE_PAYMENT + "}";
        } else {
            return GsonUtils.get().toJson(this, CodeVo.class);
        }
    }
}
