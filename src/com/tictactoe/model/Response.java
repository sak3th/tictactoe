package com.tictactoe.model;

import com.google.gson.Gson;

public class Response<T> {
	
    public enum Status { OK, WARNING, ERROR }
    
    public Status status;
    public String msg;
    public T obj;
    
    public Response(Status status, String msg, T obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }
    
    /*
     * TODO check this later
    static Response<Player> create(Status status, String msg, Player obj) {
        return new Response<Player>( status,  msg, obj);
    }
    */

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}