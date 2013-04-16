package com.tictactoe.model;

import java.util.ArrayList;

public class Player {

    String email;
    String userId;
    String nickName;
    String name;
    int age;
    
    ArrayList<String> gcmRegIds;

    public Player(String email, String userId,String regId, String nickName, String name, int age) {
        this.email = email;
        this.userId = userId;
        this.nickName = nickName;
        this.name = name;
        this.age = age;
        
        if(regId != null) {
            gcmRegIds = new ArrayList<String>();
            gcmRegIds.add(regId);
        }
    }

    public Player(String email, String userId) {
        this(email, userId, null, null, null, 0);
    }

    public Player(Player p) {
        this.email = p.email;
        this.userId = p.userId;
        this.nickName = p.nickName;
        this.name = p.name;
        this.age = p.age;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public void addGcmRegId(String regId) {
        if(regId != null) gcmRegIds.add(regId);
    }
    
    public void removeGcmRegId(String regId) {
        if(regId != null) gcmRegIds.remove(regId);
    }
    
    public String[] getGcmRegIds() {
        return gcmRegIds.toArray(new String[gcmRegIds.size()]);
    }

    public void setInfo(String nickName, String name, int age) {
        this.nickName = nickName;
        this.name = name;
        this.age = age;
    }
}
