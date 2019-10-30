package com.example.aes_pos_wallet.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.aes_pos_wallet.listeners.PermissionCallBack;
import com.example.aes_pos_wallet.utils.ActivityManager;
import com.example.aes_pos_wallet.utils.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.DecimalFormat;

import butterknife.ButterKnife;


/**
 * Created by Nothing on 2019/7/29.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private boolean isShowLoading = false;

    abstract int getLayoutId();

    abstract void initDate();

    abstract void initView();

    abstract void bindListener();

    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(getLayoutId(), null);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(view);
        ActivityManager.addActivity(this, getClass());
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        init();
    }

    public void post(Runnable runnable) {
        view.post(runnable);
    }

    public void post(Runnable runnable, long delay) {
        view.postDelayed(runnable, delay);
    }

    public void init() {
        setTopColor();
        initDate();
        initView();
        bindListener();
        initSystemTop();
    }


    private void initSystemTop() {
        if (!isShowSystemTop && Build.VERSION.SDK_INT >= 19) {
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    private boolean isShowTop = true;
    private boolean isDark = true;
    private boolean isShowSystemTop = true;

    public void setShowSystemTop(boolean isShowSystemTop) {
        this.isShowSystemTop = isShowSystemTop;
    }

    public void setShowTop(boolean isShowTop) {
        this.isShowTop = isShowTop;
    }

    public void setDark(boolean isDark) {
        this.isDark = isDark;
    }

    private void setTopColor() {
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, isShowTop);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        if (isDark && !StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
    }

    public DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            ActivityManager.removeActivity(this);
        }
    }


    abstract void back();


    /**
     * 显示键盘
     *
     * @param view
     */
    public static void showKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                view.requestFocus();
                imm.showSoftInput(view, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void requestPower(PermissionCallBack permissionCallBack, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        //申请多个权限，获取合并后的详细信息
        rxPermissions.requestEachCombined(permissions)
                .subscribe(permission -> {
                    if (permission.granted) {
                        //获得权限成功
                        permissionCallBack.accecpt();
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //获取权限失败，但是用户没有勾选”不再询问“，在这里应该弹出对话框向用户解释为何需要该权限
                        permissionCallBack.unAccecpt(false);
                    } else {
                        //权限申请失败，用户勾选了“不再询问”，在这里应该引导用户去设置页面打开权限
                        permissionCallBack.unAccecpt(true);
                    }
                });
    }

    private long mLastClickTime;
    private long timeInterval = 1000;

    @CallSuper
    public void onClick(View view) throws DoubleClickExcetion {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            mLastClickTime = nowTime;
        } else {
            throw new DoubleClickExcetion();
        }
    }

    class DoubleClickExcetion extends Exception {

    }
}
