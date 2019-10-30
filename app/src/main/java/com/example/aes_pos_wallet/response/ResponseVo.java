package com.example.aes_pos_wallet.response;


import com.example.aes_pos_wallet.constants.HttpConstants;

/**
 * Created by Nothing on 2019/7/29.
 */

public class ResponseVo<T> {
    private int code = -1;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static ResponseVo getNetError(String message) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode(HttpConstants.NET_FIAL_CODE);
//        responseVo.setMsg(message);
        responseVo.setMsg("网络异常");
        return responseVo;
    }

    public static ResponseVo getNullError() {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode(HttpConstants.NULL_FIAL_CODE);
        responseVo.setMsg("服务器异常");
        return responseVo;
    }
}
