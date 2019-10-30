package com.newland.pospp.bluetoothbase.aidl;

interface OnStatusListener {
	//String getIdentity();
	void onStatusChange(int newStatus, int oldStatus);
}
