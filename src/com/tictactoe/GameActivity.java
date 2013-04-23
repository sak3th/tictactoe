package com.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.tictactoe.globals.TicTacToeGlobals;
import com.tictactoe.model.Game;
import com.tictactoe.model.Game.Move;

public class GameActivity extends Activity {

	GridView mGridView;
	BoardAdapter mBoardAdapter;
	Game mGame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		mGridView = (GridView) findViewById(R.id.gridViewBoard);
		// TODO get game and moves from intent
		int position = getIntent().getIntExtra("gamePosition", -1);
		Log.d("saketh","postion - " + position);
		if(position == -1) {
			// TODO fetch position from shared preference
		} else {
		    mGame = TicTacToeGlobals.getInstance().getGames()[position];
		}

		mBoardAdapter = new BoardAdapter(getApplicationContext(), R.layout.move, mGame.moves);
		mGridView.setAdapter(mBoardAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(mGame.moves[position] == Move.CROSS_PLAYED
				        || mGame.moves[position] == Move.NOUGHT_PLAYED) {
				    Animation wiggle =
				            AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wiggle);
                    view.startAnimation(wiggle);
				} else {
				    mGame.play(position);
                    mBoardAdapter.notifyDataSetChanged();
				}
			}
		});
	}


	public class BoardAdapter extends ArrayAdapter<Move> {
		private Move[] moves;
		private Context context;
		private int resource;

		public BoardAdapter(Context context, int resource, Move[] moves) {
			super(context, resource, moves);
			this.moves = moves;
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
			ImageView image = (ImageView) convertView.findViewById(R.id.imageButtonMove);
			if(moves[position] == Move.NOT_PLAYED) {
				image.setImageResource(R.drawable.ic_not_played);
			} else if(moves[position] == Move.CROSS_PLAYED) {
				image.setImageResource(R.drawable.ic_cross_played);
			} else if(moves[position] == Move.NOUGHT_PLAYED) {
				image.setImageResource(R.drawable.ic_nought_played);
			} else if(moves[position] == Move.CROSS_PLAY) {
                image.setImageResource(R.drawable.ic_cross_play);
            } else if(moves[position] == Move.NOUGHT_PLAY) {
                image.setImageResource(R.drawable.ic_nought_play);
            }
			return convertView;
		}

		@Override
		public int getCount() {
			return 9;
		}
	}

}
