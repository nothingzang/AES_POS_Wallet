package com.example.aes_pos_wallet.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


import com.example.aes_pos_wallet.AESApplication;
import com.example.aes_pos_wallet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * android原生音效
 *
 * @author Nothing
 */
public class SoundUtils {

    private static SoundUtils INSTANCE;

    private SoundPool mSoundPool;

    private HashMap<Integer, Integer> mSoundId;
    private List<Character> numberList = new ArrayList<>();

    private HashMap<String, Integer> mSoundNumberId;

    private SoundUtils() {
    }

    public static SoundUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SoundUtils();
        }
        return INSTANCE;
    }

    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("deprecation")
    public void load(Context context) {
        mSoundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 5);
        mSoundNumberId = new HashMap<String, Integer>();
        mSoundId = new HashMap<Integer, Integer>();
        mSoundNumberId.put("0", mSoundPool.load(context, R.raw._0, 1));
        mSoundNumberId.put("1", mSoundPool.load(context, R.raw._1, 1));
        mSoundNumberId.put("2", mSoundPool.load(context, R.raw._2, 1));
        mSoundNumberId.put("3", mSoundPool.load(context, R.raw._3, 1));
        mSoundNumberId.put("4", mSoundPool.load(context, R.raw._4, 1));
        mSoundNumberId.put("5", mSoundPool.load(context, R.raw._5, 1));
        mSoundNumberId.put("6", mSoundPool.load(context, R.raw._6, 1));
        mSoundNumberId.put("7", mSoundPool.load(context, R.raw._7, 1));
        mSoundNumberId.put("8", mSoundPool.load(context, R.raw._8, 1));
        mSoundNumberId.put("9", mSoundPool.load(context, R.raw._9, 1));
        mSoundNumberId.put("dot", mSoundPool.load(context, R.raw.dot, 1));
        mSoundNumberId.put("hundred", mSoundPool.load(context, R.raw.hundred, 1));
        mSoundNumberId.put("success", mSoundPool.load(context, R.raw.success, 1));
        mSoundNumberId.put("success_nonumber", mSoundPool.load(context, R.raw.success_nonumber, 1));
        mSoundNumberId.put("fail", mSoundPool.load(context, R.raw.transfail, 1));
        mSoundNumberId.put("fail_code", mSoundPool.load(context, R.raw.fail_code, 1));
        mSoundNumberId.put("ten", mSoundPool.load(context, R.raw.ten, 1));
        mSoundNumberId.put("ten_million", mSoundPool.load(context, R.raw.ten_million, 1));
        mSoundNumberId.put("ten_thousand", mSoundPool.load(context, R.raw.ten_thousand, 1));
        mSoundNumberId.put("thousand", mSoundPool.load(context, R.raw.thousand, 1));
        mSoundNumberId.put("yuan", mSoundPool.load(context, R.raw.yuan, 1));
        mSoundNumberId.put("back_success", mSoundPool.load(context, R.raw.back_success, 1));
        mSoundNumberId.put("loss_pay", mSoundPool.load(context, R.raw.loss_pay, 1));
        mSoundNumberId.put("repeat_pay", mSoundPool.load(context, R.raw.repeat_pay, 1));
        mSoundNumberId.put("out_paytime", mSoundPool.load(context, R.raw.out_paytime, 1));
    }

    int i = 0;

    long time = 300;

    public void queueSound(final List<String> numbers) {
        if (numbers == null || numbers.size() == 0) return;
        AESApplication.getInstance().runOnUIDelay(new Runnable() {
            @Override
            public void run() {
                if (mSoundPool == null) return;
                String numberChar = numbers.get(0);
                if (numberChar.equals("success")) time = 2000;
                else time = 400;
                if (mSoundPool.play(mSoundNumberId.get(numberChar), 1, 1, 0, 0, 1) == 0) {
//                        Log.e("soundPool", "Failed to start sound (" + sid + ")");
                }
                numbers.remove(0);
                queueSound(numbers);
            }
        }, time);
    }

}
