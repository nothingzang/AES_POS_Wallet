package com.example.aes_pos_wallet.listeners;


import java.util.HashMap;
import java.util.Map;

public abstract class KeyActionListener {

    private Map<Integer, Character> keymap;

    public Map<Integer, Character> getKeymap() {
        return keymap;
    }

    private boolean isCaps = false;

    StringBuffer stringBuffer = new StringBuffer();

    public void onKey(int action) {
        if (getKeymap() == null) {
            initMap();
        }
        if (action == 59) {
            isCaps = true;
        } else if (action == 66) {
            String code = stringBuffer.toString();
            stringBuffer = new StringBuffer();
            doOnce(code);
        } else if (getKeymap().keySet().contains(action)) {
            String number = "";
            if (isCaps) {
                if (action == 74) number += ":";
                else if (action == 75) number += "\"";
                else if (action == 71) number += "{";
                else if (action == 72) number += "}";
                else
                    number += ("" + getKeymap().get(action)).toUpperCase();
            } else {
                number += ("" + getKeymap().get(action));
            }
            isCaps = false;
            stringBuffer.append(number);
        }
    }

    public abstract void doOnce(String code);

    //59 大写 66 输出
    private void initMap() {
        if (keymap == null) keymap = new HashMap<>();
        keymap.put(7, '0');
        keymap.put(8, '1');
        keymap.put(9, '2');
        keymap.put(10, '3');
        keymap.put(11, '4');
        keymap.put(12, '5');
        keymap.put(13, '6');
        keymap.put(14, '7');
        keymap.put(15, '8');
        keymap.put(16, '9');
        keymap.put(29, 'a');
        keymap.put(30, 'b');
        keymap.put(31, 'c');
        keymap.put(32, 'd');
        keymap.put(33, 'e');
        keymap.put(34, 'f');
        keymap.put(35, 'g');
        keymap.put(36, 'h');
        keymap.put(37, 'i');
        keymap.put(38, 'j');
        keymap.put(39, 'k');
        keymap.put(40, 'l');
        keymap.put(41, 'm');
        keymap.put(42, 'n');
        keymap.put(43, 'o');
        keymap.put(44, 'p');
        keymap.put(45, 'q');
        keymap.put(46, 'r');
        keymap.put(47, 's');
        keymap.put(48, 't');
        keymap.put(49, 'u');
        keymap.put(50, 'v');
        keymap.put(51, 'w');
        keymap.put(52, 'x');
        keymap.put(53, 'y');
        keymap.put(54, 'z');
        keymap.put(55, ',');
        keymap.put(68, '`');
        keymap.put(69, '-');
        keymap.put(70, '=');
        keymap.put(71, '[');
        keymap.put(72, ']');
        keymap.put(73, '\\');
        keymap.put(74, ';');
        keymap.put(75, '\'');
        keymap.put(76, '/');
        keymap.put(77, '@');
    }


}