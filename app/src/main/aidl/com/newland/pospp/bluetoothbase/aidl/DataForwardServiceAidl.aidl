package com.newland.pospp.bluetoothbase.aidl;

import com.newland.pospp.bluetoothbase.aidl.OnDataReceiveListener;

import com.newland.pospp.bluetoothbase.aidl.OnStatusListener;

import com.newland.pospp.bluetoothbase.aidl.OnPinpadListener;


interface DataForwardServiceAidl {
	//0未知 1.蓝牙关闭 2.蓝牙正在关闭 3.蓝牙正在打开 4.空闲 5.搜索设备 6.正在连接设备 7.已连接设备 8.等待连接设备
	int getStatus();
	
	boolean send(in byte[] data);
	
	void setIsCheckDataLenth(boolean value); //是否校验底座数据长度
	void setIsPassthrough(boolean value);  //是否AIDL透传
	boolean getIsCheckDataLenth(); //是否校验底座数据长度
	boolean getIsPassthrough();  //是否AIDL透传
	void registerDataReceiveListener(in OnDataReceiveListener listener);
	void registerPinpadListener(in OnPinpadListener listener);
	
	void unregisterPinpadListener(in OnPinpadListener listener);
	
	void unregisterDataReceiveListener(in OnDataReceiveListener listener);
	
	/**
	 * return json data {"name":"N900-BTDESK-xxxx","address":""}
	 */
	String getRememberDevice();
	
	void registerStatusListener(in OnStatusListener listener);
	
	void unregisterStatusListener(in OnStatusListener listener);

	//boolean sendCommand(in byte[] data);

	//boolean isCommandConnected();

	IBinder getService(String name);
	
	//0x31 des 0x32 sm4
	byte[] getPinInput(int mk,int wk,int mode);
	boolean loadMainKey(in byte[] key,int index);
	boolean loadWorkingKey(in byte[] key,int index);

	void startUsb(int port);
	void stopUsb();

	//0串口 8 USB 10 USB-HOST (扫码枪等需要供电的设备使用)
   boolean sendWithPort(int port,in byte[] data);
}
