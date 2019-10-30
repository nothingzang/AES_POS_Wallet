package com.example.aes_pos_wallet.activitys;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.aes_pos_wallet.AESApplication;
import com.example.aes_pos_wallet.R;
import com.example.aes_pos_wallet.device.S_DeviceInfo;
import com.example.aes_pos_wallet.utils.DialogUtils;
import com.example.aes_pos_wallet.utils.MessageTag;
import com.example.aes_pos_wallet.utils.ToastUtils;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.externalrfcard.ExternalRFCard;
import com.newland.mtype.module.common.rfcard.K21RFCardModule;
import com.newland.mtype.module.common.rfcard.RFCardType;
import com.newland.mtype.module.common.rfcard.RFKeyMode;
import com.newland.mtype.module.common.rfcard.RFResult;
import com.newland.mtype.util.Dump;
import com.newland.mtype.util.ISOUtils;
import com.newland.ndk.NdkApiManager;
import com.newland.ndk.RfCard;

import java.util.concurrent.TimeUnit;

import butterknife.OnClick;

public class TestActivity extends BaseActivity {


    private String snr;
    private K21RFCardModule rfCardModule;
    private ExternalRFCard externalRFCard;
    private RfCard rfCard;
    private EmvModule emvModule;


    @Override
    int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    void initDate() {
        rfCardModule = S_DeviceInfo.getInstance().getRFCardModule();
        externalRFCard = S_DeviceInfo.getInstance().getExternalRfCard();
        emvModule = S_DeviceInfo.getInstance().getEmvModuleType();
        rfCard = NdkApiManager.getNdkApiManager().getRfCard();
    }

    @Override
    void initView() {

    }

    @Override
    void bindListener() {

    }

    @Override
    void back() {

    }

    /**
     * 不使用磁道加密
     */
    private final int NO_ENC_TRACK = -1;

    @Override
    @OnClick({R.id.button, R.id.button2, R.id.button3, R.id.button4, R.id.button7, R.id.button8})
    public void onClick(View view) {
        try {
            super.onClick(view);
        } catch (Exception e) {
            return;
        }
        switch (view.getId()) {
            case R.id.button:
//                rfcardState();
                doWrite();
                break;
            case R.id.button2:
//                powerOn();
//                M1PowerOn();
                doReadPassWord();
                break;
            case R.id.button3:
                rfcardCommunication();
                break;
            case R.id.button4:
                authenticateByExtendKey();
                break;
            case R.id.button7:
                m1CardWrite();
                break;
            case R.id.button8:
                m1CardRead();
                break;

        }


    }

    /**
     * 非接卡是否在位
     */
    private static boolean rfcardState() {
        try {
            boolean isExit = S_DeviceInfo.getInstance().getRFCardModule().isRfcardExist();
            if (isExit) {
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_isExist) + "\r\n", MessageTag.NORMAL);
            } else {
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_is_not_exist) + "\r\n", MessageTag.NORMAL);
            }
            return isExit;
        } catch (Exception e) {
            ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_is_not_exist) + "\r\n", MessageTag.NORMAL);
        }
        return false;
    }

    /**
     * 非接通信
     */
    private void rfcardCommunication() {
        try {
            String apdu = "0084000004";
            byte req[] = ISOUtils.hex2byte(apdu);
            byte result[] = rfCardModule.call(req, 60, TimeUnit.SECONDS);
            ToastUtils.showMessage(getString(R.string.msg_send_data) + apdu + "\r\n", MessageTag.DATA);
            ToastUtils.showMessage(getString(R.string.msg_get_data) + ISOUtils.hexString(result) + "\r\n", MessageTag.DATA);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showMessage(getString(R.string.msg_RFcard_communicate_error) + e + "\r\n", MessageTag.ERROR);
        }
    }

    private void M1PowerOn() {
        String[] items = new String[]{"s70", "s50", "pro"};
        DialogUtils.createSingleChoiceDialog(this, getString(R.string.msg_M1Card_type), items, new DialogUtils.SingleChoiceDialogCallback() {

            @Override
            public void onResult(int id) {
                try {
                    RFResult qPResult = null;
                    if (id >= 0) {
                        if (id == 0) {
                            ToastUtils.showMessage(getString(R.string.msg_select_S70_Card_poweron), MessageTag.DATA);
                            qPResult = rfCardModule.powerOn(new RFCardType[]{RFCardType.M1CARD}, 10, TimeUnit.SECONDS, (byte) 0x18);
                        } else if (id == 1) {
                            ToastUtils.showMessage(getString(R.string.msg_select_S50_card_poweron), MessageTag.DATA);
                            qPResult = rfCardModule.powerOn(new RFCardType[]{RFCardType.M1CARD}, 10, TimeUnit.SECONDS, (byte) 0x08);
                        } else if (id == 2) {
                            ToastUtils.showMessage(getString(R.string.msg_select_Prod_card_poweron), MessageTag.DATA);
                            qPResult = rfCardModule.powerOn(new RFCardType[]{RFCardType.M1CARD}, 10, TimeUnit.SECONDS, (byte) 0x28);
                        }

                        if (qPResult.getCardSerialNo() == null) {
                            ToastUtils.showMessage(getString(R.string.msg_M1card_SN_NO_null) + "\r\n", MessageTag.DATA);
                        } else {
                            ToastUtils.showMessage(getString(R.string.msg_M1card_SN_NO) + ISOUtils.hexString(qPResult.getCardSerialNo()) + "\r\n", MessageTag.DATA);
                            snr = ISOUtils.hexString(qPResult.getCardSerialNo());
                        }
                    } else {
                        ToastUtils.showMessage(getString(R.string.dialog_btn_cancel), MessageTag.ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showMessage(getString(R.string.msg_error) + e, MessageTag.ERROR);
                }

            }
        });
    }

    private static String powerOn() {
        String snr = "";
        try {
            RFResult qPResult = S_DeviceInfo.getInstance().getRFCardModule().powerOn(new RFCardType[]{RFCardType.M1CARD}, 10, TimeUnit.SECONDS);
            if (qPResult != null && qPResult.getQpCardType() != null) {
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_type) + qPResult.getQpCardType() + "\r\n", MessageTag.DATA);
            } else {
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_error) + "\r\n", MessageTag.ERROR);
                return snr;
            }
            if (qPResult.getCardSerialNo() == null) {
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_SN_NO) + "\r\n", MessageTag.DATA);
            } else {
                snr = ISOUtils.hexString(qPResult.getCardSerialNo());
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_SN) + ISOUtils.hexString(qPResult.getCardSerialNo()) + "\r\n", MessageTag.DATA);
            }

            if (qPResult.getATQA() == null) {
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_ATQA_null) + "\r\n", MessageTag.DATA);
            } else {
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_ATQA) + Dump.getHexDump(qPResult.getATQA()) + "\r\n", MessageTag.DATA);
            }
            ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_poweron_finished) + "\r\n", MessageTag.NORMAL);
            return snr;
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_RFcard_poweron_error) + e, MessageTag.ERROR);
        }
        return snr;
    }

    /**
     * 外部密钥认证
     */
    private void authenticateByExtendKey() {
        String[] items = new String[]{"KEYA_0x60", "KEYA_0x00", "KEYB_0x61", "KEYB_0x01"};
        DialogUtils.createCustomDialog(this, getString(R.string.tv_external_key_auth), items, R.layout.dialog_m1_external_auth, new DialogUtils.CustomDialogCallback() {

            @Override
            public void onResult(int id, View dialogView) {
                RFKeyMode qpKeyMode = RFKeyMode.KEYA_0X60;
                if (id >= 0) {
                    if (id == 0) {
                        qpKeyMode = RFKeyMode.KEYA_0X60;
                        ToastUtils.showMessage("KEYA_0X60", MessageTag.DATA);
                    } else if (id == 1) {
                        ToastUtils.showMessage("KEYA_0X00", MessageTag.DATA);
                        qpKeyMode = RFKeyMode.KEYA_0X00;
                    } else if (id == 2) {
                        ToastUtils.showMessage("KEYB_0X61", MessageTag.DATA);
                        qpKeyMode = RFKeyMode.KEYB_0X61;
                    } else {
                        ToastUtils.showMessage("KEYB_0X01", MessageTag.DATA);
                        qpKeyMode = RFKeyMode.KEYB_0X01;
                    }

                    EditText edtBlockNum = dialogView.findViewById(R.id.edit_qccard_block);
                    EditText edtKey = dialogView.findViewById(R.id.edit_qccard_key);

                    int block = Integer.valueOf(edtBlockNum.getText().toString());
                    byte sn[] = null;

                    if (snr != null) {
                        sn = ISOUtils.hex2byte(snr);
                    } else {
                        ToastUtils.showMessage(getString(R.string.msg_check_RFcard_isPoweredon) + "\r\n", MessageTag.ERROR);
                        return;
                    }
                    byte key[] = ISOUtils.hex2byte(edtKey.getText().toString());
                    if (block >= 0 && block <= 255 && key.length == 6 && sn != null && sn.length == 4) {
                        try {
                            rfCardModule.authenticateByExtendKey(qpKeyMode, sn, block, key);
                            ToastUtils.showMessage(getString(R.string.msg_RFcard_external_key_auth_finished) + "\r\n", MessageTag.NORMAL);
                            ToastUtils.showMessage(getString(R.string.msg_KEY_mode) + qpKeyMode + "\r\n", MessageTag.DATA);
                            ToastUtils.showMessage(getString(R.string.msg_SNR_SN_NO) + (snr == null ? "null" : ISOUtils.hexString(sn)) + "\r\n", MessageTag.DATA);
                            ToastUtils.showMessage(getString(R.string.msg_security_block_NO) + block + "\r\n", MessageTag.DATA);
                            ToastUtils.showMessage(getString(R.string.msg_external_key) + (key == null ? "null" : ISOUtils.hexString(key)) + "\r\n", MessageTag.DATA);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showMessage(getString(R.string.msg_error) + e, MessageTag.ERROR);
                        }

                    } else {
                        ToastUtils.showMessage(getString(R.string.msg_write_illegal), MessageTag.ERROR);
                    }
                } else {
                    ToastUtils.showMessage(getString(R.string.dialog_btn_cancel), MessageTag.DATA);
                }

            }
        });
    }

    /**
     * 写块
     */
    public void m1CardWrite() {
        DialogUtils.createCustomDialog(this, getString(R.string.tv_write_block), null, R.layout.dialog_m1_write, new DialogUtils.CustomDialogCallback() {
            @Override
            public void onResult(int id, View dialogView) {
                try {
                    if (id == -1) {
                        return;
                    }
                    EditText edit_qccard_block = (EditText) dialogView.findViewById(R.id.edit_qccard_block);
                    EditText edit_qccard_data = (EditText) dialogView.findViewById(R.id.edit_qccard_data);
                    int block = Integer.valueOf(edit_qccard_block.getText().toString());
                    byte input[] = ISOUtils.hex2byte(edit_qccard_data.getText().toString());
                    if (block >= 0 && block <= 255 && input.length == 16) {
                        rfCardModule.writeDataBlock(block, input);
                        ToastUtils.showMessage(getString(R.string.msg_storage_block) + block + "\r\n", MessageTag.DATA);
                        ToastUtils.showMessage(getString(R.string.msg_write_block_data) + (input == null ? "null" : ISOUtils.hexString(input)) + "\r\n", MessageTag.DATA);
                        ToastUtils.showMessage(getString(R.string.msg_write_block_data) + "\r\n", MessageTag.NORMAL);
                    } else {
                        ToastUtils.showMessage(getString(R.string.msg_write_illegal) + "\r\n", MessageTag.ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showMessage(getString(R.string.msg_write_block_data_error) + e.getMessage() + "\r\n", MessageTag.ERROR);
                    ToastUtils.showMessage(getString(R.string.msg_check_RFcard_isPoweredon) + "\r\n", MessageTag.ERROR);
                }
            }
        });
    }

    /**
     * 读块
     */
    private void m1CardRead() {
        TextView tip = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null).findViewById(R.id.textview_tip);
        tip.setText(getString(R.string.dialog_tv_qccard_block));
        DialogUtils.createCustomDialog(this, getString(R.string.tv_read_block), null, R.layout.dialog_edittext, new DialogUtils.CustomDialogCallback() {
            @Override
            public void onResult(int id, View dialogView) {
                try {
                    if (id == -1) {
                        return;
                    }
                    EditText editTextData = dialogView.findViewById(R.id.edit_data);
                    int block = Integer.valueOf(editTextData.getText().toString());
                    if (block >= 0 && block <= 255) {
                        byte output[] = rfCardModule.readDataBlock(block);
                        ToastUtils.showMessage(getString(R.string.msg_storage_block) + block + "\r\n", MessageTag.DATA);
                        ToastUtils.showMessage(getString(R.string.msg_data) + output == null ? "null" : ISOUtils.hexString(output) + "\r\n", MessageTag.DATA);
                        ToastUtils.showMessage(getString(R.string.msg_read_block_finished) + "\r\n", MessageTag.NORMAL);
                    } else {
                        ToastUtils.showMessage(getString(R.string.msg_input_illegal) + "\r\n", MessageTag.ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showMessage(getString(R.string.msg_read_block_error) + e + "\r\n", MessageTag.ERROR);
                }

            }
        });
    }

    public static String doRead() {
        int block = 4;
        if (rfcardState()) {
            try {
                S_DeviceInfo.getInstance().getRFCardModule().authenticateByExtendKey(RFKeyMode.KEYA_0X60, ISOUtils.hex2byte(powerOn()), block, ISOUtils.hex2byte("ffffffffffff"));
                byte output[] = S_DeviceInfo.getInstance().getRFCardModule().readDataBlock(block);
                String res = ISOUtils.hexString(output);
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_error) + e, MessageTag.ERROR);
            }
        }
        return null;
    }

    public static void doWrite() {
        int block = 4;
//        String number = Long.toHexString(15723184098311921L);
        String number = "15723184098311921";
        byte input[] = ISOUtils.hex2byte(number);
        if (rfcardState()) {
            try {
                S_DeviceInfo.getInstance().getRFCardModule().authenticateByExtendKey(RFKeyMode.KEYA_0X60, ISOUtils.hex2byte(powerOn()), block, ISOUtils.hex2byte("ffffffffffff"));
                S_DeviceInfo.getInstance().getRFCardModule().writeDataBlock(block, input);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_error) + e, MessageTag.ERROR);
            }
        }
    }


    public static void doReadPassWord() {
        int block = 1;
        for (long i = 0; i < 4294967295l; i++) {
            if (rfcardState()) {
                String passWord = Long.toHexString(i);
                while (passWord.length() < 12) {
                    passWord = "0" + passWord;
                }
                try {
                    ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_external_key) + ":" + passWord, MessageTag.ERROR);
                    S_DeviceInfo.getInstance().getRFCardModule().authenticateByExtendKey(RFKeyMode.KEYA_0X60, ISOUtils.hex2byte(powerOn()), block, ISOUtils.hex2byte(passWord));
                    byte output[] = S_DeviceInfo.getInstance().getRFCardModule().readDataBlock(block);
                    String res = ISOUtils.hexString(output);
                    ToastUtils.showMessage("成功获取——结果:" + res, MessageTag.ERROR);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showMessage(AESApplication.getInstance().getString(R.string.msg_error) + e, MessageTag.ERROR);
                }
            } else return;
        }
    }
}
