package com.example.aes_pos_wallet.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.aes_pos_wallet.AESApplication;
import com.example.aes_pos_wallet.R;


/**
 * android SoundPool
 * @author chenkh
 * @date 2015-7-29
 * @time 上午9:24:18
 *
 */
public class SoundPoolImpl {

	private static SoundPoolImpl INSTANCE;
	
	private SoundPool soundPool;
	
	
	public static SoundPoolImpl getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new SoundPoolImpl();
		}
		return INSTANCE;
	}
	
	public void initLoad(){
		soundPool= new SoundPool(3, AudioManager.STREAM_SYSTEM,5);
		soundPool.load(AESApplication.getInstance(),
				R.raw.click1, 1);
	}
	
	public void play(){
		soundPool.play(1,1, 1, 0, 0, 1);
	}
	
	public void release(){
		soundPool.release();
	}
	
}
