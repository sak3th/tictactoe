package com.tictactoe;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tictactoe.model.Response;

public class WelcomeFragment extends Fragment {
	
	private static final int EVENT_LOGIN_RESPONSE = 0;
 
	private TextView mLogin;
	private TextView mSkip;
	private LinearLayout mButtons;
	private ProgressBar mSpinner;
	
	private LoginTask mLoginTask;
	
	private OnLoginListener mLoginListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mLoginListener = (OnLoginListener) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null) return;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_welcome, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mLogin = (TextView) view.findViewById(R.id.buttonSignin);
		mLogin.setOnClickListener(mOnClickListener);
		mSkip  = (TextView) view.findViewById(R.id.buttonSkip);
		mSkip.setOnClickListener(mOnClickListener);
		mButtons = (LinearLayout) view.findViewById(R.id.buttons);
		mSpinner = (ProgressBar) view.findViewById(R.id.spinner);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mLoginTask = new LoginTask(mHandler.obtainMessage(EVENT_LOGIN_RESPONSE));
	}
	
	public void onPause() {
		super.onPause();
		if(!mLoginTask.isCancelled()) mLoginTask.cancel(true);
	};
	
	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonSignin:
				mButtons.setVisibility(View.GONE);
				mSpinner.setVisibility(View.VISIBLE);
				mLoginTask.execute(TicTacToe.APPENGINE_URI + "/reception");
				Toast.makeText(getActivity(), "Executed", Toast.LENGTH_SHORT).show();
				break;
			case R.id.buttonSkip:
				mLoginListener.onLoginSkipped();
				break;
			default: break;
			}
		}
	};
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENT_LOGIN_RESPONSE:
				Response resp = (Response) msg.obj;
				if(resp.status == Response.Status.OK) {
				    mLoginListener.onLoginResponse(resp);			    
				}		    
				break;

			default:
				break;
			}
		};
	};
	
	public interface OnLoginListener {
		public void onLoginSkipped();
		public void onLoginResponse(Response resp);
	}
	
}
