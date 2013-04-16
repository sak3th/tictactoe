package com.tictactoe;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    GCMRegistrationListener gcmListener;

    public GCMIntentService() {
        super(TicTacToeGlobals.SENDER_ID);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void register(Context context) {
        GCMRegistrar.checkDevice(context); // checks if device supports GCM
        GCMRegistrar.checkManifest(context); // verifies application manifest meets all requirements
        final String regId = GCMRegistrar.getRegistrationId(context);
        if (regId.equals("")) {
            // TODO what if device does not have reliable data service
            GCMRegistrar.register(context, TicTacToeGlobals.SENDER_ID);
        } else {
            Log.v(TAG, "Already registered");
        }
    }

    public static void unregister(Context mContext) {
        GCMRegistrar.unregister(mContext);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        Storage.storeSharedPref(context, Storage.GCM_REG_ID, registrationId);
        if(ServerUtilities.register(context, registrationId)) {
            TicTacToeGlobals.getInstance().getGcmMessageReceiver().send(0, new Bundle());
        }
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        // TODO pull the playload from the intent
        Log.i(TAG, "Received message from server" + intent.getDataString());
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification. Total - " + total);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

    public interface GCMRegistrationListener {
        public void onGCMRegistered(String regId);
        public void onGCMUnregistered(String regId);
    }
}
