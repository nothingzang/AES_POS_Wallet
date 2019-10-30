package com.example.aes_pos_wallet.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aes_pos_wallet.AESApplication;
import com.example.aes_pos_wallet.R;
import com.example.aes_pos_wallet.adapter.TransactionListAdapter;
import com.example.aes_pos_wallet.constants.Constants;
import com.example.aes_pos_wallet.device.S_DeviceInfo;
import com.example.aes_pos_wallet.listeners.CallBackListener;
import com.example.aes_pos_wallet.response.ResponseVo;
import com.example.aes_pos_wallet.utils.BytesUtils;
import com.example.aes_pos_wallet.utils.DataForwardController;
import com.example.aes_pos_wallet.utils.HexUtil;
import com.example.aes_pos_wallet.utils.HttpUtils;
import com.example.aes_pos_wallet.utils.MessageTag;
import com.example.aes_pos_wallet.utils.SoundPoolImpl;
import com.example.aes_pos_wallet.utils.SoundUtils;
import com.example.aes_pos_wallet.utils.StringUtils;
import com.example.aes_pos_wallet.utils.ToastUtils;
import com.example.aes_pos_wallet.vo.CodeVo;
import com.example.aes_pos_wallet.vo.TransactionPage;
import com.example.aes_pos_wallet.vo.TransactionVo;
import com.example.aes_pos_wallet.vo.VoiceTemplate;
import com.example.aes_pos_wallet.vo.unionVo.UnionCodeValue;
import com.example.aes_pos_wallet.vo.unionVo.UnionVoCode;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.newland.mtype.ModuleType;
import com.newland.mtype.ProcessTimeoutException;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.CommonCardType;
import com.newland.mtype.module.common.cardreader.K21CardReaderEvent;
import com.newland.mtype.module.common.cardreader.OpenCardReaderResult;
import com.newland.mtype.module.common.cardreader.SearchCardRule;
import com.newland.pospp.bluetoothbase.aidl.OnDataReceiveListener;
import com.newland.pospp.bluetoothbase.aidl.OnPinpadListener;
import com.newland.pospp.bluetoothbase.aidl.OnStatusListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;


public class TransactionActivity extends BaseActivity {
    private String success = "<STX><0025><SET><01><00><VOICE=0008收款成功><ETX>";
    private String successCode = HexUtil.string2HexString(success + "<" + HexUtil.xorHexString(HexUtil.string2HexString(success)) + ">");
    private String errTwo = "<STX><0027><SET><01><00><VOICE=0010二维码无效><ETX>";
    private String errTwoCode = HexUtil.string2HexString(errTwo + "<" + HexUtil.xorHexString(HexUtil.string2HexString(errTwo)) + ">");
    private String fail = "<STX><0025><SET><01><00><VOICE=0008收款失败><ETX>";
    private String failCode = HexUtil.string2HexString(fail + "<" + HexUtil.xorHexString(HexUtil.string2HexString(fail)) + ">");
    private String lossPay = "<STX><0025><SET><01><00><VOICE=0008余额不足><ETX>";
    private String lossPayCode = HexUtil.string2HexString(lossPay + "<" + HexUtil.xorHexString(HexUtil.string2HexString(lossPay)) + ">");
    private String outPay = "<STX><0029><SET><01><00><VOICE=0012不在消费时段><ETX>";
    private String outPayCode = HexUtil.string2HexString(outPay + "<" + HexUtil.xorHexString(HexUtil.string2HexString(outPay)) + ">");
    private String doublePay = "<STX><0037><SET><01><00><VOICE=0020不可在该时段重复消费><ETX>";
    private String doublePayCode = HexUtil.string2HexString(doublePay + "<" + HexUtil.xorHexString(HexUtil.string2HexString(doublePay)) + ">");


    private double price = 1;

    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.show_price)
    TextView showPrice;
    @BindView(R.id.edit_price)
    EditText editPrice;
    @BindView(R.id.confim_button)
    TextView confimButton;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout refreshLayout;

    private OnDataReceiveListener dataReceiveListener;
    private OnStatusListener onStatusListener;
    private OnPinpadListener onPinpadListener;

    private TransactionListAdapter transactionListAdapter;
    private List<TransactionVo> transactionVos = new ArrayList<>();
    private int page = 1;
    private int pageSize = 10;

    public TransactionActivity() {
    }

    @Override
    int getLayoutId() {
        return R.layout.activity_orders;
    }

    private void doSaoma(String code) {
        Gson gson = new Gson();
        try {
            if (code.startsWith("dw00")) {
                sendCutPayment(code.substring(4), price);
                return;
            } else if (code.startsWith("hQVDUFY")) {
                String result = HexUtil.bytesToHex(android.util.Base64.decode(code, android.util.Base64.NO_WRAP));
                UnionVoCode unionVoCode = UnionVoCode.getVoByCode(result);
                sendCutPayment(UnionCodeValue.getVoByCode(unionVoCode.getValue().getValue()).getTokenCode(), price);
                return;
            }
            CodeVo codeVo = gson.fromJson(code, CodeVo.class);
            Log.e("Nothing_code", codeVo.toString());
            if (codeVo.getType() != CodeVo.TYPE_PAYMENT) {
                SoundUtils.getInstance().queueSound(new VoiceTemplate()
                        .prefix("fail_code")
                        .gen());
                DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(errTwoCode));
                return;
            }
            sendCutPayment(codeVo.getQrCode(), price);
        } catch (Exception e) {
            SoundUtils.getInstance().queueSound(new VoiceTemplate()
                    .prefix("fail_code")
                    .gen());
            DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(errTwoCode));
            return;
        }
    }


    @Override
    void initDate() {
        transactionListAdapter = new TransactionListAdapter(transactionVos);
        listView.setAdapter(transactionListAdapter);
//        dataReceiveListener = new OnDataReceiveListener.Stub() {
//            @Override
//            public void onDataReceive(byte[] data) throws RemoteException {
//                try {
//                    String code = HexUtil.hexString2String(BytesUtils.bytesToHex(data));
//                    code = code.replaceAll("(\\\r\\\n|\\\r|\\\n|\\\n\\\r)", "");
//                    Log.i("扫码结果：", code);
//                    if (code.startsWith("<STX>")) {
//                        return;
//                    }
//                    doSaoma(code);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onDataReceiveWithPort(int port, byte[] data) throws RemoteException {
//                try {
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };
//        //1.蓝牙关闭 2.蓝牙正在关闭 3.蓝牙正在打开 4.空闲 5.搜索设备 6.正在连接设备7已连接
//        onStatusListener = new OnStatusListener.Stub() {
//            @Override
//            public void onStatusChange(final int newStatus, final int oldStatus) throws RemoteException {
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "状态从" + oldStatus + "变为" + newStatus, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        };
//
//        onPinpadListener = new OnPinpadListener.Stub() {
//
//            @Override
//            public void onPinResult(byte[] pin) throws RemoteException {
////                tvPwd.setText("密码：" + BytesUtils.bytesToHex(pin));
//            }
//        };
////        etSend.setText("");
//        if (!DataForwardController.getInstance().isConnectAIDL()) {
//            DataForwardController.getInstance().init(this,
//                    dataReceiveListener, onStatusListener);
//            //密码键盘
//            //dataReceiveListener,onStatusListener,onPinpadListener);
//        }
        getOrders();
    }

    void refreshDate() {
        transactionVos.clear();
        page = 1;
        getOrders();

    }

    @Override
    void initView() {
        listView.setItemsCanFocus(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshDate();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                getOrders();
            }
        });
    }

    @Override
    void bindListener() {
        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 限制最多能输入9位整数
                if (s.toString().contains(".")) {
                    if (s.toString().indexOf(".") > 9) {
                        s = s.toString().subSequence(0, 9) + s.toString().substring(s.toString().indexOf("."));
                        editPrice.setText(s);
                        editPrice.setSelection(9);
                    }
                } else {
                    if (s.toString().length() > 9) {
                        s = s.toString().subSequence(0, 9);
                        editPrice.setText(s);
                        editPrice.setSelection(9);
                    }
                }
                // 判断小数点后只能输入两位
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editPrice.setText(s);
                        editPrice.setSelection(s.length());
                    }
                }
                //如果第一个数字为0，第二个不为点，就不允许输入
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editPrice.setText(s.subSequence(0, 1));
                        editPrice.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editPrice.getText().toString().trim() != null && !editPrice.getText().toString().trim().equals("")) {
                    if (editPrice.getText().toString().trim().substring(0, 1).equals(".")) {
                        editPrice.setText("0" + editPrice.getText().toString().trim());
                        editPrice.setSelection(2);
                    }
                }
            }
        });
    }

    @Override
    void back() {

    }


    public void sendCutPayment(String qrcode, final double price) {
        HttpUtils.sendCutPayment(qrcode, price, new CallBackListener() {
            @Override
            public void onSuccess(ResponseVo responseVo) {
                refreshDate();
                SoundUtils.getInstance().queueSound(new VoiceTemplate()
                        .prefix("success_nonumber")
                        .gen());
                DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(successCode));
            }

            @Override
            public void onFail(ResponseVo responseVo) {
                String reason = responseVo.getMsg();
                if (TextUtils.isEmpty(reason)) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("fail")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(failCode));
                } else if (reason.equals("二维码无效")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("fail_code")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(errTwoCode));
                } else if (reason.equals("余额不足")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("loss_pay")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(lossPayCode));
                } else if (reason.equals("不在消费时段")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("out_paytime")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(outPayCode));
                } else if (reason.equals("不可在该时段重复消费")) {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("repeat_pay")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(doublePayCode));
                } else if (responseVo.getCode() == 500807) {
                    ToastUtils.show(responseVo.getMsg());
                } else {
                    SoundUtils.getInstance().queueSound(new VoiceTemplate()
                            .prefix("fail")
                            .gen());
                    DataForwardController.getInstance().sendport(0, BytesUtils.hexStringToBytes(failCode));
                }
                ToastUtils.show(responseVo.getMsg());
            }
        });
    }

    class q {
        String deviceSn;

        public q(String deviceSn) {
            this.deviceSn = deviceSn;
        }

        public q() {
        }
    }

    public void getOrders() {
        try {
            HttpUtils.getOrders(new q(Constants.getTerminalCode()), page, pageSize, new CallBackListener() {
                @Override
                public void onSuccess(ResponseVo responseVo) {
                    if (responseVo.getData() instanceof TransactionPage) {
                        TransactionPage transactionPage = (TransactionPage) responseVo.getData();
                        transactionVos.addAll(transactionPage.getResultItems());
                        if (transactionListAdapter == null) {
                            transactionListAdapter = new TransactionListAdapter(transactionVos);
                            listView.setAdapter(transactionListAdapter);
                        }
                        transactionListAdapter.notifyDataSetChanged();
                    }
                    refreshLayout.finishRefreshing();
                    refreshLayout.finishLoadmore();
                }

                @Override
                public void onFail(ResponseVo responseVo) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.finishRefreshing();
                            refreshLayout.finishLoadmore();
                            page--;
                        }
                    });
                }
            });
            page++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @OnClick(R.id.confim_button)
    public void onClick(View view) {
        try {
            super.onClick(view);
        } catch (Exception e) {
            return;
        }
        if (view.getId() == R.id.confim_button) {
            String priceString = editPrice.getText().toString();
            if (!TextUtils.isEmpty(priceString)) {
                try {
                    price = Double.valueOf(priceString);
                    showPrice.setText(StringUtils.getDouble(price));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            editPrice.setText("");
            doSaoMa();
        }

    }

    private void doSaoMa() {
        Intent intent = new Intent(TransactionActivity.this, ScanViewActivity.class);
        intent.putExtra("scanType", 0x00);
        intent.putExtra("price", price);
//        context.startActivity(intent);
//        Intent intent = new Intent(TransactionActivity.this, SanMaActivity.class);
//        intent.putExtra("price", price);
        startActivityForResult(intent, 111);
    }

    private void doCard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) return;
                ModuleType[] openReaders = new ModuleType[]{ModuleType.COMMON_RFCARDREADER};
                S_DeviceInfo.getInstance().getCardRead().openCardReader(openReaders, false, 60, TimeUnit.SECONDS, new DeviceEventListener<K21CardReaderEvent>() {
                    @Override
                    public void onEvent(K21CardReaderEvent openCardReaderEvent, Handler handler) {
                        OpenCardReaderResult cardResult = openCardReaderEvent.getOpenCardReaderResult();
                        CommonCardType[] openedModuleTypes = null;
                        if (openCardReaderEvent.isFailed()) {
                            if (openCardReaderEvent.getException() instanceof ProcessTimeoutException) {
                                ToastUtils.showMessage(getString(R.string.msg_timeout) + "\r\n", MessageTag.NORMAL);
                            }
                            ToastUtils.showMessage(getString(R.string.msg_reader_open_failed) + "\r\n", MessageTag.NORMAL);
                        }
                        if (cardResult == null || (openedModuleTypes = cardResult.getResponseCardTypes()) == null || openedModuleTypes.length <= 0) {
                            ToastUtils.showMessage(getString(R.string.msg_card_info_null) + "\r\n", MessageTag.ERROR);
                        } else if (openCardReaderEvent.isSuccess()) {
                            String showMsg = TestActivity.doRead();
                            SoundPoolImpl.getInstance().play();
                            if (!TextUtils.isEmpty(showMsg) && showMsg.length() > 18) {
                                while (showMsg.startsWith("0")) {
                                    showMsg = showMsg.substring(1);
                                }
                                String code = showMsg.substring(0, 17);
                                doSaoma("dw00" + code);
                            }
//                            switch (openCardReaderEvent.getOpenCardReaderResult().getResponseCardTypes()[0]) {
//                                case MSCARD:
//                                    showMsg = context.getString(R.string.msg_cardreader_swiper);
//                                    break;
//                                case ICCARD:
//                                    showMsg = context.getString(R.string.msg_cradreader_insert);
//                                    break;
//                                case RFCARD:
//                                    RFCardType rfCardType = openCardReaderEvent.getOpenCardReaderResult().getResponseRFCardType();
//                                    if (null == rfCardType) {
//                                        showMsg = context.getString(R.string.msg_cardreader_rfcard);
//                                        break;
//                                    }
//                                    switch (rfCardType) {
//                                        case ACARD:
//                                        case BCARD:
//                                            showMsg = context.getString(R.string.msg_cardreader_rf_cpu);
//                                            break;
//                                        case M1CARD:
//                                            byte sak = openCardReaderEvent.getOpenCardReaderResult().getSAK();
//                                            if (sak == 0x08) {
//                                                showMsg = context.getString(R.string.msg_cardreader_rf_s50);
//                                            } else if (sak == 0x18) {
//                                                showMsg = context.getString(R.string.msg_cardreader_rf_s70);
//                                            } else if (sak == 0x28) {
//                                                showMsg = context.getString(R.string.msg_cardreader_rf_s50_pro);
//                                            } else if (sak == 0x38) {
//                                                showMsg = context.getString(R.string.msg_cardreader_rf_s70_pro);
//                                            } else {
//                                                showMsg = "sak=" + sak;
//                                                showMsg = showMsg + context.getString(R.string.msg_cardreader_undefind);
//                                            }
//                                            break;
//                                        default:
//                                            showMsg = context.getString(R.string.msg_cardreader_undefind_rf);
//                                            break;
//                                    }
//                                    break;
//                                default:
//                                    break;
//                            }
//                            long endTime = System.currentTimeMillis();
                            ToastUtils.showMessage(showMsg + "\r\n", MessageTag.NORMAL);
                        } else if (openCardReaderEvent.isUserCanceled()) {
                            ToastUtils.showMessage(getString(R.string.msg_cancel_open_reader) + "\r\n", MessageTag.NORMAL);
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            doCard();
                        }
                    }

                    @Override
                    public Handler getUIHandler() {
                        return null;
                    }
                }, SearchCardRule.RFCARD_FIRST);
            }
        }).start();

    }
    @Override
    protected void onDestroy() {
        DataForwardController.getInstance().release(this);
        super.onDestroy();
    }
}
