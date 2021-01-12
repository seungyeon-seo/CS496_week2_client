package com.example.cs496_week2_client.models;

public class ContactModel {
    // TODO 서버의 models/contact에 맞춰 수정
    String phone, fullName, personId, image;

    public String getFullName() {
        return fullName;
    }

    public String getImage() {
        return image;
    }

    public String getPersonId() {
        return personId;
    }

    public String getPhone() {
        return phone;
    }

}
