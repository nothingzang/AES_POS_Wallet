package com.example.aes_pos_wallet.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import com.newland.pospp.bluetoothbase.aidl.DataForwardServiceAidl;
import com.newland.pospp.bluetoothbase.aidl.ICommandService;
import com.newland.pospp.bluetoothbase.aidl.OnDataReceiveListener;
import com.newland.pospp.bluetoothbase.aidl.OnPinpadListener;
import com.newland.pospp.bluetoothbase.aidl.OnStatusListener;

import org.json.JSONException;
import org.json.JSONObject;

public class DataForwardController {
    public static final String TAG = "DataForwardController";

    String DATA_SERVICE_ACTION = "com.newland.pospp.bluetooth.service.Data";

    private static DataForwardController dataController;

    DataForwardServiceAidl dataServiceAidl = null;
    OnDataReceiveListener listener = null;
    OnDataReceiveListener tmpListener = null;
    OnStatusListener statusListener = null;
    OnStatusListener tmpStatusListener = null;
    OnPinpadListener pinpadListener = null;
    OnPinpadListener tmpPinpadListener = null;

    private DataForwardController() {
        initAidl();
    }

    private ServiceConnection serviceConn;

    private void initAidl() {

        serviceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                dataServiceAidl = DataForwardServiceAidl.Stub.asInterface(service);
                try {
                    if (tmpListener != null) {
                        dataServiceAidl.registerDataReceiveListener(tmpListener);
                        listener = tmpListener;
                        tmpListener = null;
                    }
                    if (tmpStatusListener != null) {
                        dataServiceAidl.registerStatusListener(tmpStatusListener);
                        statusListener = tmpStatusListener;
                        tmpStatusListener = null;
                    }
                    if (tmpPinpadListener != null) {
                        dataServiceAidl.registerPinpadListener(tmpPinpadListener);
                        pinpadListener = tmpPinpadListener;
                        tmpPinpadListener = null;
                    }

                    dataServiceAidl.setIsPassthrough(true);

                    //输密
//					loadMainKey(new byte[]{0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31}, 0);
//					loadWorkingKey(new byte[]{0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31,0X31}, 1);
//					startPinInput(0,1,0x32);


                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                dataServiceAidl = null;
                if (listener != null || statusListener != null) {
                }
            }
        };
    }

    public static DataForwardController getInstance() {
        if (dataController == null) {
            dataController = new DataForwardController();
        }

        return dataController;
    }

    /**
     * 初始化,绑定服务
     */
    public void init(Context context, OnDataReceiveListener listener, OnStatusListener statusListener) {
        try {
            if (dataServiceAidl != null) {
                return;
            }
            if (this.listener != null || this.statusListener != null) {
                this.listener = null;
                this.statusListener = null;
            }
            this.tmpListener = listener;
            this.tmpStatusListener = statusListener;
            initAidl();
            Intent intent = new Intent();
            intent.setAction(DATA_SERVICE_ACTION);
            context.bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化,绑定服务
     */
    public void init(Context context, OnDataReceiveListener listener, OnStatusListener statusListener, OnPinpadListener pinpadListener) {
        try {
            if (dataServiceAidl != null) {
                return;
            }
            if (this.listener != null || this.statusListener != null || this.pinpadListener != null) {
                this.listener = null;
                this.statusListener = null;
                this.pinpadListener = null;
            }
            this.tmpListener = listener;
            this.tmpStatusListener = statusListener;
            this.tmpPinpadListener = pinpadListener;

            Intent intent = new Intent();
            intent.setAction(DATA_SERVICE_ACTION);
            context.bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadMainKey(byte[] key, int index) {
        try {
            boolean ret = dataServiceAidl.loadMainKey(key, index);
            Log.d("bttest", "loadMainKey:" + ret);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loadWorkingKey(byte[] key, int index) {
        try {
            boolean ret = dataServiceAidl.loadWorkingKey(key, index);
            Log.d("bttest", "loadMainKey:" + ret);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void startPinInput(int mk, int wk, int mode) {
        try {
            dataServiceAidl.getPinInput(mk, wk, mode);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void openUsb() {
        try {
            dataServiceAidl.startUsb(8);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void openUsbhost() {
        try {
            dataServiceAidl.startUsb(10);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void closeUsb() {
        try {
            dataServiceAidl.stopUsb();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendport(int port, byte[] data) {
//        try {
//            dataServiceAidl.sendWithPort(port, data);
//        } catch (RemoteException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    public void test1(OnDataReceiveListener onDataReceiveListener){
        try {
            dataServiceAidl.registerDataReceiveListener(onDataReceiveListener);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void test2(){
        try {
            dataServiceAidl.setIsCheckDataLenth(false);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void test3(){
        try {
            dataServiceAidl.setIsPassthrough(true);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 解绑服务
     */
    public void release(Context context) {
        try {
            if (dataServiceAidl != null) {
                try {
                    if (listener != null) {
                        dataServiceAidl.unregisterDataReceiveListener(listener);
                    }
                    if (statusListener != null) {
                        dataServiceAidl.unregisterStatusListener(statusListener);
                    }
                    if (pinpadListener != null) {
                        dataServiceAidl.unregisterPinpadListener(pinpadListener);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener = null;
                statusListener = null;
                pinpadListener = null;
                context.unbindService(serviceConn);
                dataServiceAidl = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean send(byte[] data) {
        if (dataServiceAidl != null) {
            try {
                return dataServiceAidl.send(data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isConnectAIDL() {
        return dataServiceAidl != null;
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    public String getDeviceName() {
        String deviceName = "";
        try {
            String jsonStr = dataServiceAidl.getRememberDevice();
            JSONObject obj = new JSONObject(jsonStr);
            deviceName = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return deviceName;
    }

    /**
     * 获取控制通道服务
     *
     * @return
     */
    public ICommandService getCommandService() {
        try {
            return ICommandService.Stub.asInterface(dataServiceAidl.getService(""));
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取波特率
     */
    public int getBaudRate() {

        try {

            return getCommandService().getBaudRate();
        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }

    }

    /**
     * 设置波特率
     */
    public boolean setBaudRate(int baudRate) {

        try {

            return getCommandService().setBaudRate(baudRate);
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

    }

}
