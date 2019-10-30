package com.example.aes_pos_wallet.vo.unionVo;

import android.text.TextUtils;

import com.example.aes_pos_wallet.utils.HexUtil;
import com.example.aes_pos_wallet.vo.TLV_vo;


public class UnionVoCode {

    private String payCode = "ADD01";
    private String reciveCode = "ADD02";
    TLV_vo top;
    TLV_vo value;


    public static UnionVoCode getVoByCode(String code) {
        UnionVoCode unionVoCode = new UnionVoCode();
        if (code.length() % 2 != 0) return null;
        String[] codes = new String[code.length() / 2];
        for (int i = 0; i < codes.length; i++) {
            codes[i] = code.substring(0, 2);
            code = code.substring(2);
        }
        byte type = 0x00;
        String tag = "";
        int length = 0;
        String value = "";
        for (int j = 0; j < codes.length; j++) {
            String codeItem = codes[j];
            switch (type) {
                case 0x00:
                    codeItem = codeItem.toUpperCase();
                    tag += codeItem;
                    if (codeItem.endsWith("F") && Integer.parseInt("" + codeItem.charAt(0), 16) % 2 != 0) {
                        type = 0x00;
                    } else {
                        type = 0x01;
                    }
                    break;
                case 0x01:
                    length = Integer.parseInt(codeItem, 16);
                    type = 0x02;
                    break;
                case 0x02:
                    value += codeItem;
                    length--;
                    if (length == 0) {
                        if (tag.equals("85")) {
                            unionVoCode.setTop(TLV_vo.getVo(tag, value));
                        } else if (tag.equals("61")) {
                            unionVoCode.setValue(TLV_vo.getVo(tag, value));
                        }
                        type = 0x00;
                        tag = "";
                        value = "";
                    }
                    break;
            }
        }
        return unionVoCode;
    }


    public String toString() {
        return android.util.Base64.encodeToString(HexUtil.hexToBytes(top.toString() + value.toString()), android.util.Base64.NO_WRAP);
    }

    public TLV_vo getTop() {
        return top;
    }

    public void setTop(TLV_vo top) {
        this.top = top;
    }

    public TLV_vo getValue() {
        return value;
    }

    public void setValue(TLV_vo value) {
        this.value = value;
    }

    private UnionVoCode() {

    }

    public static final UnionVoCode getVo(String amount, String token, String type) {
        if(TextUtils.isEmpty(amount))amount="00";
        UnionVoCode unionVoCode = new UnionVoCode();
        unionVoCode.setTop(TLV_vo.getVo("85", "4350563031"));
        unionVoCode.setValue(TLV_vo.getVo("61", UnionCodeValue.getVo(amount, token, type).toString()));
        return unionVoCode;
    }
}
