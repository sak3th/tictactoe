package com.tictactoe;

import com.tictactoe.model.Player;

import android.app.Application;

public class TicTacToe extends Application {

    public static final String DEVELOPMENT_URI = "http://192.168.1.106:8888";
    public static final String PRODUCTION_URI = "http://kattam-tictactoe.appspot.com";    
    public static final String APPENGINE_URI = PRODUCTION_URI;

    Player mPlayer;

    public void setPlayer(Player player) {
        mPlayer = player;
    }

    public Player getPlayer() { 
        return mPlayer;
    }

}
