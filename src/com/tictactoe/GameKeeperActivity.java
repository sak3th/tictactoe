package com.tictactoe;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tictactoe.WelcomeFragment.LoginListener;
import com.tictactoe.globals.Storage;
import com.tictactoe.globals.TicTacToeGlobals;
import com.tictactoe.model.Game;
import com.tictactoe.model.Game.Move;
import com.tictactoe.model.Game.Turn;

public class GameKeeperActivity extends Activity {

	private LinearLayout mLayoutGameKeeper;
    private ListView mListView;
    private GamesAdapter mGamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_game_keeper);
        Random random = new Random();
        int noofgames = 4;
        Game[] games = new Game[noofgames];

        for(int game =0 ; game < noofgames; game ++)
        	games[game] = new Game("", "", (new Random().nextInt(2) == 0)? Turn.O : Turn.X);

        for (int i=0; i < noofgames ; i++) {
			games[i].xEmail = "v.saketh@gmail.com";
			games[i].oEmail = (random.nextInt(100) + 1) + "@guest.com";
			games[i].gameId = String.valueOf((random.nextInt(1000000) + 1));
			
			for (int j=0; j<9; j++) {
				int rand = random.nextInt(3);
				switch (rand) {
				case 0:
					games[i].moves[j] = Move.NOT_PLAYED;
					break;
				case 1:
					games[i].moves[j] = Move.NOUGHT_PLAYED;
					break;
				case 2:
					games[i].moves[j] = Move.CROSS_PLAYED;
					break;
				}
			}
		}
        
        mLayoutGameKeeper = (LinearLayout)findViewById(R.id.linearLayoutGameKeeper);
        mListView = (ListView)findViewById(R.id.listView);
        TicTacToeGlobals.getInstance().setGames(games);
        mGamesAdapter = new GamesAdapter(getApplicationContext(), R.layout.game_keeper_row, games);
        mListView.setAdapter(mGamesAdapter);
        mListView.setStackFromBottom(true);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mGamesAdapter.notifyDataSetChanged();
    }


    public class GamesAdapter extends ArrayAdapter<Game> {
        private Game[] games;
        private Context context;
        private int resource;
        private GridView gridView;


        public GamesAdapter(Context context, int resource, Game[] games) {
            super(context,resource, games);
            this.games = games;
            this.resource = resource;
            this.context = context;
            gridView = new GridView(getApplicationContext());
            gridView.setNumColumns(3);
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
            // TODO use only one
            imageButton.setBackgroundDrawable(null);
            imageButton.setImageBitmap(null);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            if((games.length - position) == 1) {
                textView.setText("More");
                imageButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_more));
                imageButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "More clicked", Toast.LENGTH_SHORT).show();
                        floatProgressBar();
                    }
                });
            } else if((games.length - position) == 2) {
                textView.setText("New Game");
                imageButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_new));
                imageButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "New clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                String regEmail = Storage.get(getApplicationContext(), Storage.REG_EMAIL);
                regEmail = "v.saketh@gmail.com";
            	if((games[position].isMyTurn(regEmail))) { 
            	    textView.setText(R.string.your_turn);
            	} else {
            	    textView.setText(R.string.waiting_for_turn);
            	}

                BoardAdapter adapter = new BoardAdapter(getApplicationContext(), 
                        R.layout.preview_move, games[position].moves);
                gridView.setAdapter(adapter);
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                        getResources().getDisplayMetrics());
                Bitmap bitmap = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.translate(-gridView.getScrollX(), -gridView.getScrollY());
                int spec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                gridView.measure(spec, spec);                
                gridView.layout(0, 0, height, height);
                gridView.draw(canvas);
                imageButton.setImageBitmap(bitmap);
                imageButton.setOnClickListener(new GameOnClickListener(position));
            }
            return convertView;
        }
        
        private class GameOnClickListener implements OnClickListener {
        	int position;
        	public GameOnClickListener(int position){
        		this.position = position;
        	}
			@Override
			public void onClick(View v) {
            	Intent intent = new Intent(getContext(), GameActivity.class);
            	intent.putExtra("gamePosition", position);
            	startActivity(intent);
			}
        }
    }
    
    public interface GameListener {
        public void onGameClicked();
    }
    
    private void floatProgressBar() {
    	startActivity(new Intent(getApplicationContext(), ProgressSpinnerActivity.class) );
    }

}
