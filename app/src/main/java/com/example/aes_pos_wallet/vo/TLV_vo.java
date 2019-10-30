package com.example.aes_pos_wallet.vo;

import android.text.TextUtils;

public class TLV_vo {
    private String tag;
    private String length;
    private String value;

    public String toString() {
        return tag + length + value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private TLV_vo() {
    }

    public static TLV_vo getVo(String tag, String value) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(value)) return null;
        TLV_vo tlv_vo = new TLV_vo();
        if (value.length() % 2 != 0) value += "F";
        tlv_vo.setTag(tag);
        String length = Integer.toHexString(value.length() / 2);
        while (length.length() < 2) {
            length = "0" + length;
        }
        tlv_vo.setLength(length);
        tlv_vo.setValue(value);
        return tlv_vo;
    }
}
