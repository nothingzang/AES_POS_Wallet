package com.example.aes_pos_wallet.utils;

import com.google.gson.Gson;

/**
 * Created by Nothing on 2019/7/30.
 */

public class GsonUtils {
    public static Gson gson;
    public synchronized static Gson get(){
        if(gson==null)gson=new Gson();
        return gson;
    }
}
