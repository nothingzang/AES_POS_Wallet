package com.example.aes_pos_wallet.activitys;


import android.view.View;
import android.widget.Button;

import com.example.aes_pos_wallet.R;
import com.example.aes_pos_wallet.device.S_DeviceInfo;
import com.example.aes_pos_wallet.utils.MessageTag;
import com.example.aes_pos_wallet.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.button5)
    Button button;

    @Override
    int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    void initDate() {

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

    @OnClick({R.id.button5, R.id.button6})
    public void onClick(View view) {
        try {
            super.onClick(view);
        } catch (DoubleClickExcetion doubleClickExcetion) {
            doubleClickExcetion.printStackTrace();
        }
        switch (view.getId()) {
            case R.id.button5:
                if (S_DeviceInfo.getInstance().isConnectDevice())
                    startActivity(TestActivity.class);
                else
                    ToastUtils.showMessage(MainActivity.this.getString(R.string.msg_device_conn_first), MessageTag.TIP);
                break;
            case R.id.button6:
                S_DeviceInfo.getInstance().connectDevice();
                break;
        }

    }
}
