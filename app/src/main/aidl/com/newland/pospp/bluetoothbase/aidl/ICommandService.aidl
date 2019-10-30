package com.newland.pospp.bluetoothbase.aidl;

interface ICommandService {
    String getBluetoothName();
    String getBluetoothAddress();
    boolean openCashBox();
    String getBluetoothVersion();

    boolean setBackConnectEnabled(boolean enable);
    boolean setAuthEnabled(boolean enable);
    boolean setBaudRate(int baudRate);
    int getBaudRate();

    int getDeviceStatus();

    boolean isCommandConnected();
}
