package com.example.cs496_week2_client.contacts;

import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs496_week2_client.models.ContactModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Contact>> contacts;
    public MutableLiveData<String> message;
    private int called=0;
    Activity activity;
    ContactDataService dataService;

    ContactViewModel(Activity act){
        activity = act;
        dataService = new ContactDataService();
        contacts = new MutableLiveData<ArrayList<Contact>>();
        contacts.setValue(new ArrayList<Contact>());
        message = new MutableLiveData<String>();
        getContacts();
    }

    public void getContacts() {
        getContactsDevice();
        Log.i("GetContacts", "Done from device");
        getContactsServer();
        Log.i("GetContacts", "Done from server");
        called = called + 1;
        message.setValue(String.valueOf(called));
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
                long person = cursor.getLong(3);
                String lookup = cursor.getString(4);

                Contact contact = new Contact(phone, fullName, image, person, lookup);

                if (contact.isStartWith("01")) {
                    if(!isContained(contact))
                        contacts.getValue().add(contact);
                    Log.d("<<CONTACTS>>", contact.getMsg());
                }

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void getContactsServer() {
        Log.i("GetContactServer", "Start");
        dataService.select.getContacts().enqueue(new Callback<ArrayList<ContactModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ContactModel>> call,
                                   Response<ArrayList<ContactModel>> response) {
                Log.i("GetContactServer", "Response");
                if (response.isSuccessful()) {
                    ArrayList<ContactModel> contactModels = response.body();
                    ArrayList<Contact> contact = contacts.getValue();
                    for (int i = 0; i < contactModels.size(); i++) {
                        Contact ct = new Contact(contactModels.get(i).getPhone(),
                                contactModels.get(i).getFullName(),
                                contactModels.get(i).getImage(),
                                Long.parseLong(contactModels.get(i).getPersonId()),
                                contactModels.get(i).getLookup() );
                        if(!isContained(ct))
                        {
                            Log.i("GetContactServer", ct.fullName+" is not contained :"+contacts.getValue().size());
                            contact.add(ct);
                            Log.i("GetContactServer", ct.fullName+" is added : "+contacts.getValue().size());
                        }
                    }
                    contacts.setValue(contact);
                    Log.i("GetContactServer", "Success");
                }
                else try {
                    throw new Exception("response is not successful");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ContactModel>> call, Throwable t) {
                Log.d("GetContactServer", "Failed to get data");
                t.printStackTrace();
            }
        });

    }

    private boolean isContained(Contact ct) {
        for (int i = 0; i < contacts.getValue().size(); i++) {
            if (contacts.getValue().get(i).phone.equals(ct.phone))
                return true;
        }
        return false;
    }
}

class ContactViewModelFactory implements ViewModelProvider.Factory {
    Activity activity;
    ContactViewModelFactory(Application application, Activity act){
        activity = act;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ContactViewModel.class)) {
            return (T)new ContactViewModel(activity);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}