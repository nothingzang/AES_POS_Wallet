package com.example.aes_pos_wallet.listeners;

import android.text.TextUtils;


import com.example.aes_pos_wallet.AESApplication;
import com.example.aes_pos_wallet.constants.HttpConstants;
import com.example.aes_pos_wallet.response.ResponseVo;
import com.example.aes_pos_wallet.utils.GsonUtils;

import java.lang.reflect.Type;

/**
 * Created by Nothing on 2019/7/30.
 */

public abstract class CallBackListener {
    public abstract void onSuccess(ResponseVo responseVo);

    public abstract void onFail(ResponseVo responseVo);

    public void onEnd(String responseString, Type responseVoClass) {
        if (TextUtils.isEmpty(responseString)) {
            onFail(ResponseVo.getNullError());
            return;
        }
//         responseString = responseString.replaceAll("\\\\","");
        ResponseVo responseVo = null;
        try {
            responseVo = GsonUtils.get().fromJson(responseString, responseVoClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (responseVo == null) {
            onFail(ResponseVo.getNullError());
            return;
        }
        if (responseVo.getCode() == HttpConstants.SUCCESS_CODE) {
            ResponseVo finalResponseVo = responseVo;
            AESApplication.getInstance().runOnUI(new Runnable() {
                @Override
                public void run() {
                    onSuccess(finalResponseVo);
                }
            });
            return;
        }
        if (responseVo.getCode() == HttpConstants.TOKEN_OUTTIME_CODE) {
            AESApplication.getInstance().loginOut("登录过期，请重新登录!");
            return;
        }
        onFail(responseVo);
    }

    public void onStar(Object... obj) {
    }


}
