package com.tictactoe;

import com.tictactoe.AccountsFragment.AccountsListener;
import com.tictactoe.WelcomeFragment.OnLoginListener;
import com.tictactoe.model.Player;
import com.tictactoe.model.Response;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

public class HomeActivity extends Activity implements OnLoginListener, AccountsListener {

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	@Override
	public void onLoginSkipped() {
	    // TODO add functionality for guest player 
	}

    @Override
    public void onLoginResponse(Response resp) {
        SigninFragment fgmt = new SigninFragment();
        Bundle args = new Bundle();
        args.putString("url", TicTacToe.APPENGINE_URI + resp.msg);
        fgmt.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.home_fragment_container, fgmt);
        transaction.addToBackStack(null);
        
        transaction.commit();        
    }

    @Override
    public void onLogin(Response resp) {
        /*
         * "status":"OK",
         * "msg":"http://kattam-tictactoe.appspot.com/_ah/logout?continue\u003dhttps://www.google.com/accounts/Logout%3Fcontinue%3Dhttps://appengine.google.com/_ah/logout%253Fcontinue%253Dhttp://kattam-tictactoe.appspot.comv.saketh%252540gmail.com/%26service%3Dah",
         * "obj":{"email":"v.saketh@gmail.com","userId":"103230768538478020324","age":0,"jdoDetachedState":[{"targetClassName":"tictactoe.model.Player","hashCode":-139636265,"keyAsObject":"v.saketh@gmail.com"},null,[1,1,1,1,1],[]]}}
         */
        ((TicTacToe) getApplication()).setPlayer((Player) resp.obj);
    }

}
