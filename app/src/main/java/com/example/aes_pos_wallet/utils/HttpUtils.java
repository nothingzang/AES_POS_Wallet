package com.example.aes_pos_wallet.utils;

import android.text.TextUtils;
import android.util.Log;


import com.example.aes_pos_wallet.constants.Constants;
import com.example.aes_pos_wallet.constants.HttpAddress;
import com.example.aes_pos_wallet.constants.HttpConstants;
import com.example.aes_pos_wallet.listeners.CallBackListener;
import com.example.aes_pos_wallet.response.ResponseVo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static String TAG = "NOTHING" + HttpUtils.class.getSimpleName();

    public static void login(String loginName, String loginPwd, CallBackListener callBackListener) {
        Map<String, Object> data = new HashMap<>();
        data.put("loginName", loginName);
        data.put("loginPwd", loginPwd);
        data.put("deviceSn", Constants.getTerminalCode());
        post(HttpAddress.getHttpUrl(HttpAddress.WCMD_CUT_LOGIN, "dw-cashier-boss"), data, callBackListener);
    }

    public static void sendCutPayment(String qrcode, double price, CallBackListener callBackListener) {
        Map<String, Object> data = new HashMap<>();
        data.put("twoDimensional", qrcode);
        data.put("deviceSn", Constants.getTerminalCode());
        data.put("amount", price);
        post(HttpAddress.getHttpUrl(HttpAddress.WCMD_CUT_PAYMENT, "dw-cashier-boss"), data, callBackListener);
    }

    public static void getOrders(Object q, int page, int pageSize, CallBackListener callBackListener) {
        Map<String, Object> data = new HashMap<>();
        data.put("q", q);
        data.put("pageNo", page);
        data.put("pageSize", pageSize);
        post(HttpAddress.getHttpUrl(HttpAddress.WCMD_CUT_ORDERS, "dw-cashier-boss"), data, callBackListener);
    }

    private static void get(String path, Map<String, String> requestBody, final CallBackListener callBackListener) {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(path + "?");
        if (requestBody != null && requestBody.size() > 0)
            for (String key : requestBody.keySet()) {
                requestUrl.append(key + "=" + requestBody.get(key) + "&");
            }
        LogUtils.d(TAG, "requestUrl: " + requestUrl.toString());
        Request.Builder requestBuild = new Request.Builder()
                .url(requestUrl.toString());
        if (!TextUtils.isEmpty(Constants.getTOKEN())) {
            requestBuild.addHeader("Authorization", Constants.getTOKEN());
        }
        Request request = requestBuild.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callBackListener != null)
                    callBackListener.onFail(ResponseVo.getNetError(e.getMessage()));
                LogUtils.d(TAG, "onFailure: " + e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    LogUtils.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                String responseString = response.body().string();
                LogUtils.d(TAG, "onResponse: " + responseString);
                if (callBackListener != null) {
                    callBackListener.onEnd(responseString, HttpConstants.getTypeByUrl(path));
                }
            }
        });
    }

    private static void post(String path, Map requestBody, CallBackListener callBackListener) {
        Request.Builder requestBuild = new Request.Builder()
                .url(path);
        if (requestBody != null && requestBody.size() > 0)
            requestBuild.post(RequestBody.create(JSON, GsonUtils.get().toJson(requestBody)));
        else
            requestBuild.post(RequestBody.create(JSON, ""));
        if (!path.endsWith("login") && !TextUtils.isEmpty(Constants.getTOKEN())) {
            requestBuild.addHeader("Authorization", Constants.getTOKEN());
        }
        Request request = requestBuild.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("Nothing_time", "star_time=" + System.currentTimeMillis());
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Nothing_time", "end_time=" + System.currentTimeMillis());
                if (callBackListener != null)
                    callBackListener.onFail(ResponseVo.getNetError(e.getMessage()));
                LogUtils.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("Nothing_time", "end_time=" + System.currentTimeMillis());
                LogUtils.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    LogUtils.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                String responseString = response.body().string();
                LogUtils.d(TAG, "onResponse: " + responseString);
                if (callBackListener != null) {
                    callBackListener.onEnd(responseString, HttpConstants.getTypeByUrl(path));
                }
            }
        });
    }


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

}
