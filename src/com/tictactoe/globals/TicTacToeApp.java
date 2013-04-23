package com.tictactoe.globals;

import android.app.Application;

public class TicTacToeApp extends Application {

    TicTacToeGlobals mTicTacToeGlobals;
    
    @Override
    public void onCreate() {
        mTicTacToeGlobals = new TicTacToeGlobals(this);
        mTicTacToeGlobals.onCreate();
    }
}