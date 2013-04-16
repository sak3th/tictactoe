package com.tictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class GcmMessageReceiver extends ResultReceiver {
    
    private Receiver mReceiver;

    public GcmMessageReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }else {
            Log.e(TicTacToeGlobals.TAG, "GCM message missed");
        }
    }
    
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }
 
    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }
}
