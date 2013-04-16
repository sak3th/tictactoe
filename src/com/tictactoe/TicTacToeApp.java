package com.tictactoe;

import android.app.Application;

public class TicTacToeApp extends Application {

    TicTacToeGlobals mTicTacToeGlobals;
    
    @Override
    public void onCreate() {
        mTicTacToeGlobals = new TicTacToeGlobals(this);
        mTicTacToeGlobals.onCreate();
    }
}