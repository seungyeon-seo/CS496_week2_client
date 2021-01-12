package com.example.cs496_week2_client.ui.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.databinding.FragmentContactBinding;
import com.example.cs496_week2_client.models.Contact;
import com.example.cs496_week2_client.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.cs496_week2_client.util.UserUtils.getUserBundle;
import static com.example.cs496_week2_client.util.UserUtils.parseUserBundleGetUser;

public class ContactFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ContactAdapter adapter;
    LayoutInflater initInflater;
    ViewGroup initContainer;
    Bundle initSavedInstanceState;
    ContactService dataService;
    User user;

    // For view model
    ContactViewModel viewModel;
    ContactViewModelFactory viewModelFactory;
    FragmentContactBinding binding;


    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance(User user, String token) {
        // no arguments
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.setArguments(getUserBundle(user, token));
        return contactFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = parseUserBundleGetUser(getArguments());
        dataService = Api.getInstance().getContactService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Store parameters
        initInflater = inflater;
        if (container != null) initContainer = container;
        if (savedInstanceState != null) initSavedInstanceState = savedInstanceState;

        // Init View Model Variables
        viewModelFactory = new ContactViewModelFactory(getActivity().getApplication(), getActivity(), user);
        viewModel = new ViewModelProvider(getActivity(), viewModelFactory).get(ContactViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        binding.setLifecycleOwner(this);
        binding.setContactViewModel(viewModel);

        // RecyclerView Initialization
        view = binding.getRoot();
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
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        });

        // Init SearchView
        SearchView searchView = binding.searchView;
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
        FloatingActionButton createButton = binding.phoneAddButton;
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ContactCreateActivity.class);
            startActivityForResult(intent, RequestCode.CREATE_NEW_CONTACT);
        });

        // Init sync Button
        TextView syncButton = binding.synchButton;
        syncButton.setOnClickListener(v -> {
            for (int i = 0; i < viewModel.contacts.getValue().size(); i++) {
                viewModel.postContact(viewModel.contacts.getValue().get(i));
            }
            Toast.makeText(getContext(), "동기화 중입니다", Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            MainActivity main = (MainActivity) getActivity();
            switch (requestCode) {
                // createButton click
                case RequestCode.CREATE_NEW_CONTACT:
                    main.setViewPager(0);
                    Bundle bundle = data.getExtras();
                    viewModel.getContacts();
                    Contact ct = findContact(bundle.getString("fullName"));
                    if (ct != null) viewModel.postContact(ct);
                    else Log.e("Contact Creation", "Fail to create contact");
                    break;
                default:
                    Log.e("ContactFragment", "해당 requestCode 는 존재하지 않습니다 : " + requestCode);
            }
        }
    }

    private Contact findContact(String name) {
        for (int i = 0; i < viewModel.contacts.getValue().size(); i++) {
            Contact ct = viewModel.contacts.getValue().get(i);
            if (ct.getFullName().equals(name))
                return ct;
        }
        Log.e("findContact", "return null");
        return null;
    }
}

class RequestCode {
    static final int CREATE_NEW_CONTACT = 1;

}
