package com.example.wx.inba.model;

public class User {
    int id;
    String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public User() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "User[id="+id+",username="+username+"]";
    }
}

