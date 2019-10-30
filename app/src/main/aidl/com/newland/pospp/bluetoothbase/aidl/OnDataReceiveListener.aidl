package com.newland.pospp.bluetoothbase.aidl;

interface OnDataReceiveListener {
    //废弃
	void onDataReceive(in byte[] data);
	//8 USB 10 USB-HOST (扫码枪等需要供电的设备使用)
	void onDataReceiveWithPort(int port,in byte[] data);
}
