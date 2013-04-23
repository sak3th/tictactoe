package com.tictactoe.globals;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    private static final String PREFS_NAME = "TicTacToePrefs";
    
    public static final String DEVICE_ID = "device_id";
    public static final String GCM_REG_ID = "gcm_reg_id";
    
    public static final String REG_EMAIL = "reg_email";

    public static void set(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    public static void resetToNull(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, null);
        editor.commit();
    }
    
    public static String get(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, null);
    }
    

}
