package com.example.aes_pos_wallet.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.aes_pos_wallet.AESApplication;
import com.example.aes_pos_wallet.view.ToastView;


public class ToastUtils {
    public static void show(String text) {
        if (TextUtils.isEmpty(text)) return;
        if (ExecutorsUtils.isMainThread()) {
            ToastView.showToast(text, Toast.LENGTH_SHORT);
//            Toast.makeText(AESApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
        } else {
            AESApplication.getInstance().runOnUI(new Runnable() {
                @Override
                public void run() {
                    ToastView.showToast(text, Toast.LENGTH_SHORT);
//                    Toast.makeText(AESApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void show(Spanned text) {
        if (TextUtils.isEmpty(text)) return;
        if (ExecutorsUtils.isMainThread()) {
            ToastView.showToast(text, Toast.LENGTH_SHORT);
//            Toast.makeText(AESApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
        } else {
            AESApplication.getInstance().runOnUI(new Runnable() {
                @Override
                public void run() {
                    ToastView.showToast(text, Toast.LENGTH_SHORT);
//                    Toast.makeText(AESApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public static void showLong(String text) {
        if (ExecutorsUtils.isMainThread()) {
            ToastView.showToast(text, Toast.LENGTH_SHORT);
//            Toast.makeText(AESApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
        } else {
            AESApplication.getInstance().runOnUI(new Runnable() {
                @Override
                public void run() {
                    ToastView.showToast(text, Toast.LENGTH_SHORT);
//                    Toast.makeText(AESApplication.getInstance(), text, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public static void showMessage(final String mess, final int messageType) {
        Log.e("Nothing", mess);
        String message = "";
        switch (messageType) {
            case MessageTag.NORMAL:
                message = "<font color='white'>" + mess + "</font>";
                break;
            case MessageTag.ERROR:
                message = "<font color='red'>" + mess + "</font>";
                break;
            case MessageTag.TIP:
                message = "<font color='green'>" + mess + "</font>";
                break;
            case MessageTag.DATA:
                message = "<font color='blue'>" + mess + "</font>";
                break;
            case MessageTag.WARN:
                message = "<u><font color='red'>" + mess + "</font></u>";
                break;
            default:
                break;
        }
        show(Html.fromHtml(message));
    }
}
