package com.example.cs496_week2_client.contacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_week2_client.MainActivity;
import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.models.ContactModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactFragment extends Fragment {
    View view;
    public ArrayList<Contact> contacts;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ContactAdapter adapter;
    LayoutInflater initInflater;
    ViewGroup initContainer;
    Bundle initSavedInstanceState;
    ContactDataService dataService;


    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        // no arguments
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataService = new ContactDataService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Store parameters
        initInflater = inflater;
        if (container != null) initContainer = container;
        if (savedInstanceState != null) initSavedInstanceState = savedInstanceState;

        // RecyclerView Initialization
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Set LayoutManager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        // Init contact list
        contacts = getContacts();

        // Set Adapter
        adapter = new ContactAdapter(contacts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Init SearchView
        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int pos = adapter.findText(newText);
                recyclerView.scrollToPosition(pos);
                return true;
            }
        });

        // Init createButton
        FloatingActionButton createButton = view.findViewById(R.id.phone_add_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateActivity.class);
                startActivityForResult(intent, 10001);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            MainActivity main = (MainActivity) getActivity();
            switch (requestCode) {
                //createButton click
                case 10001:
                    main.setViewPager(0);
                    break;
            }
        }

    }

    private ArrayList<Contact> getContacts() {
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

        Cursor cursor = requireActivity().getContentResolver().query(
                uri, projection, null, null, sortOrder);

        // Read Data
        LinkedHashSet<Contact> hasList = new LinkedHashSet<Contact>();
        ArrayList<Contact> contacts;

        if (cursor.moveToFirst()) {
            do {
                String phone = cursor.getString(0);
                String fullName = cursor.getString(1);
                String image = cursor.getString(2);
                long person = cursor.getLong(3);
                String lookup = cursor.getString(4);

                Contact contact = new Contact(phone, fullName, image, person, lookup);

                postContact(contact);

                if (contact.isStartWith("01")) {
                    hasList.add(contact);
                    Log.d("<<CONTACTS>>", contact.getMsg());
                }

            } while (cursor.moveToNext());
        }

        contacts = new ArrayList<Contact>(hasList);
        if (cursor != null) {
            cursor.close();
        }
        return contacts;
    }

    private void postContact(Contact contact) {
        // Set input to POST to server
        HashMap<String, Object> input = new HashMap<>();
        input.put("fullName", contact.fullName);
        input.put("phone", contact.phone);
        input.put("lookup", contact.lookup);
        input.put("personId", Long.toString(contact.personId));
        input.put("image", contact.image);

        // Init POST function
        dataService.insert.insertContact(input).enqueue(new Callback<ContactModel>() {
            @Override
            public void onResponse(Call<ContactModel> call, Response<ContactModel> response) {
                Log.d("InsertContact", "on Response");
                if (!response.isSuccessful()) {
                    Log.i("InsertContact", "response is not successful");
                    return;
                }
                ContactModel ct = response.body();
                if (ct != null) {
                    Log.d("InsertContact", "contact is filled");
                } else Log.d("InsertContact", "contact is null");
            }

            @Override
            public void onFailure(Call<ContactModel> call, Throwable t) {
                Log.e("InsertContact", "Fail to insert contact " + contact.fullName);
                t.printStackTrace();
            }
        });
    }
}
