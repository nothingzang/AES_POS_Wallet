package com.example.aes_pos_wallet.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.newland.SettingsManager;
import android.newland.content.NlContext;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aes_pos_wallet.R;
import com.example.aes_pos_wallet.constants.Constants;
import com.example.aes_pos_wallet.device.S_DeviceInfo;
import com.example.aes_pos_wallet.listeners.CallBackListener;
import com.example.aes_pos_wallet.response.ResponseVo;
import com.example.aes_pos_wallet.utils.BytesUtils;
import com.example.aes_pos_wallet.utils.DataForwardController;
import com.example.aes_pos_wallet.utils.HexUtil;
import com.example.aes_pos_wallet.utils.HttpUtils;
import com.example.aes_pos_wallet.utils.MessageTag;
import com.example.aes_pos_wallet.utils.SoundPoolImpl;
import com.example.aes_pos_wallet.utils.SoundUtils;
import com.example.aes_pos_wallet.utils.ToastUtils;
import com.example.aes_pos_wallet.vo.CodeVo;
import com.example.aes_pos_wallet.vo.VoiceTemplate;
import com.example.aes_pos_wallet.vo.unionVo.UnionCodeValue;
import com.example.aes_pos_wallet.vo.unionVo.UnionVoCode;
import com.google.gson.Gson;
import com.newland.mtype.ModuleType;
import com.newland.mtype.ProcessTimeoutException;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.log.DeviceLogger;
import com.newland.mtype.log.DeviceLoggerFactory;
import com.newland.mtype.module.common.cardreader.CommonCardType;
import com.newland.mtype.module.common.cardreader.K21CardReaderEvent;
import com.newland.mtype.module.common.cardreader.OpenCardReaderResult;
import com.newland.mtype.module.common.cardreader.SearchCardRule;
import com.newland.mtype.module.common.rfcard.RFCardType;
import com.newland.mtype.module.common.rfcard.RFResult;
import com.newland.mtype.module.common.scanner.ScanLightType;
import com.newland.mtype.module.common.scanner.ScannerListener;
import com.newland.mtype.util.ISOUtils;

import java.time.chrono.IsoEra;
import java.util.concurrent.TimeUnit;

public class ScanViewActivity extends Activity {

    private SurfaceView surfaceView;
    private Context context;
    private int scanType;
    private int timeout;
    private static DeviceLogger logger = DeviceLoggerFactory.getLogger(ScanViewActivity.class);
    private ImageView scanIV;
    private RelativeLayout frontLL;
    private LinearLayout switch_fr;
    private LinearLayout switch_bc;
    private boolean isFinish = false;
    private boolean isTimeout = true;
    private AnimationDrawable scanAnim;
    private FrameLayout backFL;
    private static final int Code_PERMISSION = 100;
    private TextView picTv, posTv;
    private SettingsManager settingManager;
    double price;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        View view = View.inflate(this, R.layout.sacn_view, null);
        setContentView(view);
        init();
        try {
            settingManager = (SettingsManager) getSystemService(NlContext.SETTINGS_MANAGER_SERVICE);
            settingManager.setAppSwitchKeyEnabled(false);
            settingManager.setHomeKeyEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler scanHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    startScan();
                    break;
                }
                default:
                    break;
            }
        }

    };

    private void init() {
        scanType = getIntent().getIntExtra("scanType", 0x00);//Front default
        price = getIntent().getDoubleExtra("price", 0);
//		timeout=getIntent().getIntExtra("timeout", 60);
        timeout = 60;
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        frontLL = (RelativeLayout) findViewById(R.id.ll_front);
        switch_fr = (LinearLayout) findViewById(R.id.ll_switch_front);
        switch_bc = (LinearLayout) findViewById(R.id.ll_switch_back);
        backFL = (FrameLayout) findViewById(R.id.fl_back);
        scanIV = (ImageView) findViewById(R.id.iv_scan);

        picTv = (TextView) findViewById(R.id.text_pic);
        posTv = (TextView) findViewById(R.id.text_pos);
        switch_fr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("---------------切换前置---------");
                switch_bc.setEnabled(true);
                switch_fr.setEnabled(false);
                isFinish = false;
                isTimeout = false;
                S_DeviceInfo.getInstance().getScanner().operLight(ScanLightType.LED_LIGHT, 0);
                S_DeviceInfo.getInstance().getScanner().operLight(ScanLightType.RED_LIGHT, 0);
                S_DeviceInfo.getInstance().getScanner().operLight(ScanLightType.FLASH_LIGHT, 0);
                S_DeviceInfo.getInstance().getScanner().stopScan();
                scanType = Constants.ScanType.FRONT;
            }
        });

        switch_bc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                logger.debug("---------------切换后置---------");
                switch_bc.setEnabled(false);
                switch_fr.setEnabled(true);
                isFinish = false;
                isTimeout = false;
                S_DeviceInfo.getInstance().getScanner().operLight(ScanLightType.LED_LIGHT, 0);
                S_DeviceInfo.getInstance().getScanner().operLight(ScanLightType.RED_LIGHT, 0);
                S_DeviceInfo.getInstance().getScanner().operLight(ScanLightType.FLASH_LIGHT, 0);
                S_DeviceInfo.getInstance().getScanner().stopScan();
                scanType = Constants.ScanType.BACK;
            }
        });

        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, Code_PERMISSION);
            } else {
                startScan();
            }
        } else {
            startScan();
        }
        doCard();
    }

    private void startScan() {
        try {
            isFinish = false;
            isTimeout = true;
            if (scanType == Constants.ScanType.BACK) {//后置的
                frontLL.setVisibility(View.GONE);
                backFL.setVisibility(View.VISIBLE);
                boolean resutl = S_DeviceInfo.getInstance().getScanner().isSupScanCode(1);
                switch_fr.setVisibility(View.GONE);
                if (resutl) {
                    switch_fr.setVisibility(View.VISIBLE);
                }
                S_DeviceInfo.getInstance().getScanner().initScanner(context, surfaceView, scanType);

            } else if (scanType == Constants.ScanType.FRONT) {
                backFL.setVisibility(View.GONE);
                frontLL.setVisibility(View.VISIBLE);
                scanAnim = (AnimationDrawable) scanIV.getDrawable();
                if (scanAnim != null && !scanAnim.isRunning()) {
                    scanAnim.start();
                }
                boolean resutl = S_DeviceInfo.getInstance().getScanner().isSupScanCode(0);
                switch_bc.setVisibility(View.GONE);
                if (resutl) {
                    switch_bc.setVisibility(View.VISIBLE);
                }
                S_DeviceInfo.getInstance().getScanner().initScanner(context, null, scanType);
            } else {
                finish();
            }
            S_DeviceInfo.getInstance().getScanner().startScan(timeout, TimeUnit.SECONDS, new ScannerListener() {
                @Override
                public void onResponse(String[] barcodes) {
                    logger.debug("---------------onResponse---------" + barcodes[0]);
                    doSaoma(barcodes[0]);
                    isTimeout = false;
                    isFinish = false;
                    SoundPoolImpl.getInstance().play();
//                    Message scanMsg = new Message();
//                    scanMsg.what = 1;
//                    scanHandler.sendMessage(scanMsg);
                }

                @Override
                public void onFinish() {
                    logger.debug("---------------onFinish---------" + isFinish + isTimeout);
                    if (isTimeout) {
                        isFinish = true;
                        finish();
                    } else if (!isTimeout && !isFinish) {
                        Message scanMsg = new Message();
                        scanMsg.what = 1;
                        scanHandler.sendMessage(scanMsg);
                    } else {
                        finish();
                        logger.debug("---------------onFinish---------");
                    }
                }
            }, true);
        } catch (Exception e) {
            e.printStackTrace();
            isFinish = true;
            logger.debug("---------------Exception---------" + e.getMessage());
            finish();
            e.getStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        logger.debug("------onPause--------" + isTimeout + "isFinish：" + isFinish);
        S_DeviceInfo.getInstance().getCardRead().cancelCardRead();
        if (isTimeout && !isFinish) {
            //取消扫码
//			Message scanMsg = new Message();
//			scanMsg.what = Constants.ScanResult.SCAN_CANCEL;
//			ScannerFragment.getScanEventHandler().sendMessage(scanMsg);
        }
        isTimeout = false;
        isFinish = true;
//
//		S_DeviceInfo.getInstance().getScanner().operLight(ScanLightType.LED_LIGHT,0);
//		S_DeviceInfo.getInstance().getScanner().operLight(ScanLightType.RED_LIGHT,0);
//		S_DeviceInfo.getInstance().getScanner().operLight(ScanLightType.FLASH_LIGHT,0);
        S_DeviceInfo.getInstance().getScanner().stopScan();

        if (scanAnim != null && scanAnim.isRunning()) {
            scanAnim.stop();
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        logger.debug("---------------keyCode---------" + keyCode);//700S设备 左边是24 右边是25
        logger.debug("---------------event---------" + event);
//		if((keyCode==KeyEvent.KEYCODE_VOLUME_UP&& event.getRepeatCount() == 0)){
//			logger.debug("发起700扫码");
//			startScan();
//		}else if((keyCode==KeyEvent.KEYCODE_VOLUME_DOWN&& event.getRepeatCount() == 0)){
//			logger.debug("发起700扫码");
//			startScan();
//		}

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            logger.debug("回退键");
            isTimeout = true;
            isFinish = false;
            finish();
        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        logger.debug("---------------onDestroy---------");
        try {
            S_DeviceInfo.getInstance().getCardRead().cancelCardRead();
            settingManager.setAppSwitchKeyEnabled(true);
            settingManager.setHomeKeyEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            if (requestCode == Code_PERMISSION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限被用户同意,做相应的事情
                    startScan();
                } else {
                    //权限被用户拒绝，做相应的事情
//					Message scanMsg = new Message();
//					scanMsg.what = AppConfig.ScanResult.SCAN_ERROR;
//					Bundle scanBundle = new Bundle();
//					scanBundle.putInt("errorCode", 0);
//					scanBundle.putString("errormessage","摄像头动态授权失败");
//					scanMsg.setData(scanBundle);
//					ScannerFragment.getScanEventHandler().sendMessage(scanMsg);
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
                DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(Constants.errTwoCode));
                return;
            }
            sendCutPayment(codeVo.getQrCode(), price);
        } catch (Exception e) {
            SoundUtils.getInstance().queueSound(new VoiceTemplate()
                    .prefix("fail_code")
                    .gen());
            DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(Constants.errTwoCode));
            return;
        }
    }


    public void sendCutPayment(String qrcode, final double price) {
        HttpUtils.sendCutPayment(qrcode, price, new CallBackListener() {
            @Override
            public void onSuccess(ResponseVo responseVo) {
                SoundUtils.getInstance().queueSound(new VoiceTemplate()
                        .prefix("success_nonumber")
                        .gen());
                DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(Constants.successCode));
            }

            @Override
            public void onFail(ResponseVo responseVo) {
                String reason = responseVo.getMsg();
                if (TextUtils.isEmpty(reason)) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("fail")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(Constants.failCode));
                } else if (reason.equals("二维码无效")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("fail_code")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(Constants.errTwoCode));
                } else if (reason.equals("余额不足")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("loss_pay")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(Constants.lossPayCode));
                } else if (reason.equals("不在消费时段")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("out_paytime")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(Constants.outPayCode));
                } else if (reason.equals("不可在该时段重复消费")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("repeat_pay")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(Constants.doublePayCode));
                } else if (responseVo.getCode() == 500807) {
                    ToastUtils.show(responseVo.getMsg());
                } else {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("fail")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(Constants.failCode));
                }
                ToastUtils.show(responseVo.getMsg());
            }
        });
    }

    private void doCard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) return;
                String showMsg = TestActivity.doRead();
                SoundPoolImpl.getInstance().play();
                if (!TextUtils.isEmpty(showMsg) && showMsg.length() > 18) {
                    if (showMsg.startsWith("0")) {
                        showMsg = showMsg.substring(1);
                    }
                    if (showMsg.length() > 17) {
                        String code = showMsg.substring(0, 17);
                        doSaoma("dw00" + code);
                    }
                }
                ToastUtils.showMessage(showMsg + "\r\n", MessageTag.NORMAL);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    doCard();
                }
            }
        }).start();

    }
}
