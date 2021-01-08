package com.example.cs496_week2_client.contacts;

import android.net.Uri;

public class Contact {
    String phone, fullName, lookup;
    long personId;
    String image = null;

    public Contact(String ph, String fn, String bytes, long pid, String key) {
        phone = ph;
        fullName = fn;
        if (bytes != null)  image = bytes;
        personId = pid;
        lookup = key;
    }

    /* Useful Functions */
    public boolean isStartWith (String str) {
        return phone.startsWith(str);
    }
    public String getMsg() {
        return ("name=" + fullName + ", phone=" + phone);
    }
}
