package com.tictactoe.model;

public class Player {
	public String email;
	public String userId;
	public String name;
	public String nickName;
	public int age;
	
	Player(String email, String userId, String name, String nickName, int age) {
		this.email = email;
		this.name = name;
		this.nickName = nickName;
		this.age = age;
	}
	
	public Player(String email, String userId) {
		this(email, userId, null, null, 0);
	}

	public String getEmail() {
		return email;
	}
}
