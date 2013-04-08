package com.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    public static final String PREFS_NAME = "TicTacToePrefs";

    public static void storeSharedPref(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    public static String getSharedPref(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, null);
    }
    

}
