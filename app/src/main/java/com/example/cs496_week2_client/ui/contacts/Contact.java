package com.example.cs496_week2_client.ui.contacts;

import android.location.Location;
import android.util.Log;

// TODO ContactModel 과 Contact 통합. 필요 없는 건 아예 없애기
public class Contact implements Comparable<Contact> {
    String phone, fullName, lookup = null;
    long personId;
    String image = null;

    // TODO 필요 없는 정보 지우기

    public Contact(String ph, String fn, String bytes, long pid) {
        phone = ph;
        fullName = fn;
        if (bytes != null)  image = bytes;
        personId = pid;
    }

    /* Useful Functions */
    public boolean isStartWith (String str) {
        return phone.startsWith(str);
    }
    public String getMsg() {
        return ("name=" + fullName + ", phone=" + phone);
    }

    @Override
    public int compareTo(Contact o) {
        if (this.fullName.compareTo(o.fullName) > 0)
            return 1;
        Log.i("Contact", "Compare result: 0");
        return 0;
    }
}