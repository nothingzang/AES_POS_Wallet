package com.example.aes_pos_wallet;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.example.aes_pos_wallet.activitys.LoginActivity;
import com.example.aes_pos_wallet.constants.Constants;
import com.example.aes_pos_wallet.constants.HttpConstants;
import com.example.aes_pos_wallet.device.S_DeviceInfo;
import com.example.aes_pos_wallet.utils.ActivityManager;
import com.example.aes_pos_wallet.utils.ExecutorsUtils;
import com.example.aes_pos_wallet.utils.SoundPoolImpl;
import com.example.aes_pos_wallet.utils.SoundUtils;
import com.example.aes_pos_wallet.utils.ToastUtils;


/**
 * Created by Nothing on 2019/7/29.
 */

public class AESApplication extends Application {
    private static AESApplication aesApplication;

    public static AESApplication getInstance() {
        return aesApplication;
    }

    private Handler handler;


    @Override
    public void onCreate() {
        super.onCreate();
        aesApplication = this;
        handler = new Handler(Looper.getMainLooper());//初始化handler
        Constants.init();
        SoundUtils.getInstance().load(this);
        SoundPoolImpl.getInstance().initLoad();
        HttpConstants.init();
        if (!S_DeviceInfo.getInstance().isConnectDevice())
            S_DeviceInfo.getInstance().connectDevice();
        ExecutorsUtils.setThreadPoolRunning(true);
//        createFloatView();
    }

    public void loginOut(String info) {
        Constants.updateToken("");
        ActivityManager.removeAllActivity();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        if (!TextUtils.isEmpty(info))
            ToastUtils.show("" + info);
    }

    public void runOnUI(Runnable runnable) {
        if (handler == null) return;
        handler.post(runnable);
    }

    public void runOnUIDelay(Runnable runnable, long time) {
        handler.postDelayed(runnable, time);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    //判断当前应用是否是debug状态
    public static boolean isApkInDebug() {
        try {
            ApplicationInfo info = AESApplication.getInstance().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

}

