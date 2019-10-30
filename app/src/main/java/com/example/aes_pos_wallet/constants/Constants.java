package com.example.aes_pos_wallet.constants;

import android.text.TextUtils;

import com.example.aes_pos_wallet.device.S_DeviceInfo;
import com.example.aes_pos_wallet.utils.HexUtil;
import com.example.aes_pos_wallet.utils.SpUtils;


public class Constants {
    public final static String AES_TERMINAL_CODE = "terminalCode";
    public final static String AES_TOKEN_VALUE = "TokenValue";
    public static final String SP_Address = "AES_SP_Address";
    private static String TOKEN = "";//登录签名
    private static String terminalCode = "";//设备编号

    public static String getTOKEN() {
        return TOKEN;
    }

    public static String getTerminalCode() {
        if (TextUtils.isEmpty(terminalCode)) {
            try {
                terminalCode = S_DeviceInfo.getInstance().getDevice().getDeviceInfo().getSN();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return terminalCode;
    }

    public static void init() {
        TOKEN = SpUtils.get().get(AES_TOKEN_VALUE, "");
        terminalCode = SpUtils.get().get(AES_TERMINAL_CODE, "");
    }

    public static void updateToken(String token) {
        TOKEN = token;
        SpUtils.get().save(AES_TOKEN_VALUE, token);
    }

    public static void updateSN(String sn) {
        terminalCode = sn;
        SpUtils.get().save(AES_TERMINAL_CODE, sn);
    }

    public static class ScanType {
        /**
         * Front scan
         */
        public static final int FRONT = 1;
        /**
         * Back scan
         */
        public static final int BACK = 0;
    }

    public static class ScanResult {
        public static final int SCAN_FINISH = 0;
        public static final int SCAN_RESPONSE = 1;
        public static final int SCAN_ERROR = 2;
        public static final int SCAN_TIMEOUT = 3;
        public static final int SCAN_CANCEL = 4;
    }

    private static String success = "<STX><0025><SET><01><00><VOICE=0008收款成功><ETX>";
    public static String successCode = HexUtil.string2HexString(success + "<" + HexUtil.xorHexString(HexUtil.string2HexString(success)) + ">");
    private static String errTwo = "<STX><0027><SET><01><00><VOICE=0010二维码无效><ETX>";
    public static String errTwoCode = HexUtil.string2HexString(errTwo + "<" + HexUtil.xorHexString(HexUtil.string2HexString(errTwo)) + ">");
    private static String fail = "<STX><0025><SET><01><00><VOICE=0008收款失败><ETX>";
    public static String failCode = HexUtil.string2HexString(fail + "<" + HexUtil.xorHexString(HexUtil.string2HexString(fail)) + ">");
    private static String lossPay = "<STX><0025><SET><01><00><VOICE=0008余额不足><ETX>";
    public static String lossPayCode = HexUtil.string2HexString(lossPay + "<" + HexUtil.xorHexString(HexUtil.string2HexString(lossPay)) + ">");
    private static String outPay = "<STX><0029><SET><01><00><VOICE=0012不在消费时段><ETX>";
    public static String outPayCode = HexUtil.string2HexString(outPay + "<" + HexUtil.xorHexString(HexUtil.string2HexString(outPay)) + ">");
    private static String doublePay = "<STX><0037><SET><01><00><VOICE=0020不可在该时段重复消费><ETX>";
    public static String doublePayCode = HexUtil.string2HexString(doublePay + "<" + HexUtil.xorHexString(HexUtil.string2HexString(doublePay)) + ">");


}
