package com.tictactoe;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Toast;

import com.tictactoe.model.Player;

public class TicTacToeGlobals extends ContextWrapper {

    public static final String TAG = TicTacToeApp.class.getSimpleName();

    // server urls
    public static final String DEVELOPMENT_URI = "http://192.168.1.133:8888";
    public static final String PRODUCTION_URI = "http://kattam-tictactoe.appspot.com";
    //public static final String APPENGINE_URI = TicTacToeGlobals.PRODUCTION_URI;
    public static final String APPENGINE_URI = TicTacToeGlobals.DEVELOPMENT_URI;

    // sender, device ids
    public static final String SENDER_ID = "527416798900";
    
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";

    private static TicTacToeGlobals sMe;

    private Player mPlayer;
    private Account mAccount;

    private DefaultHttpClient mHttpClient;

    public TicTacToeGlobals(Context context) {
        super(context);
        sMe = this;
    }

    public void onCreate() {
        // TODO perform any global initializations here
        mHttpClient = new DefaultHttpClient();
        // Don't follow redirects
        mHttpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
    }

    static TicTacToeGlobals getInstance() {
        if (sMe == null) {
            throw new IllegalStateException("No TicTacToeGlobals here!");
        }
        return sMe;
    }

    public void setPlayer(Player player) {
        mPlayer = player;
    }

    public Player getPlayer() { 
        return mPlayer;
    }

    public Account getAccount() {
        return mAccount;
    }

    public void setAccount(Account account) {
        mAccount = account;
    }

    public DefaultHttpClient getHttpCient() {
        return mHttpClient;
    }

    public static void todoToast(String toast) {
        Toast.makeText(sMe, "// TODO " + toast, Toast.LENGTH_SHORT).show();
    }

}
