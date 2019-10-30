package com.example.aes_pos_wallet.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.aes_pos_wallet.AESApplication;
import com.example.aes_pos_wallet.constants.Constants;


public class SpUtils {


    private static SpUtils spUtils;

    private SpUtils() {

    }

    public synchronized static SpUtils get() {
        if (spUtils == null) spUtils = new SpUtils();
        return spUtils;
    }

    public String get(String key, String defaultValue) {
        return getSP().getString(key, defaultValue);
    }

    public float get(String key, float defaultValue) {
        return getSP().getFloat(key, defaultValue);
    }

    public int get(String key, int defaultValue) {
        return getSP().getInt(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return getSP().getLong(key, defaultValue);
    }

    public boolean get(String key, boolean defaultValue) {
        return getSP().getBoolean(key, defaultValue);
    }


    public void save(String key, String value) {
        getSP().edit().putString(key, value).commit();
    }

    public void save(String key, float value) {
        getSP().edit().putFloat(key, value).commit();
    }

    public void save(String key, boolean value) {
        getSP().edit().putBoolean(key, value).commit();
    }

    public void save(String key, int value) {
        getSP().edit().putInt(key, value).commit();
    }

    public void save(String key, long value) {
        getSP().edit().putLong(key, value).commit();
    }


    private SharedPreferences sharedPreferences;

    private SharedPreferences getSP() {
        if (sharedPreferences == null)
            sharedPreferences = AESApplication.getInstance().getSharedPreferences(Constants.SP_Address, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

}
