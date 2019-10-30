package com.example.aes_pos_wallet.view;

import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aes_pos_wallet.AESApplication;
import com.example.aes_pos_wallet.R;


public class ToastView extends Toast {


    private ToastView() {
        super(AESApplication.getInstance());
    }


    private static ToastView initToast(String infoTx) {
        ToastView toastView = new ToastView();
        View view = LayoutInflater.from(AESApplication.getInstance()).inflate(R.layout.view_toast, null);
        toastView.setView(view);
        toastView.getView().bringToFront();
        TextView info = view.findViewById(R.id.tv_info);
        toastView.setGravity(Gravity.CENTER, 0, 200);
        if (!TextUtils.isEmpty(infoTx) && info != null)
            info.setText(infoTx);
        return toastView;
    }
    private static ToastView initToast(Spanned infoTx) {
        ToastView toastView = new ToastView();
        View view = LayoutInflater.from(AESApplication.getInstance()).inflate(R.layout.view_toast, null);
        toastView.setView(view);
        toastView.getView().bringToFront();
        TextView info = view.findViewById(R.id.tv_info);
        toastView.setGravity(Gravity.CENTER, 0, 200);
        if (!TextUtils.isEmpty(infoTx) && info != null)
            info.setText(infoTx);
        return toastView;
    }

    /**
     * 图标状态 不显示图标
     */
    private static final int TYPE_HIDE = -1;
    /**
     * 图标状态 显示√
     */
    private static final int TYPE_TRUE = 0;
    /**
     * 图标状态 显示×
     */
    private static final int TYPE_FALSE = 1;


    /**
     * 显示Toast
     *
     * @param text    显示的文本
     * @param time    显示时长
     * @param imgType 图标状态
     */
//    public static void showToast(String text, int time, int imgType) {
    public static void showToast(String text, int time) {
        // 初始化一个新的Toast对象
        ToastView toastView = initToast(text);

        // 设置显示时长
        if (time == Toast.LENGTH_LONG) {
            toastView.setDuration(Toast.LENGTH_LONG);
        } else {
            toastView.setDuration(Toast.LENGTH_SHORT);
        }

//        // 判断图标是否该显示，显示√还是×
//        if (imgType == TYPE_HIDE) {
//            toast_img.setVisibility(View.GONE);
//        } else {
//            if (imgType == TYPE_TRUE) {
//                toast_img.setBackgroundResource(R.drawable.toast_y);
//            } else {
//                toast_img.setBackgroundResource(R.drawable.toast_n);
//            }
//            toast_img.setVisibility(View.VISIBLE);
//
//            // 动画
//            ObjectAnimator.ofFloat(toast_img, "rotationY", 0, 360).setDuration(1700).start();
//        }

        // 显示Toast
        toastView.show();
    }

    public static void showToast(Spanned text, int time) {
        // 初始化一个新的Toast对象
        ToastView toastView = initToast(text);

        // 设置显示时长
        if (time == Toast.LENGTH_LONG) {
            toastView.setDuration(Toast.LENGTH_LONG);
        } else {
            toastView.setDuration(Toast.LENGTH_SHORT);
        }

//        // 判断图标是否该显示，显示√还是×
//        if (imgType == TYPE_HIDE) {
//            toast_img.setVisibility(View.GONE);
//        } else {
//            if (imgType == TYPE_TRUE) {
//                toast_img.setBackgroundResource(R.drawable.toast_y);
//            } else {
//                toast_img.setBackgroundResource(R.drawable.toast_n);
//            }
//            toast_img.setVisibility(View.VISIBLE);
//
//            // 动画
//            ObjectAnimator.ofFloat(toast_img, "rotationY", 0, 360).setDuration(1700).start();
//        }

        // 显示Toast
        toastView.show();
    }

}
