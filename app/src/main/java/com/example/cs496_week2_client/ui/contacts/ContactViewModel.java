package com.example.cs496_week2_client.ui.contacts;

import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.Contact;
import com.example.cs496_week2_client.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ContactViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Contact>> contacts;
    Activity activity;
    ContactService contactService;
    User user;

    ContactViewModel(Activity activity, User user){
        this.activity = activity;
        this.user = user;

        contactService = Api.getInstance().getContactService();
        contacts = new MutableLiveData<ArrayList<Contact>>();
        contacts.setValue(new ArrayList<Contact>());
        getContacts();
    }

    public void getContacts() {
        getContactsServer();
        Log.i("GetContacts", "서버에서 연락처를 가져왔습니다");
        getContactsDevice();
        Log.i("GetContacts", "기기에서 연락처를 가져왔습니다");
    }

    private void getContactsDevice() {
        // Init Cursor
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Photo.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        };
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = activity.getApplicationContext().getContentResolver().query(
                uri, projection, null, null, sortOrder);

        // Read Data
        if (cursor.moveToFirst()) {
            do {
                String phone = cursor.getString(0);
                String fullName = cursor.getString(1);
                String image = cursor.getString(2);
                String personId = String.valueOf(cursor.getLong(3));

                Contact contact = new Contact();
                contact.setPhone(phone);
                contact.setFullName(fullName);
                contact.setImage(image);
                contact.setPersonId(personId);

                ArrayList<Contact> curContacts = contacts.getValue();

                if (isStartWith(contact,"01")) {
                    if(!isContained(contact))
                        curContacts.add(contact);
                    Log.d("<<CONTACTS>>", getMsg(contact));
                }
                contacts.setValue(curContacts);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void getContactsServer() {
        contactService.contact.getContacts(user.getId()).enqueue(new Callback<ArrayList<Contact>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ArrayList<Contact>> call,
                                   Response<ArrayList<Contact>> response) {
                Log.i("GetContactServer", "Response");
                if (response.isSuccessful()) {
                    ArrayList<Contact> serverContacts = response.body();
                    ArrayList<Contact> curContacts = contacts.getValue();
                    assert curContacts != null;
                    for (int i = 0; serverContacts != null && i < serverContacts.size(); i++) {
                        Contact contact = serverContacts.get(i);
                        if(!isContained(contact)) // is curContacts contains contact?
                        {
                            curContacts.add(contact);
                        }
                    }
                    contacts.setValue(curContacts);
                }
                else try {
                    throw new Exception("response is not successful");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ArrayList<Contact>> call, Throwable t) {
                Log.d("GetContactServer", "Failed to get data");
                t.printStackTrace();
            }
        });
    }

    public void postContact(Contact contact) {
        Log.i("PostContact", "start function about "+contact.getFullName());
        // Set input for server
        HashMap<String, Object> input = new HashMap<>();
        input.put("fullName", contact.getFullName());
        input.put("phone", contact.getPhone());
        input.put("personId", contact.getPersonId());
        input.put("image", contact.getImage());

        contactService.contact.insertContact(input, user.getId()).enqueue(new Callback<Contact>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                Log.d("InsertContact", "on Response");
                if (!response.isSuccessful()) {
                    Log.i("InsertContact", "response is not successful");
                    return;
                }
                Contact ct = response.body();
                if (ct != null) {
                    Log.d("InsertContact", "success "+response.message());
                } else Log.d("InsertContact", "contact is null");
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.e("InsertContact", "Fail to insert contact " + contact.getFullName());
                t.printStackTrace();
            }
        });
    }

    private boolean isContained(Contact ct) {
        for (int i = 0; i < contacts.getValue().size(); i++) {
            if (contacts.getValue().get(i).getPhone().equals(ct.getPhone()))
                return true;
        }
        return false;
    }

    private boolean isStartWith (Contact contact, String str) {
        return contact.getPhone().startsWith(str);
    }
    private String getMsg(Contact contact) {
        return ("name=" + contact.getFullName() + ", phone=" + contact.getPhone());
    }

}

class ContactViewModelFactory implements ViewModelProvider.Factory {
    Activity activity;
    User user;
    ContactViewModelFactory(Application application, Activity activity, User user){
        this.activity = activity;
        this.user = user;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ContactViewModel.class)) {
            return (T)new ContactViewModel(activity, user);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}