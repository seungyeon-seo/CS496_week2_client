package com.example.cs496_week2_client.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;

public class FeedFragment extends Fragment {
    Api api;
    String token;
    RecyclerView posts;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    public FeedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        api = Api.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }


}