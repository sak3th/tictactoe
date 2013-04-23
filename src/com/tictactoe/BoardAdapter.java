package com.tictactoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tictactoe.model.Game.Move;

public class BoardAdapter extends BaseAdapter {
	private Move[] moves;
	private Context context;
	private int resource;

	public BoardAdapter(Context context, int resource, Move[] moves) {
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
	
	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setMove(int position, Move move) {
		moves[position] = move;
	}

	public Move getMove(int position) {
		return moves[position];
	}
}
