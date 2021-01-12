package com.example.cs496_week2_client.models;

public class Contact {
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


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
