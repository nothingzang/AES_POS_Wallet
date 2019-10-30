package com.example.aes_pos_wallet.device;

import com.newland.mtype.Device;
import com.newland.mtype.module.common.cardreader.CardReader;
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
import com.newland.mtype.module.common.rfcard.RFCardModule;
import com.newland.mtype.module.common.scanner.BarcodeScanner;
import com.newland.mtype.module.common.security.SecurityModule;
import com.newland.mtype.module.common.serialport.SerialModule;
import com.newland.mtype.module.common.sm.SmModule;
import com.newland.mtype.module.common.storage.Storage;
import com.newland.mtype.module.common.swiper.K21Swiper;

/**
 * Created by YJF on 2015/8/11 0011.
 */
public abstract class AbstractDevice {

	public abstract void connectDevice();

	public abstract void disconnect();

	public abstract boolean isDeviceAlive();

	public abstract String getModel();

	public abstract Device getDevice();

	public abstract CardReader getCardReaderModuleType();

	public abstract EmvModule getEmvModuleType();

	public abstract ICCardModule getICCardModule();

	public abstract IndicatorLight getIndicatorLight();

	public abstract K21Pininput getK21Pininput();

	public abstract Printer getPrinter();

	public abstract RFCardModule getRFCardModule();

	public abstract BarcodeScanner getBarcodeScanner();

	public abstract SecurityModule getSecurityModule();

	public abstract Storage getStorage();

	public abstract K21Swiper getK21Swiper();

	public abstract SerialModule getUsbSerial();

	public abstract SmModule getSmModule();

	public abstract ExternalPinInput getExternalPinInput();

	public abstract ExternalRFCard getExternalRfCard();

	public abstract ExternalSignature getExternalSignature();

	public abstract ExternalScan getExternalScan();

	public abstract ExternalGuestDisplay getExternalGuestDisplay();

}
