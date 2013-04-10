package com.tictactoe;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.tictactoe.AccountsFragment.AccountsListener;
import com.tictactoe.GetPlayerAsyncTask.PlayerInfoListener;
import com.tictactoe.GetTokenAsyncTask.GetTokenListener;
import com.tictactoe.WelcomeFragment.LoginListener;
import com.tictactoe.model.Player;

public class HomeActivity extends Activity 
        implements LoginListener, GetTokenListener, AccountsListener, PlayerInfoListener {
    
    private static final String TAG = HomeActivity.class.getSimpleName();
    
    private static final int USER_INPUT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		if(findViewById(R.id.home_fragment_container) != null) {
			if(savedInstanceState != null) {
				return;
			}			
		}
		
		WelcomeFragment welcomeFgmt = new WelcomeFragment();
		getFragmentManager().beginTransaction().add(
				R.id.home_fragment_container, welcomeFgmt).commit();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(requestCode == USER_INPUT) {
            getGoogleAuthToken(TicTacToeGlobals.getInstance().getAccount());
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
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
            AccountsFragment fgmt = new AccountsFragment();
            Bundle args = new Bundle();
            fgmt.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.home_fragment_container, fgmt);
            transaction.addToBackStack(null);
            
            transaction.commit();   
            break;
        }
	}
	
	@Override
	public void onLoginSkipped() {
	    // TODO add functionality for guest player 
	}

    @Override
    public void onUserInputRequired(Intent intent) {
        startActivityForResult(intent, USER_INPUT);
    }

    @Override
    public void onTokenAcquired(Boolean result) {
        if(result) {
            new GetPlayerAsyncTask(this).execute(TicTacToeGlobals.APPENGINE_URI + "/reception");
        }
    }

    @Override
    public void onAccountSelected(Account account) {
        TicTacToeGlobals.getInstance().setAccount(account);
        getGoogleAuthToken(account);
    }

    @Override
    public void onPlayerInfoAcquired(Player player) {
        Log.d(TAG, "Acquired player info - " + new Gson().toJson(player) );
        TicTacToeGlobals.getInstance().setPlayer(player);
        GCMIntentService.register(getApplicationContext());
    }
    
    private void getGoogleAuthToken(Account account) {
        new GetTokenAsyncTask(getApplicationContext(), this).execute(account);
    }

}
