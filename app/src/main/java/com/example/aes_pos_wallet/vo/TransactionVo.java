package com.example.aes_pos_wallet.vo;


import android.text.TextUtils;


import com.example.aes_pos_wallet.utils.DateUtils;

import java.text.ParseException;

/**
 * Created by Nothing on 2019/7/31.
 */

public class TransactionVo {
    public static final String ORDER_TYPE_CONSUME = "consume";
    public static final String ORDER_TYPE_RECHARGE = "recharge";
    public static final String ORDER_TYPE_TRANSFER = "transfer";
    public static final String ORDER_TYPE_REFUND = "refund";

    public static final String USER_TYPE_USER = "user";
    public static final String USER_TYPE_SHOP = "shop";

    /* 命名规则： user: "nickname(*+name)"*/

    private boolean showDate = false;
    private String orderId;
    private double payAmount;
    private String orderNo;
    private String orderType;
    private String finishTime;
    private String receiverName;
    private String receiverNickName;
    private String sendUserId;
    private String receiverUserId;
    private String sendUserType;
    private String receiverUserType;
    private String sendName;
    private String sendNickName;
    private String sendLoginName;
    private String receiverLoginName;
    private String refundState;

    public String getSendLoginName() {
        if (sendLoginName != null && sendLoginName.length() > 7) {
            return "*** **** " + sendLoginName.substring(7);
        }
        return sendLoginName;
    }

    public void setSendLoginName(String sendLoginName) {
        this.sendLoginName = sendLoginName;
    }

    public String getReceiverLoginName() {
        if (receiverLoginName != null && receiverLoginName.length() > 7) {
            return "*** **** " + receiverLoginName.substring(7);
        }
        return receiverLoginName;
    }

    public void setReceiverLoginName(String receiverLoginName) {
        this.receiverLoginName = receiverLoginName;
    }

    public boolean getRefundState() {
        return !TextUtils.isEmpty(refundState) && refundState.endsWith("1");
    }

    public void setRefundState(String refundState) {
        this.refundState = refundState;
    }

    public String getReceiverShowName() {
//        if (getReceiverUserType().equals(TransactionVo.USER_TYPE_SHOP)) return getReceiverName();
        String realName = TextUtils.isEmpty(getReceiverName()) ? "" : ("*" + getReceiverName().substring(1));
        String name = TextUtils.isEmpty(getReceiverNickName()) ? getReceiverLoginName() : getReceiverNickName();
        String showName = TextUtils.isEmpty(realName) ? name : name + "(" + realName + ")";
        return showName;
    }

    public String getSendShowName() {
//        if (getSendUserType().equals(TransactionVo.USER_TYPE_SHOP)) return getSendName();
        String realName = TextUtils.isEmpty(getSendName()) ? "" : ("*" + getSendName().substring(1));
        String name = TextUtils.isEmpty(getSendNickName()) ? getSendLoginName() : getSendNickName();
        String showName = TextUtils.isEmpty(realName) ? name : name + "(" + realName + ")";
        return showName;
    }


    public String getReceiverNickName() {
        return receiverNickName;
    }

    public void setReceiverNickName(String receiverNickName) {
        this.receiverNickName = receiverNickName;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public long getFinishTimeLone() throws ParseException {
        return DateUtils.fromDateToHMS(finishTime);
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getSendUserType() {
        return sendUserType;
    }

    public void setSendUserType(String sendUserType) {
        this.sendUserType = sendUserType;
    }

    public String getReceiverUserType() {
        return receiverUserType;
    }

    public void setReceiverUserType(String receiverUserType) {
        this.receiverUserType = receiverUserType;
    }

    /**
     * test
     *
     * @param showDate
     */
    public TransactionVo(boolean showDate) {
        this.showDate = showDate;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }


}
