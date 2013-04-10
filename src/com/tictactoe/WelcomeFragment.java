package com.tictactoe;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WelcomeFragment extends Fragment {

    private TextView mLoginAllow;
    private TextView mLoginSkip;
    private LinearLayout mButtons;
    private ProgressBar mSpinner;

    private LoginListener mLoginListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mLoginListener = (LoginListener) activity;
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
        mLoginAllow = (TextView) view.findViewById(R.id.buttonSignin);
        mLoginAllow.setOnClickListener(mOnClickListener);
        mLoginSkip  = (TextView) view.findViewById(R.id.buttonSkip);
        mLoginSkip.setOnClickListener(mOnClickListener);
        mButtons = (LinearLayout) view.findViewById(R.id.buttons);
        mSpinner = (ProgressBar) view.findViewById(R.id.spinner);
    }

    private void onLoginAllowed() {
        mLoginListener.onLoginAllowed();
    }

    private void onLoginSkipped() {
        mLoginListener.onLoginSkipped();
    }

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.buttonSignin:
                mLoginAllow.setTextColor(getResources().getColor(R.color.button_gray));
                onLoginAllowed();
                break;
            case R.id.buttonSkip:
                mLoginSkip.setTextColor(getResources().getColor(R.color.button_gray));
                onLoginSkipped();
                break;
            default: break;
            }
        }
    };

    public interface LoginListener {
        public void onLoginAllowed();
        public void onLoginSkipped();
    }

}
