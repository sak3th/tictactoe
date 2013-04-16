package com.tictactoe.model;

public class Game {
    
    public enum Turn {
        X, O
    }
    
    public enum GameState {
        WHISTLE, MOVE1, MOVE2, MOVE3, MOVE4, MOVE5, MOVE6, MOVE7, MOVE8, MOVE9, BELL
    }
    
    public String xEmail;
    public String oEmail;
    
    public String xGcmId;
    public String oGcmId;
    
    public Turn turn;
    public GameState gameState;

    public Game(String xEmail, String oEmail, String xGcmId, String oGcmId, Turn turn) {
        this.xEmail = xEmail;
        this.oEmail = oEmail;
        this.xGcmId = xGcmId;
        this.oGcmId = oGcmId;
        this.turn = turn;
    }
    
    
    
    

}
