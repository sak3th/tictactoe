package com.tictactoe.model;

public class Game {

    public enum Turn {
        X, O
    }

    public enum Move {
        NOT_PLAYED, CROSS_PLAYED, CROSS_PLAY, NOUGHT_PLAYED, NOUGHT_PLAY
    }

    public enum GameState {
        STARTED,  ENDED
    }

    public String gameId;

    public String xEmail;
    public String oEmail;

    public Turn turn;
    public Move[] moves;
    public GameState gameState;

    public Game(String xEmail, String oEmail, Turn turn) {
        this.xEmail = xEmail;
        this.oEmail = oEmail;
        this.turn = turn;

        moves = new Move[9];
        for(int i=0; i<9; i++) {
            moves[i] = Move.NOT_PLAYED;
        }
    }

    public void play(int position) {
        if(moves[position] == Move.CROSS_PLAY || moves[position] == Move.NOUGHT_PLAY) {
            moves[position] = Move.NOT_PLAYED;
        } else if(moves[position] == Move.NOT_PLAYED){
            Move currentMove = ((turn == Turn.X)? Move.CROSS_PLAY: Move.NOUGHT_PLAY);
            for(int i=0; i<9; i++) {
                if(moves[i] == currentMove) {
                    moves[i] = Move.NOT_PLAYED;
                    break;
                }
            }
            moves[position] = currentMove;
        }
    }

    public boolean isMyTurn(String email) {
        if(turn == Turn.X)
            return email.equalsIgnoreCase(xEmail);
        else
            return email.equalsIgnoreCase(oEmail);
    }

}
