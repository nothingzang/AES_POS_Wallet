package com.example.aes_pos_wallet.activitys;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.aes_pos_wallet.R;
import com.example.aes_pos_wallet.constants.Constants;
import com.example.aes_pos_wallet.device.S_DeviceInfo;
import com.example.aes_pos_wallet.listeners.CallBackListener;
import com.example.aes_pos_wallet.response.ResponseVo;
import com.example.aes_pos_wallet.utils.HttpUtils;
import com.example.aes_pos_wallet.utils.StringUtils;
import com.example.aes_pos_wallet.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.et_password)
    EditText passwordEdit;
    @BindView(R.id.login_button)
    Button login_button;


//    private void showDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("物理键盘授权")
//                .setMessage("外接物理键盘读取未授权").setPositiveButton("去授权", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        AccessibilityUtil.jumpToSetting(LoginActivity.this);
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(LoginActivity.this, "未授权外接物理键盘读取，物理键盘无法使用", Toast.LENGTH_LONG).show();
//                        dialogInterface.dismiss();
//                    }
//                });
//        builder.create().show();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    int getLayoutId() {
        return R.layout.activity_login;
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

    //
    @OnClick({R.id.login_button})
    public void onClick(View view) {
        try {
            super.onClick(view);
        } catch (DoubleClickExcetion doubleClickExcetion) {
            doubleClickExcetion.printStackTrace();
            return;
        }
        switch (view.getId()) {
            case R.id.login_button:
                String name = phoneEdit.getText().toString().trim();
                String passWord = passwordEdit.getText().toString().trim();
                if (!StringUtils.isPhoneNumber(name)) {
                    ToastUtils.show("请检查账号");
                    return;
                }
                if (!StringUtils.isPhoneNumber(passWord)) {
                    ToastUtils.show("请检查密码");
                    return;
                }
                try {
                    if (TextUtils.isEmpty(Constants.getTerminalCode())) {
                        String sn = S_DeviceInfo.getInstance().getDevice().getDeviceInfo().getSN();
                        Constants.updateSN(sn);
                        System.out.println("设备编号：" + sn);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "正在初始化服务，请稍等重试", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                login(name, StringUtils.getMdPassWord(passWord));
                break;
        }
    }


    public void login(String loginName, String loginPwd) {
        HttpUtils.login(loginName, loginPwd, new CallBackListener() {
            @Override
            public void onSuccess(ResponseVo responseVo) {
                Constants.updateToken(responseVo.getData().toString());
                startActivity(TransactionActivity.class);
                finish();
            }

            @Override
            public void onFail(ResponseVo responseVo) {
                ToastUtils.show(responseVo.getMsg());
            }
        });
    }
}
