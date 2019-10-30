package com.example.aes_pos_wallet.listeners;

public interface PermissionCallBack {
    void accecpt();

    void unAccecpt(boolean isClose);

    /**
     * android.permission-group.CALENDAR
     *                           - android.permission.READ_CALENDAR
     *                           - android.permission.WRITE_CALENDAR
     *
     * android.permission-group.CALL_LOG
     *                           - android.permission.READ_CALL_LOG
     *                           - android.permission.WRITE_CALL_LOG
     *                           - android.permission.PROCESS_OUTGOING_CALLS
     *
     * android.permission-group.CAMERA
     *                           - android.permission.CAMERA
     *
     * android.permission-group.CONTACTS
     *                           - android.permission.READ_CONTACTS
     *                           - android.permission.WRITE_CONTACTS
     *                           - android.permission.GET_ACCOUNTS
     *
     * android.permission-group.LOCATION
     *                           - android.permission.ACCESS_FINE_LOCATION
     *                           - android.permission.ACCESS_COARSE_LOCATION
     *
     * android.permission-group.MICROPHONE
     *                           - android.permission.RECORD_AUDIO
     *
     * android.permission-group.PHONE
     *                           - android.permission.READ_PHONE_STATE
     *                           - android.permission.READ_PHONE_NUMBERS
     *                           - android.permission.CALL_PHONE
     *                           - android.permission.ANSWER_PHONE_CALLS
     *                           - com.android.voicemail.permission.ADD_VOICEMAIL
     *                           - android.permission.USE_SIP
     *
     * android.permission-group.SENSORS
     *                           - android.permission.BODY_SENSORS
     *
     * android.permission-group.SMS
     *                           - android.permission.SEND_SMS
     *                           - android.permission.RECEIVE_SMS
     *                           - android.permission.READ_SMS
     *                           - android.permission.RECEIVE_WAP_PUSH
     *                           - android.permission.RECEIVE_MMS
     *
     * android.permission-group.STORAGE
     *                           - android.permission.READ_EXTERNAL_STORAGE
     *                           - android.permission.WRITE_EXTERNAL_STORAGE
     * ---------------------
     *
     *
     */
}
