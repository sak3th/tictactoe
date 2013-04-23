package com.tictactoe.globals;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tictactoe.gcm.GcmMessageReceiver;
import com.tictactoe.gcm.GcmMessageReceiver.Receiver;
import com.tictactoe.model.Game;
import com.tictactoe.model.Player;

public class TicTacToeGlobals extends ContextWrapper {

    public static final String TAG = TicTacToeApp.class.getSimpleName();

    // server urls
    public static final String DEVELOPMENT_URI = "http://10.0.1.10:8888";
    public static final String PRODUCTION_URI = "http://kattam-tictactoe.appspot.com";
    //public static final String APPENGINE_URI = TicTacToeGlobals.PRODUCTION_URI;
    public static final String APPENGINE_URI = TicTacToeGlobals.DEVELOPMENT_URI;

    // sender, device ids
    public static final String SENDER_ID = "527416798900";

    // supported logins
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    
    // GCM messages
    public static final int GCM_REGISTRATION_FAILED = 1;
    public static final int GCM_REGISTERED_ON_SERVER = 2;
    public static final int GCM_NOT_REGISTERED_ON_SERVER = 3;


    private static TicTacToeGlobals sMe;

    private Player mPlayer;
    private Account mAccount;
    
    private Game[] mGames;
    
    private GcmMessageReceiver mGcmMessageReceiver;

    private DefaultHttpClient mHttpClient;

    public TicTacToeGlobals(Context context) {
        super(context);
        sMe = this;
        mGcmMessageReceiver = new GcmMessageReceiver(mHandler);
    }

    public void onCreate() {
        // TODO perform any global initializations here
        mHttpClient = new DefaultHttpClient();
        // Don't follow redirects
        mHttpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
    }

    public static TicTacToeGlobals getInstance() {
        if (sMe == null) {
            throw new IllegalStateException("No TicTacToeGlobals here!");
        }
        return sMe;
    }
    
    public void setGcmMessageReceiver(Receiver receiver) {
        mGcmMessageReceiver.setReceiver(receiver);
    }
    
    public GcmMessageReceiver getGcmMessageReceiver() {
        return mGcmMessageReceiver;
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
    
    public Game[] getGames() {
        return mGames;
    }

    public void setGames(Game[] games) {
        mGames = games;
    }

    public DefaultHttpClient getHttpCient() {
        return mHttpClient;
    }

    public static void todoToast(String toast) {
        Toast.makeText(sMe, "// TODO " + toast, Toast.LENGTH_SHORT).show();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            
        }
    };

}
