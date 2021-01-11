package com.example.cs496_week2_client.models;

import java.util.ArrayList;

public class Group {
    // TODO 서버의 models/group 에 맞춰 수정

    private String name;
    private String code;
    private ArrayList<String> users;

    public ArrayList<String> getUsers() {
        return users;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
