package com.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gcm.GCMRegistrar;
import com.tictactoe.globals.Storage;

public class PreHomeActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boolean home = ( (Storage.get(getApplicationContext(), Storage.GCM_REG_ID) != null) || 
							(!GCMRegistrar.isRegisteredOnServer(getApplicationContext())) ); 
		
		home = false;
		finish();
		if(home) {
			startActivity(new Intent(getApplicationContext(), HomeActivity.class));
		} else {
			startActivity(new Intent(getApplicationContext(), GameKeeperActivity.class));
		}
	}

}
