package com.example.cs496_week2_client.models;

import java.util.List;

public class User {
    private String nickName;
    private String id;
    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public String getNickName() {
        return nickName;
    }

    public String getId() {
        return id;
    }
}
