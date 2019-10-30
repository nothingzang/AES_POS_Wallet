package com.newland.pospp.bluetoothbase.aidl;

import com.newland.pospp.bluetoothbase.aidl.OnStatusListener;

import com.newland.pospp.bluetoothbase.aidl.OnSearchListener;

import com.newland.pospp.bluetoothbase.aidl.OnDataReceiveListener;

interface BluetoothServiceAidl {

	
	// 说明 :BTWB = BluetoothWifiBase = 蓝牙底座带wifi版 （本程序所有该版本底座相关方式使用此缩写)
	 

	//-------------------------------- 通用蓝牙方法 --------------------------------
	void setIsCheckDataLenth(boolean value); //是否校验底座数据长度
	void setIsPassthrough(boolean value);  //是否AIDL透传
	boolean getIsCheckDataLenth(); //是否校验底座数据长度
	boolean getIsPassthrough();  //是否AIDL透传
	
	boolean isBluetoothEnable();
	
	void enableBluetooth();
	
	void disableBluetooth();
	
	void startBluetoothSearch(in OnSearchListener listener);
	
	boolean startBluetoothConn(String name,String address);
	
	/**
	 * return json data {"name":"xxxx","address":"xxx"}
	 */
	String getRememberDevice();
	
	String getConnectedDevice();
	
	int getStatus();
	
	/**
	* 1 纯蓝牙底座 2 BTWB底座 0 非底座  -1 未连接
	*/
	int getBluetoothType();
		
	void disConnectBluetooth();
	
	void registerStatusListener(in OnStatusListener listener);
	
	void unregisterStatusListener(in OnStatusListener listener);
	
	boolean isForceAuth();
	
	void setForceAuthFlag(boolean isForce);
	
	boolean isFilterName();
	
	void setFilterNameFlag(boolean flag);	

	
	//-------------------------------- 双通道蓝牙底座专用方法（带命令模式） --------------------------------
	/**
	 * true时可以调用ICommandService的接口
 	 */
	boolean isCommandConnected();
	
	
	//-------------------------------- WIFI版蓝牙底座专用方法 --------------------------------
	/**
	 * port = 0 串口模式 
	 * port = 8 usb方式 
	 * port = 10 扫码枪虚拟串口
	 * port = -1 默认 串口+usb方式
	 * port = 11 全模式
 	 */
	void setPort(int port);
	
	int getPort();
	
	
}
