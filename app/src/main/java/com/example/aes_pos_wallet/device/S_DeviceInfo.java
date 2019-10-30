package com.example.aes_pos_wallet.device;

import android.content.Context;
import android.newland.os.NlBuild;
import android.os.Build;
import android.os.Handler;

import com.example.aes_pos_wallet.AESApplication;
import com.example.aes_pos_wallet.R;
import com.example.aes_pos_wallet.utils.ExecutorsUtils;
import com.example.aes_pos_wallet.utils.MessageTag;
import com.example.aes_pos_wallet.utils.ToastUtils;
import com.newland.me.ConnUtils;
import com.newland.me.DeviceManager;
import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.ExModuleType;
import com.newland.mtype.ModuleType;
import com.newland.mtype.conn.DeviceConnParams;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.K21CardReader;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.externalGuestDisplay.ExternalGuestDisplay;
import com.newland.mtype.module.common.externalPin.ExternalPinInput;
import com.newland.mtype.module.common.externalScan.ExternalScan;
import com.newland.mtype.module.common.externalrfcard.ExternalRFCard;
import com.newland.mtype.module.common.externalsignature.ExternalSignature;
import com.newland.mtype.module.common.iccard.ICCardModule;
import com.newland.mtype.module.common.light.IndicatorLight;
import com.newland.mtype.module.common.pin.K21Pininput;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.rfcard.K21RFCardModule;
import com.newland.mtype.module.common.scanner.BarcodeScanner;
import com.newland.mtype.module.common.scanner.BarcodeScannerManager;
import com.newland.mtype.module.common.security.K21SecurityModule;
import com.newland.mtype.module.common.serialport.SerialModule;
import com.newland.mtype.module.common.sm.SmModule;
import com.newland.mtype.module.common.storage.Storage;
import com.newland.mtype.module.common.swiper.K21Swiper;
import com.newland.mtypex.nseries.NSConnV100ConnParams;
import com.newland.mtypex.nseries3.NS3ConnParams;

import java.lang.reflect.Method;

/**
 * Created by YJF on 2015/8/11 0011.
 */
public class S_DeviceInfo extends AbstractDevice {

    private static final String K21_DRIVER_NAME = "com.newland.me.K21Driver";
    private DeviceConnParams deviceConnParams = null;
    private boolean isConnectDevice = false;//设备连接状态

    public boolean isConnectDevice() {
        return isConnectDevice;
    }

    public void setConnectDevice(boolean connectDevice) {
        isConnectDevice = connectDevice;
    }

    private static Context context;
    private static DeviceManager deviceManager;
    private static S_DeviceInfo sdkDevice;

    private S_DeviceInfo(Context context) {
        this.context = context;
    }

    public static S_DeviceInfo getInstance() {
        if (sdkDevice == null) {
            synchronized (S_DeviceInfo.class) {
                if (sdkDevice == null) {
                    sdkDevice = new S_DeviceInfo(AESApplication.getInstance());
                }
            }
        }
        return sdkDevice;
    }

    @Override
    public void connectDevice() {
        ExecutorsUtils.getThreadPoolInstance().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    showMessage(R.string.msg_device_connecting, MessageTag.TIP);
                    deviceManager = ConnUtils.getDeviceManager();
                    initConnParams();
                    deviceManager.init(context, K21_DRIVER_NAME, deviceConnParams, new DeviceEventListener<ConnectionCloseEvent>() {

                        @Override
                        public void onEvent(ConnectionCloseEvent event, Handler handler) {
                            if (event.isSuccess()) {
                                showMessage(context.getString(R.string.msg_device_disconn_succ), MessageTag.NORMAL);
                            }
                            if (event.isFailed()) {
                                showMessage(context.getString(R.string.msg_conn_exception), MessageTag.ERROR);
                            }
                        }

                        @Override
                        public Handler getUIHandler() {
                            return null;
                        }
                    });
                    deviceManager.connect();
                    showMessage(context.getString(R.string.msg_device_conn_succ), MessageTag.NORMAL);
                    showMessage(context.getString(R.string.device_type) + Build.MODEL, MessageTag.NORMAL);
                    showMessage(context.getString(R.string.connect_type) + deviceConnParams.getConnectType(), MessageTag.NORMAL);
                    setConnectDevice(true);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    showMessage(context.getString(R.string.msg_conn_exception) + e1, MessageTag.ERROR);
                }
            }
        });
    }


    @Override
    public void disconnect() {
        ExecutorsUtils.getThreadPoolInstance().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (deviceManager != null) {
                        deviceManager.disconnect();
                        deviceManager = null;
                        showMessage(R.string.msg_device_disconn_succ, MessageTag.TIP);
                        setConnectDevice(false);
                        sdkDevice = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(context.getString(R.string.msg_device_disconn_exception) + e, MessageTag.TIP);
                }
            }
        });
    }

    private void showMessage(String msg, int messageType) {
        ToastUtils.showMessage(msg, messageType);
    }

    private void showMessage(int msgId, int messageType) {
        ToastUtils.showMessage(context.getString(msgId), messageType);
    }

    public static String SDKVersion() {
        String version = "unknown";
        version = getProperties("ro.build.newland_sdk");
        if ("unknown".equals(version)) {
            version = getProperties("ro.build.customer_id");
            if ("unknown".equals(version)) {
                // According to the MTMS rules
                //20180719，SDK 2.0： SDK 2.0 branch、ChinaUms、AliQianNiu，the others are SDK 3.0.
                return version;
            } else if ("ChinaUms".equals(version) || "SDK_2.0".equals(version) || "AliQianNiu".equals(version)) {
                return "SDK2.0";
            } else {
                return "SDK3.0";
            }
        } else {
            return version;
        }


    }

    private static String getProperties(String key) {
        String defaultValue = "unknown";
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, defaultValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private void initConnParams() {
        String current_driver_version = NlBuild.VERSION.NL_FIRMWARE;
        if (SDKVersion() == "SDK2.0") {
            deviceConnParams = new NSConnV100ConnParams();
        } else if (SDKVersion() == "SDK3.0") {
            deviceConnParams = new NS3ConnParams();
        } else {            //unkonwn
            if ("SA1".equals(NlBuild.VERSION.NL_HARDWARE_ID) && Build.MODEL.equals("N900")) { //900 3G Only supports 2.0
                deviceConnParams = new NSConnV100ConnParams();
                return;
            }
            if (Build.MODEL.equals("N900")) {
                if (current_driver_version.equals("V2.0.28") || current_driver_version.equals("V2.1.03") || current_driver_version.equals("V2.1.05") || current_driver_version.equals("V2.1.09") || current_driver_version.equals("V2.1.18") || current_driver_version.equals("V2.1.27") || current_driver_version.equals("V2.1.37") || current_driver_version.equals("V2.1.49") || current_driver_version.equals("V2.1.53") || current_driver_version.equals("V2.1.58")
                        || current_driver_version.equals("V2.1.62") || current_driver_version.equals("V2.0.16") || current_driver_version.equals("V2.1.17") || current_driver_version.equals("V2.1.21") || current_driver_version.equals("V2.1.23") || current_driver_version.equals("V2.1.24") || current_driver_version.equals("V2.1.29") || current_driver_version.equals("V2.1.31") || current_driver_version.equals("V2.1.32") || current_driver_version.equals("V2.1.40") || current_driver_version.equals("V2.1.41")

                        || current_driver_version.equals("V2.1.44") || current_driver_version.equals("V2.0.45") || current_driver_version.equals("V2.1.46") || current_driver_version.equals("V2.1.48") || current_driver_version.equals("V2.1.51") || current_driver_version.equals("V2.1.55") || current_driver_version.equals("V2.1.56") || current_driver_version.equals("V2.1.60")) {
                    deviceConnParams = new NS3ConnParams(); //3.0 connection
                } else {
                    deviceConnParams = new NSConnV100ConnParams(); //2.0 connect default
                }

            } else if (Build.MODEL.equals("N910")) {
                if (current_driver_version.equals("V2.0.28") || current_driver_version.equals("V2.1.03") || current_driver_version.equals("V2.1.05") || current_driver_version.equals("V2.1.09") || current_driver_version.equals("V2.1.18") || current_driver_version.equals("V2.1.27") || current_driver_version.equals("V2.1.35") || current_driver_version.equals("V2.1.40") || current_driver_version.equals("V2.1.52") || current_driver_version.equals("V2.1.54")
                        || current_driver_version.equals("V2.1.64") || current_driver_version.equals("V2.0.16") || current_driver_version.equals("V2.1.13") || current_driver_version.equals("V2.1.21") || current_driver_version.equals("V2.1.23") || current_driver_version.equals("V2.1.24") || current_driver_version.equals("V2.1.29") || current_driver_version.equals("V2.1.30") || current_driver_version.equals("V2.1.32") || current_driver_version.equals("V2.1.43") || current_driver_version.equals("V2.0.33")
                        || current_driver_version.equals("V2.1.44") || current_driver_version.equals("V2.0.45") || current_driver_version.equals("V2.1.37") || current_driver_version.equals("V2.0.36") || current_driver_version.equals("V2.1.66") || current_driver_version.equals("V2.1.67") || current_driver_version.equals("V2.1.68") || current_driver_version.equals("V2.1.71") || current_driver_version.equals("V2.1.72") || current_driver_version.equals("V2.3.00") || current_driver_version.equals("V2.1.57") || current_driver_version.equals("V2.1.26")) {
                    deviceConnParams = new NS3ConnParams();
                } else {
                    deviceConnParams = new NSConnV100ConnParams(); //2.0 connect default
                }
            } else {
                deviceConnParams = new NS3ConnParams();
            }

        }
    }

    @Override
    public boolean isDeviceAlive() {
        boolean ifConnected = (deviceManager == null ? false : deviceManager.getDevice().isAlive());
        return ifConnected;
    }

    @Override
    public K21CardReader getCardReaderModuleType() {
        K21CardReader cardReader = (K21CardReader) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_CARDREADER);
        return cardReader;
    }

    @Override
    public EmvModule getEmvModuleType() {
        EmvModule emvModule = (EmvModule) deviceManager.getDevice().getExModule(ExModuleType.EMVINNERLEVEL2);
        return emvModule;
    }

    @Override
    public ICCardModule getICCardModule() {
        ICCardModule iCCardModule = (ICCardModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_ICCARDREADER);
        return iCCardModule;
    }

    @Override
    public IndicatorLight getIndicatorLight() {
        IndicatorLight indicatorLight = (IndicatorLight) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_INDICATOR_LIGHT);
        return indicatorLight;
    }

    @Override
    public K21Pininput getK21Pininput() {
        K21Pininput k21Pininput = (K21Pininput) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PININPUT);
        return k21Pininput;
    }

    @Override
    public Printer getPrinter() {
        Printer printer = (Printer) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PRINTER);
        return printer;
    }

    @Override
    public K21RFCardModule getRFCardModule() {
        K21RFCardModule rFCardModule = (K21RFCardModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_RFCARDREADER);
        return rFCardModule;
    }

    BarcodeScanner barcodeScanner;
    K21CardReader cardReader;
    public BarcodeScanner getScanner() {
        if (barcodeScanner == null) barcodeScanner = getBarcodeScanner();
        return barcodeScanner;
    }
    public K21CardReader getCardRead() {
        if (cardReader == null) cardReader = getCardReaderModuleType();
        return cardReader;
    }

    @Override
    public BarcodeScanner getBarcodeScanner() {
        BarcodeScannerManager barcodeScannerManager = (BarcodeScannerManager) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_BARCODESCANNER);
        BarcodeScanner scanner = barcodeScannerManager.getDefault();
        return scanner;
    }

    @Override
    public K21SecurityModule getSecurityModule() {
        K21SecurityModule securityModule = (K21SecurityModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SECURITY);
        return securityModule;
    }

    @Override
    public Storage getStorage() {
        Storage storage = (Storage) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_STORAGE);
        return storage;
    }

    @Override
    public K21Swiper getK21Swiper() {
        K21Swiper k21Swiper = (K21Swiper) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SWIPER);
        return k21Swiper;
    }

    @Override
    public com.newland.mtype.Device getDevice() {
        return deviceManager.getDevice();
    }

    @Override
    public SerialModule getUsbSerial() {
        SerialModule k21Serial = (SerialModule) deviceManager.getDevice().getExModule(ExModuleType.USBSERIAL);
        return k21Serial;
    }

    @Override
    public SmModule getSmModule() {
        SmModule smModule = (SmModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SM);
        return smModule;
    }

    @Override
    public String getModel() {
        return Build.MODEL;
    }

    @Override
    public ExternalPinInput getExternalPinInput() {
        return (ExternalPinInput) deviceManager.getDevice().getStandardModule(ModuleType.EXTERNAL_PININPUT);
    }

    @Override
    public ExternalRFCard getExternalRfCard() {
        return (ExternalRFCard) deviceManager.getDevice().getExModule(ExModuleType.RFCARD);
    }

    @Override
    public ExternalSignature getExternalSignature() {
        return (ExternalSignature) deviceManager.getDevice().getExModule(ExModuleType.SIGNATURE);
    }

    @Override
    public ExternalScan getExternalScan() {
        return (ExternalScan) deviceManager.getDevice().getExModule(ExModuleType.SCAN);
    }

    @Override
    public ExternalGuestDisplay getExternalGuestDisplay() {
        return (ExternalGuestDisplay) deviceManager.getDevice().getExModule(ExModuleType.GUEST_DISPLAY);
    }

    public DeviceConnParams getDeviceConnParams() {
        return deviceConnParams;
    }
}
