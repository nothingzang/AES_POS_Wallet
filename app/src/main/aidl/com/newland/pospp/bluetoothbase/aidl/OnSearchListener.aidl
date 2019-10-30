package com.newland.pospp.bluetoothbase.aidl;

interface OnSearchListener {
	void onDeviceFound(String name, String address);
	
	void onFinish();
}
