package com.example.aes_pos_wallet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.example.aes_pos_wallet.R;
import com.example.aes_pos_wallet.vo.TransactionModel;
import com.example.aes_pos_wallet.vo.TransactionVo;

import java.util.List;

/**
 * Created by Nothing on 2019/7/31.
 */

public class TransactionListAdapter extends BaseAdapter {


    private List<TransactionVo> transactionVos;


    public TransactionListAdapter(List<TransactionVo> transactionVos) {
        this.transactionVos = transactionVos;
    }


    @Override
    public int getCount() {
        return transactionVos.size();
    }

    @Override
    public Object getItem(int position) {
        return transactionVos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transaction_item, parent, false);
        (new TransactionModel(v)).initView(transactionVos.get(position));
        return v;
    }
}
