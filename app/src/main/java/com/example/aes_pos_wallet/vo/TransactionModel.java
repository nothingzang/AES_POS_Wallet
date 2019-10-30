package com.example.aes_pos_wallet.vo;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;


import com.example.aes_pos_wallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nothing on 2019/7/31.
 */

public class TransactionModel {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.time_day)
    TextView time_day;
    @BindView(R.id.change_type)
    TextView change_type;
    @BindView(R.id.number_icon)
    TextView number_icon;

    public TransactionModel(View itemView) {
        ButterKnife.bind(this, itemView);
    }

    public void initView(final TransactionVo transactionVo) {
        try {
            if (transactionVo.getOrderType().equals("consume")) {
                time_day.setTextColor(Color.parseColor("#2A9EFA"));
                time_day.setText("收款单：" + transactionVo.getOrderNo());
            } else if (transactionVo.getOrderType().equals("refund")) {
                time_day.setTextColor(Color.RED);
                time_day.setText("退款单：" + transactionVo.getOrderNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            time_day.setText(transactionVo.getFinishTime());
        }
        if (transactionVo.getOrderType().equals(TransactionVo.ORDER_TYPE_REFUND)) {
            change_type.setText("-");
            number_icon.setTextColor(Color.parseColor("#999999"));
            change_type.setTextColor(Color.parseColor("#333333"));
            number.setTextColor(Color.parseColor("#333333"));
            name.setText("收款方：" + transactionVo.getReceiverShowName());
        } else if (transactionVo.getOrderType().equals(TransactionVo.ORDER_TYPE_RECHARGE)) {
            change_type.setText("+");
            number_icon.setTextColor(Color.parseColor("#B7E0FB"));
            change_type.setTextColor(Color.parseColor("#2A9EFA"));
            number.setTextColor(Color.parseColor("#00A0FF"));
            name.setText("付款方：" + transactionVo.getSendShowName());
        } else if (transactionVo.getOrderType().equals(TransactionVo.ORDER_TYPE_CONSUME)) {
            change_type.setText("+");
            number_icon.setTextColor(Color.parseColor("#B7E0FB"));
            change_type.setTextColor(Color.parseColor("#2A9EFA"));
            number.setTextColor(Color.parseColor("#00A0FF"));
            name.setText("付款方：" + transactionVo.getSendShowName());
//            }
        } else if (transactionVo.getOrderType().equals(TransactionVo.ORDER_TYPE_TRANSFER)) {
            change_type.setText("+");
            number_icon.setTextColor(Color.parseColor("#B7E0FB"));
            change_type.setTextColor(Color.parseColor("#2A9EFA"));
            number.setTextColor(Color.parseColor("#00A0FF"));
            name.setText(transactionVo.getReceiverShowName());
        }
        number.setText("" + transactionVo.getPayAmount());
        time.setText(transactionVo.getFinishTime());
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (transactionVo.getOrderType().equals("consume")) {
//                    RefundPriceActivity.start(MainActivity.getInstance(), transactionVo);
//                }
//            }
//        });
    }
}
