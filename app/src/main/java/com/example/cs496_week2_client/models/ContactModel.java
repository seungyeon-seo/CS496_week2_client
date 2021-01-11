package com.example.cs496_week2_client.models;

public class ContactModel {
    // TODO 서버의 models/contact에 맞춰 수정
    String phone, fullName, lookup, personId, image;
    int groupId, status;
    String location;

    public String getFullName() {
        return fullName;
    }

    public String getImage() {
        return image;
    }

    public String getLookup() {
        return lookup;
    }

    public String getPersonId() {
        return personId;
    }

    public String getPhone() {
        return phone;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }
}
