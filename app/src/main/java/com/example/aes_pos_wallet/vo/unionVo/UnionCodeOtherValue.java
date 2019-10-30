package com.example.aes_pos_wallet.vo.unionVo;


import com.example.aes_pos_wallet.vo.TLV_vo;

public class UnionCodeOtherValue {
    TLV_vo type = TLV_vo.getVo("82", "8000");
    TLV_vo iin = TLV_vo.getVo("9F10", "0701010340000001083439393930303133");
    TLV_vo atc = TLV_vo.getVo("9F26", "56E8C69E41B6405C");
    TLV_vo NULL = TLV_vo.getVo("9F27", "80");
    TLV_vo NUMBER = TLV_vo.getVo("9F36", "0000");
    TLV_vo RANDOM = TLV_vo.getVo("9F37", "00000000");

    private UnionCodeOtherValue() {

    }

    public static UnionCodeOtherValue getVo() {
        return new UnionCodeOtherValue();
    }

    public String toString() {
        return type.toString() + iin.toString() + atc.toString() + NULL.toString() + NUMBER.toString() + RANDOM.toString();
    }
}
