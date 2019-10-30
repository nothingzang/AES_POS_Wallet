package com.example.aes_pos_wallet.vo.unionVo;


import com.example.aes_pos_wallet.utils.StringUtils;
import com.example.aes_pos_wallet.vo.TLV_vo;

public class UnionCodeValue {
    TLV_vo type;//类型
    TLV_vo token;//token
    TLV_vo otherValue;//银联参数
    TLV_vo number;//序列号

    public String toString() {
        return type.toString() + token.toString() + otherValue.toString() + number.toString();
    }

    public String getTokenCode() {
        String[] dValue = token.getValue().toUpperCase().split("D");
        if (dValue != null && dValue.length > 0) {
            return dValue[0];
        } else return "";
    }

    public String getAmount() {
        String[] dValue = token.getValue().toUpperCase().split("D");
        String amount = "00";
        if (dValue != null && dValue.length > 3 && dValue[3].length() > 0) {
            try {
                double amount_c = Double.parseDouble(dValue[3].toUpperCase().endsWith("F") ? dValue[3].toUpperCase().replace("F", "") : dValue[3]);
                amount_c = amount_c / 100;
                amount = StringUtils.getDouble(amount_c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return amount;
    }

    public boolean isPayCode() {
        String[] dValue = token.getValue().toUpperCase().split("D");
        if (dValue != null && dValue.length > 2) {
            return dValue[2].equals("01");
        } else return false;
    }

    public boolean isReciveCode() {
        String[] dValue = token.getValue().toUpperCase().split("D");
        if (dValue != null && dValue.length > 2) {
            return dValue[2].equals("02");
        } else return false;
    }

    private UnionCodeValue() {

    }

    public static UnionCodeValue getVoByCode(String code) {
        UnionCodeValue unionVoCode = new UnionCodeValue();
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
                    tag = tag.toUpperCase();
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
                        if (tag.equals("4F")) {
                            unionVoCode.setType(TLV_vo.getVo(tag, value));
                        } else if (tag.equals("57")) {
                            unionVoCode.setToken(TLV_vo.getVo(tag, value));
                        } else if (tag.equals("63")) {
                            unionVoCode.setOtherValue(TLV_vo.getVo(tag, value));
                        } else if (tag.equals("5F34")) {
                            unionVoCode.setNumber(TLV_vo.getVo(tag, value));
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

    public static UnionCodeValue getVo(String amount, String token, String type) {
        UnionCodeValue unionCodeValue = new UnionCodeValue();
        unionCodeValue.setType(TLV_vo.getVo("4F", "A000000333010103"));
        unionCodeValue.setToken(TLV_vo.getVo("57", token + "D5012201D" + type+"D"+amount));
        unionCodeValue.setOtherValue(TLV_vo.getVo("63", UnionCodeOtherValue.getVo().toString()));
        unionCodeValue.setNumber(TLV_vo.getVo("5F34", "01"));
        return unionCodeValue;
    }

    public TLV_vo getType() {
        return type;
    }

    public void setType(TLV_vo type) {
        this.type = type;
    }

    public TLV_vo getToken() {
        return token;
    }

    public void setToken(TLV_vo token) {
        this.token = token;
    }

    public TLV_vo getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(TLV_vo otherValue) {
        this.otherValue = otherValue;
    }

    public TLV_vo getNumber() {
        return number;
    }

    public void setNumber(TLV_vo number) {
        this.number = number;
    }
}
