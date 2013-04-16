package com.tictactoe;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GameKeeperFragment extends Fragment {

    private ListView mListView;
    private GamesAdapter mGamesAdapter;
    
    private GameListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (GameListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_game, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView)view.findViewById(R.id.listView);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        /*
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
        */

        Game[] games = new Game[6];
        mGamesAdapter = new GamesAdapter(getActivity().getApplicationContext(), 
                    R.layout.game_row, games);
        mListView.setAdapter(mGamesAdapter);
        mListView.setStackFromBottom(true);
    }


    public class GamesAdapter extends ArrayAdapter<Game> {
        private Game[] games;
        private Context context;
        private int resource;

        public GamesAdapter(Context context, int resource, Game[] games) {
            super(context,resource, games);
            this.games = games;
            this.resource = resource;
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // First let's verify the convertView is not null
            if (convertView == null) {
                // This a new view we inflate the new layout
                LayoutInflater inflater = (LayoutInflater) 
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, parent, false);
            }
           
            // Now we can fill the layout with the right values
            ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            if((games.length - position) == 1) {
                textView.setText("More");
                imageButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_more));
                imageButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "More clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if((games.length - position) == 2) {
                textView.setText("New Game");
                imageButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_new));
                imageButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "New clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                textView.setText("Waiting for turn");
                imageButton.setBackgroundDrawable(
                        getResources().getDrawable(R.drawable.ic_active_game));
                imageButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Active Game clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return convertView;
        }
    }
    
    public interface GameListener {
        public void onGameClicked();
    }

    
    public class Game {
        
    }



}
