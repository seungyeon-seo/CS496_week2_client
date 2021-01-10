package com.example.cs496_week2_client.contacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_week2_client.MainActivity;
import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.databinding.FragmentContactBinding;
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
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ContactAdapter adapter;
    LayoutInflater initInflater;
    ViewGroup initContainer;
    Bundle initSavedInstanceState;
    ContactDataService dataService;
    // For view model
    ContactViewModel viewModel;
    ContactViewModelFactory viewModelFactory;
    FragmentContactBinding binding;


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

        // Init View Model Variables
        viewModelFactory = new ContactViewModelFactory(getActivity().getApplication(), getActivity());

        viewModel = new ViewModelProvider(getActivity(), viewModelFactory).get(ContactViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        binding.setLifecycleOwner(this);
        binding.setContactViewModel(viewModel);


        binding.textView.setText("아직 아무것도 안함");
        viewModel.message.observe(getViewLifecycleOwner(), (String str) -> {
            Log.i("ContactFragment", "in observe function "+ str);
            binding.textView.setText(str);
        });

        // RecyclerView Initialization
        view = binding.getRoot();
//        view = inflater.inflate(R.layout.fragment_contact, container, false);
//        recyclerView = view.findViewById(R.id.recycler);
        recyclerView = binding.recycler;
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Set LayoutManager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        // Set Adapter
        adapter = new ContactAdapter(binding.getContactViewModel().contacts.getValue(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        viewModel.contacts.observe(getViewLifecycleOwner(), (ArrayList<Contact> data)-> {
            Log.i("ContactFragment", "in observe function "+data.size());
            if (data != null) {
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        });

        // Init SearchView
        SearchView searchView = binding.searchView;
//        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
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
//        FloatingActionButton createButton = view.findViewById(R.id.phone_add_button);
        FloatingActionButton createButton = binding.phoneAddButton;
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateActivity.class);
                startActivityForResult(intent, 10001);
            }
        });

        // Init synchButton
//        ImageButton synchButton = view.findViewById(R.id.synchButton);
        ImageButton synchButton = binding.synchButton;
        synchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < viewModel.contacts.getValue().size(); i++) {
                    postContact(viewModel.contacts.getValue().get(i));
                }
                Toast.makeText(getContext(), "동기화 중입니다", Toast.LENGTH_SHORT).show();
            }
        });

        // Init contacts
//        viewModel.getContacts();

        return binding.getRoot();
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
                    Bundle bundle = data.getExtras();
                    Contact ct = findContact(bundle.getString("fullName"));
                    if (ct != null) postContact(ct);
                    else Log.e("Contact Creation", "Fail to create contact");
                    break;
            }
        }

    }

    private void postContact(Contact contact) {
        // Set input for server
        HashMap<String, Object> input = new HashMap<>();
        input.put("fullName", contact.fullName);
        input.put("phone", contact.phone);
        input.put("lookup", contact.lookup);
        input.put("personId", Long.toString(contact.personId));
        input.put("image", contact.image);

        // Init PUT function
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
                    Log.d("InsertContact", "success "+response.message());
                } else Log.d("InsertContact", "contact is null");
            }

            @Override
            public void onFailure(Call<ContactModel> call, Throwable t) {
                Log.e("InsertContact", "Fail to insert contact " + contact.fullName);
                t.printStackTrace();
            }
        });
    }

    private Contact findContact(String name) {
        for (int i = 0; i < viewModel.contacts.getValue().size(); i++) {
            Contact ct = viewModel.contacts.getValue().get(i);
            if (ct.fullName == name)
                return ct;
        }
        return null;
    }
}
