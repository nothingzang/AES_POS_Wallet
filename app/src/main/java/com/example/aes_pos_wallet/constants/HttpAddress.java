package com.example.aes_pos_wallet.constants;

public class HttpAddress {
 //    public final static String SERVER_URL = "https://dc.arxanfintech.com/"; // 生产环境
//    public final static String SERVER_URL = "https://szdc.arxanfintech.com/"; // 测试环境
//    public final static String SERVER_URL = "https://szdc.arxanfintech.com:64301/"; // 测试环境
    public final static String SERVER_URL = "http://192.168.200.88:8098/"; // 本机环境
//    public final static String SERVER_URL = "http://192.168.200.108:8080/"; // 本机环境
//    public final static String SERVER_URL = "https://gtxcst.arxandt.com:9003/"; // 测试环境
//    public final static String SERVER_URL = "https://mvpdca.arxandt.com:9003/"; // 测试环境
    public final static String WCMD_PRODUCT = "dw/";
    public final static String WCMD_PROJECT = "";
    public final static String WCMD_VERSION = "v1/";
    public final static String WCMD_PLATFORM = "1/";
    // 获取每日交易记录
    public final static String WCMD_GET_DAILY_TRANSACTION_REPORT =
            "getDailyTransactionReport/";
    // 向cbdc上报优惠信息
    public final static String WCMD_REPORT_TX_DISCOUNT = "reportTxDiscount/";

    // 上报付款二维码及价格等信息
    public final static String WCMD_CUT_PAYMENT = "cashier/cutPayment";

    // 登录
    public final static String WCMD_CUT_LOGIN = "cashier/login";


    public final static String WCMD_CUT_ORDERS = "cashier/orders";

    /**
     * 生成HTTP请求基础部分URL，调用者可以在后面添加自定义的参数
     *
     * @param httpCmd
     * @return
     */
    public static String getHttpUrl(String httpCmd) {
        return SERVER_URL + WCMD_PRODUCT + WCMD_PROJECT +
                WCMD_VERSION + WCMD_PLATFORM + httpCmd;
    }

    /**
     * 生成HTTP请求基础部分URL，调用者可以在后面添加自定义的参数
     *
     * @param httpCmd
     * @return
     */
    public static String getHttpUrl(String httpCmd, String system) {
        return SERVER_URL + system + "/" + WCMD_PRODUCT + WCMD_PROJECT +
                WCMD_VERSION + WCMD_PLATFORM + httpCmd;
    }
}
