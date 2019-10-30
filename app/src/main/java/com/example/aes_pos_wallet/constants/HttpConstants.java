package com.example.aes_pos_wallet.constants;

import com.example.aes_pos_wallet.response.ResponseVo;
import com.example.aes_pos_wallet.vo.TransactionPage;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HttpConstants {

    public static final int NET_FIAL_CODE = -1;//网络请求 非200 错误
    public static final int NULL_FIAL_CODE = -2;//请求结果为空
    public static final int SUCCESS_CODE = 0;//请求成功 code码
    public static final int TOKEN_OUTTIME_CODE = 500210;//TOKEN过期


    private static Map<String, Type> typeMap;


    public static void init() {
        if (typeMap == null)
            typeMap = new HashMap<>();
        else
            typeMap.clear();
        typeMap.put(HttpAddress.getHttpUrl(HttpAddress.WCMD_CUT_ORDERS, "dw-cashier-boss"), new TypeToken<ResponseVo<TransactionPage>>() {
        }.getType());
//        typeMap.put(HttpAddress.LOGIN, new TypeToken<ResponseVo<LoginVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.LOGINV, new TypeToken<ResponseVo<LoginVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.BALANCE, new TypeToken<ResponseVo<BalanceVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.PAYMENTCODE, new TypeToken<ResponseVo<PaymentCodeResVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.RECHARGE, new TypeToken<ResponseVo<RechargeResponseVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.TRADERECORD, new TypeToken<ResponseVo<TransactionPage>>() {
//        }.getType());
//        typeMap.put(HttpAddress.UPMPCALLBACK, new TypeToken<ResponseVo<JPMessageVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.UPDATEUSER, new TypeToken<ResponseVo<UserVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.AUTHENTICATION, new TypeToken<ResponseVo<UserAuthVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.MANDATORY, new TypeToken<ResponseVo<VersionVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.ALL_WALLET, new TypeToken<ResponseVo<WalletsResponseVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.RECEIVE_PAYMENTCODE, new TypeToken<ResponseVo<TwoDimensionalBaseVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.GET_ALLBANKCARDS, new TypeToken<ResponseVo<List<CardVo>>>() {
//        }.getType());
//        typeMap.put(HttpAddress.TEMCARD_WALLETS, new TypeToken<ResponseVo<List<TemporatCardVo>>>() {
//        }.getType());
//        typeMap.put(HttpAddress.CONSUMEBYPAYPASSWORD, new TypeToken<ResponseVo<JPMessageVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.MOBILE, new TypeToken<ResponseVo<MobileVo>>() {
//        }.getType());
//        typeMap.put(HttpAddress.CHANGE_FREE_PAY, new TypeToken<ResponseVo<FreePayVo>>() {
//        }.getType());

    }

    public static Type getTypeByUrl(String url) {
        if (typeMap != null)
            return typeMap.get(url) == null ? ResponseVo.class : typeMap.get(url);
        return ResponseVo.class;
    }
}
