package com.tictactoe;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.tictactoe.AccountsFragment.AccountsListener;
import com.tictactoe.WelcomeFragment.LoginListener;
import com.tictactoe.appengine.GetPlayerAsyncTask;
import com.tictactoe.appengine.GetPlayerAsyncTask.PlayerInfoListener;
import com.tictactoe.appengine.GetTokenAsyncTask;
import com.tictactoe.appengine.GetTokenAsyncTask.GetTokenListener;
import com.tictactoe.gcm.GCMIntentService;
import com.tictactoe.gcm.GcmMessageReceiver;
import com.tictactoe.gcm.GcmMessageReceiver.Receiver;
import com.tictactoe.globals.Storage;
import com.tictactoe.globals.TicTacToeGlobals;
import com.tictactoe.model.Player;

public class HomeActivity extends Activity 
		implements LoginListener, GetTokenListener, AccountsListener, 
					PlayerInfoListener, Receiver {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private static final String FRAGMENT_WELCOME_TAG = "fragment_welcome_tag";
    private static final String FRAGMENT_ACCOUNTS_TAG = "fragment_accounts_tag";
    private static final String FRAGMENT_GAME_TAG = "fragment_game_tag";


    private static final int ACTIVITY_USER_INPUT = 1;

    private ProgressBar mProgressBar;
    private WelcomeFragment mWelcomeFragment;
    private AccountsFragment mAccountsFragment;

    private GcmMessageReceiver mGcmMsgReceiver; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	setContentView(R.layout.activity_home);
    	mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    	if(findViewById(R.id.home_fragment_container) != null) {
    		if(savedInstanceState != null) {
    			return;
    		}			
    	}

    	mWelcomeFragment = new WelcomeFragment();
    	mAccountsFragment = new AccountsFragment();

    	TicTacToeGlobals.getInstance().setGcmMessageReceiver(this);

    	if(Storage.get(getApplicationContext(), Storage.GCM_REG_ID) == null) {
    		Log.d(TAG, "showing welcome fragment");
    		showWelcomeFragment();
    	} else {
    		if(GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
    			Log.d(TAG, "already registered on server");
    			// TODO what if the account that the user registers with is not available anymore
    			showGameActivity();
    		} else {
    			Log.d(TAG, "not registered on server");
    		}
    	}
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_USER_INPUT) {
            getGoogleAuthToken(TicTacToeGlobals.getInstance().getAccount());
        }
    }

    @Override
    public void onLoginAllowed() {
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType(TicTacToeGlobals.GOOGLE_ACCOUNT_TYPE);
        switch (accounts.length) {
        case 0:
            // no google account present in this device
            // TODO launch 'add google account' activity here
            break;
        case 1:
            TicTacToeGlobals.getInstance().setAccount(accounts[0]);
            getGoogleAuthToken(accounts[0]);
            break;
        default:
            showAccountsFragment();
            break;
        }
    }

    @Override
    public void onLoginSkipped() {
        // TODO add functionality for guest player
        getFragmentManager().beginTransaction().remove(mWelcomeFragment).commit();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUserInputRequired(Intent intent) {
        startActivityForResult(intent, ACTIVITY_USER_INPUT);
    }

    @Override
    public void onTokenAcquired(Boolean result) {
        if(result) {
            new GetPlayerAsyncTask(this).execute(TicTacToeGlobals.APPENGINE_URI + "/reception");
        }
    }

    @Override
    public void onAccountSelected(Account account) {
        Storage.set(getApplicationContext(), Storage.REG_EMAIL, account.name);
        TicTacToeGlobals.getInstance().setAccount(account);
        getFragmentManager().beginTransaction().remove(mAccountsFragment).commit();
        mProgressBar.setVisibility(View.VISIBLE);
        getGoogleAuthToken(account);
    }

    @Override
    public void onPlayerInfoAcquired(Player player) {
        Log.d(TAG, "Acquired player info - " + new Gson().toJson(player) );
        TicTacToeGlobals.getInstance().setPlayer(player);
        GCMIntentService.register(getApplicationContext());
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        mProgressBar.setVisibility(View.INVISIBLE);
        switch (resultCode) {
        case TicTacToeGlobals.GCM_REGISTERED_ON_SERVER:
            showGameActivity();
            break;
        case TicTacToeGlobals.GCM_NOT_REGISTERED_ON_SERVER:
            // TODO allow only AI games
            showGameActivity();
        case TicTacToeGlobals.GCM_REGISTRATION_FAILED:
            // TODO allow only AI games
            showGameActivity();
            break;
        default:
            break;
        }
    }

    private void showWelcomeFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // TODO add animations using transaction.setCustomAnimations() api
        transaction.add(R.id.home_fragment_container, mWelcomeFragment, FRAGMENT_WELCOME_TAG);
        transaction.commit();
    }

    private void showAccountsFragment() {
        Bundle args = new Bundle();
        mAccountsFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // TODO add animations using transaction.setCustomAnimations() api

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.home_fragment_container, mAccountsFragment, FRAGMENT_ACCOUNTS_TAG);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void showGameActivity() {
        FragmentManager fm = getFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {    
            fm.popBackStack();
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // TODO add animations using transaction.setCustomAnimations() api
        transaction.commit();
        finish();
        startActivity(new Intent(getApplicationContext(), GameKeeperActivity.class));
    }

    //    private Fragment getActiveFragment() {
    //        if (getFragmentManager().getBackStackEntryCount() == 0) {
    //            return null;
    //        }
    //        String tag = getFragmentManager().
    //                getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
    //        return (Fragment) getFragmentManager().findFragmentByTag(tag);
    //    }

    private void getGoogleAuthToken(Account account) {
        new GetTokenAsyncTask(getApplicationContext(), this).execute(account);
    }
}
