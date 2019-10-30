package com.example.aes_pos_wallet.activitys;
/**
 * Created by giho on 2018/6/19.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.aes_pos_wallet.device.S_DeviceInfo;
import com.example.aes_pos_wallet.listeners.CallBackListener;
import com.example.aes_pos_wallet.response.ResponseVo;
import com.example.aes_pos_wallet.utils.BytesUtils;
import com.example.aes_pos_wallet.utils.DataForwardController;
import com.example.aes_pos_wallet.utils.HexUtil;
import com.example.aes_pos_wallet.utils.HttpUtils;
import com.example.aes_pos_wallet.utils.SoundUtils;
import com.example.aes_pos_wallet.utils.ToastUtils;
import com.example.aes_pos_wallet.vo.CodeVo;
import com.example.aes_pos_wallet.vo.VoiceTemplate;
import com.example.aes_pos_wallet.vo.unionVo.UnionCodeValue;
import com.example.aes_pos_wallet.vo.unionVo.UnionVoCode;
import com.google.gson.Gson;
import com.newland.mtype.module.common.scanner.BarcodeScanner;
import com.newland.mtype.module.common.scanner.ScanInitEntry;
import com.newland.mtype.module.common.scanner.ScanListener;
import com.newland.mtype.module.common.scanner.ScanResultEntry;
import com.newland.mtype.module.common.scanner.ScannerListener;

import java.util.concurrent.TimeUnit;


/**
 * Author:Created by giho on 2018/6/19
 * Describe:
 */


public class SanMaActivity extends Activity {
    private String success = "<STX><0025><SET><01><00><VOICE=0008收款成功><ETX>";
    private String successCode = HexUtil.string2HexString(success + "<" + HexUtil.xorHexString(HexUtil.string2HexString(success)) + ">");
    private String errTwo = "<STX><0027><SET><01><00><VOICE=0010二维码无效><ETX>";
    private String errTwoCode = HexUtil.string2HexString(errTwo + "<" + HexUtil.xorHexString(HexUtil.string2HexString(errTwo)) + ">");
    private String fail = "<STX><0025><SET><01><00><VOICE=0008收款失败><ETX>";
    private String failCode = HexUtil.string2HexString(fail + "<" + HexUtil.xorHexString(HexUtil.string2HexString(fail)) + ">");
    private String lossPay = "<STX><0025><SET><01><00><VOICE=0008余额不足><ETX>";
    private String lossPayCode = HexUtil.string2HexString(lossPay + "<" + HexUtil.xorHexString(HexUtil.string2HexString(lossPay)) + ">");
    private String outPay = "<STX><0029><SET><01><00><VOICE=0012不在消费时段><ETX>";
    private String outPayCode = HexUtil.string2HexString(outPay + "<" + HexUtil.xorHexString(HexUtil.string2HexString(outPay)) + ">");
    private String doublePay = "<STX><0037><SET><01><00><VOICE=0020不可在该时段重复消费><ETX>";
    private String doublePayCode = HexUtil.string2HexString(doublePay + "<" + HexUtil.xorHexString(HexUtil.string2HexString(doublePay)) + ">");


    private BarcodeScanner scannerModule;
    double price;
    private Handler handler = new Handler();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            scannerModule.stopScan();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        price = getIntent().getDoubleExtra("price", 0);
//        setContentView(R.layout.sanma_activity);
        try {
            scannerModule = S_DeviceInfo.getInstance().getScanner();
            //扫码
            doSaoma();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void doSaoma(String code) {
        Gson gson = new Gson();
        try {
            if (code.startsWith("dw00")) {
                sendCutPayment(code.substring(4), price);
                return;
            } else if (code.startsWith("hQVDUFY")) {
                String result = HexUtil.bytesToHex(android.util.Base64.decode(code, android.util.Base64.NO_WRAP));
                UnionVoCode unionVoCode = UnionVoCode.getVoByCode(result);
                sendCutPayment(UnionCodeValue.getVoByCode(unionVoCode.getValue().getValue()).getTokenCode(), price);
                return;
            }
            CodeVo codeVo = gson.fromJson(code, CodeVo.class);
            Log.e("Nothing_code", codeVo.toString());
            if (codeVo.getType() != CodeVo.TYPE_PAYMENT) {
                SoundUtils.getInstance().queueSound(new VoiceTemplate()
                        .prefix("fail_code")
                        .gen());
                DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(errTwoCode));
                doSaoma();
                return;
            }
            sendCutPayment(codeVo.getQrCode(), price);
        } catch (Exception e) {
            SoundUtils.getInstance().queueSound(new VoiceTemplate()
                    .prefix("fail_code")
                    .gen());
            DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(errTwoCode));
            doSaoma();
            return;
        }
    }


    public void doSaoma() {
        //------------新版扫码接口---------------------//
//        ScanInitEntry scanInitEntry = new ScanInitEntry();
//        scanInitEntry.setCameraId(0x00);
//        scanInitEntry.setContext(this);
//        scanInitEntry.setSurfaceView(surfaceView);
//        scanInitEntry.setTimeOut(3600);
//        scanInitEntry.setTimeOutUnit(TimeUnit.SECONDS);
//        scanInitEntry.setOnce(true);
//        scannerModule.initAndStartScan(scanInitEntry, new ScanListener() {
//            @Override
//            public void onResponse(ScanResultEntry scanResultEntry) {
//                Log.i("扫码结果：", new String[]{new String(scanResultEntry.getScanResult()[0])}[0]);
////                    doSaoma(barcodes[0]);
////					logger.debug("---------------onResponse---------"+scanResultEntry.getScanResult().length);
////
////					logger.debug("---------------onResponse---------"+new String(scanResultEntry.getScanResult()[0]));
////					logger.debug("---------------onResponse---------"+ ISOUtils.hexString(scanResultEntry.getScanResult()[0]));
////
////					logger.debug("---------------onResponse---------"+new String(scanResultEntry.getScanResult()[scanResultEntry.getScanResult().length-1]));
////					isFinish = true;
////
////					spi.play();
////					Message scanMsg = new Message();
////					scanMsg.what = AppConfig.ScanResult.SCAN_RESPONSE;
////					Bundle scanBundle = new Bundle();
////					scanBundle.putStringArray("barcodes", new String[]{new String(scanResultEntry.getScanResult()[0])});
////					scanMsg.setData(scanBundle);
////					ScannerFragment.getScanEventHandler().sendMessage(scanMsg);
//
//            }
//
//            @Override
//            public void onTimeout() {
//                if (!isFinishing())
//                    doSaoma();
//            }
//
//            @Override
//            public void onFinish() {
//                finish();
//            }
//        });
        try {
//            scannerModule.initAndStartScan();
            scannerModule.startScan(3600, TimeUnit.SECONDS, new ScannerListener() {
                @Override
                public void onResponse(String[] barcodes) {
                    Log.i("扫码结果：", barcodes[0]);
                    doSaoma(barcodes[0]);
//                    if (type == 0) {
//                        Gson gson = new Gson();
//                        try {
////                            if (strings.startsWith("dw00") && strings.length() == 21) {
////                                sendCutPayment(1, strings.substring(4), AppConstants.terminalCode, price);
////                                return;
////                            }
//                            CodeVo codeVo = gson.fromJson(strings, CodeVo.class);
//                            if (codeVo.getType() != CodeVo.TYPE_PAYMENT) {
//                                String msg = "二维码无效";
//                                Intent intent = new Intent(SanMaActivity.this, PayErrorActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putDouble("price", price);
//                                bundle.putString("reason", msg);
//                                bundle.putString("type", "pay");
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        doSaoma();
//                                    }
//                                }, 1500);
//                                return;
//                            }
//                            sendCutPayment(1, codeVo.getQrCode(), AppConstants.terminalCode, price);
//                        } catch (Exception e) {
//                            String msg = "二维码无效";
//                            Intent intent = new Intent(SanMaActivity.this, PayErrorActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putDouble("price", price);
//                            bundle.putString("reason", msg);
//                            bundle.putString("type", "pay");
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    doSaoma();
//                                }
//                            }, 1500);
//                            return;
//                        }
//                    } else {
//                        Intent intent0 = getIntent();
//                        intent0.putExtra("SaoMa", strings);
//                        setResult(1, intent0);
//                        finish();
//                    }
                }

                @Override
                public void onFinish() {
                    if (!isFinishing())
                        doSaoma();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            doSaoma();
        }

    }

    public void sendCutPayment(String qrcode, final double price) {
        HttpUtils.sendCutPayment(qrcode, price, new CallBackListener() {
            @Override
            public void onSuccess(ResponseVo responseVo) {
                SoundUtils.getInstance().queueSound(new VoiceTemplate()
                        .prefix("success_nonumber")
                        .gen());
                DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(successCode));
                doSaoma();
            }

            @Override
            public void onFail(ResponseVo responseVo) {
                String reason = responseVo.getMsg();
                if (TextUtils.isEmpty(reason)) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("fail")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(failCode));
                } else if (reason.equals("二维码无效")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("fail_code")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(errTwoCode));
                } else if (reason.equals("余额不足")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("loss_pay")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(lossPayCode));
                } else if (reason.equals("不在消费时段")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("out_paytime")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(outPayCode));
                } else if (reason.equals("不可在该时段重复消费")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("repeat_pay")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(doublePayCode));
                } else if (responseVo.getCode() == 500807) {
                    ToastUtils.show(responseVo.getMsg());
                } else {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("fail")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(failCode));
                }
                ToastUtils.show(responseVo.getMsg());
                doSaoma();
            }
        });
    }

//    public void sendCutPayment(long operatorId, String qrcode,
//                               String posSn, final double price) {
//        GsonBuilder builder = new GsonBuilder();
//        Map<String, Object> data = new HashMap<>();
//        data.put("token", AppConstants.token);
//        data.put("userId", operatorId);
//        data.put("twoDimensional", qrcode);
//        data.put("deviceSn", posSn);
//        data.put("amount", price);
//        String json = builder.create().toJson(data);
//        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//        OkHttpClient client = AppUtils.createPosOkHttpClient();
//        RequestBody body = RequestBody.create(mediaType, json);
//        String reqUrl = AppRegistry.getHttpUrl(AppRegistry.WCMD_CUT_PAYMENT, "dw-cashier-boss");
//        Log.e("yantao", "url:" + reqUrl + "!");
//        final Request request = new Request.Builder()
//                .url(reqUrl)
//                .addHeader("Authorization", AppConstants.token)
//                .post(body)
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Intent intent = new Intent(SanMaActivity.this, PayErrorActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putDouble("price", price);
//                bundle.putString("reason", "网络异常");
//                bundle.putString("type", "pay");
//                intent.putExtras(bundle);
//                startActivity(intent);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        doSaoma();
//                    }
//                }, 1500);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String rawResp = "";
//                try {
//                    rawResp = response.body().string();
//                    Log.i("Nothing_msg", rawResp);
//                    JSONObject jo = new JSONObject(rawResp);
//                    int code = jo.getInt("code");
//                    if (0 == code) {
//                        Intent intent = new Intent(SanMaActivity.this, PaySuccessAcivity.class);
//                        Bundle params = new Bundle();
//                        params.putDouble("price", price);
//                        params.putString("type", "pay");
//                        intent.putExtras(params);
//                        startActivity(intent);
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                doSaoma();
//                            }
//                        }, 1500);
//
////                        finish();
//                    } else {
//                        if (code == AppConstants.OUT_CODE) {
//                            App.getInstance().outLogin();
//                        } else {
//                            String msg = "";
//                            if (jo.has("msg")) {
//                                msg = jo.getString("msg");
//                            } else {
//                                msg = "服务器异常";
//                            }
//                            Intent intent = new Intent(SanMaActivity.this, PayErrorActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putDouble("price", price);
//                            bundle.putString("reason", msg);
//                            bundle.putString("type", "pay");
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    doSaoma();
//                                }
//                            }, 1500);
////                            finish();
//                        }
//                    }
//                } catch (IOException e) {
//                } catch (JSONException e) {
//                }
//                Log.e("yantao", "支付成功：" + rawResp + "!");
//            }
//        });
//    }

}
