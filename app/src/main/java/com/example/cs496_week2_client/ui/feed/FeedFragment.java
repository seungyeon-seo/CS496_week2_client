package com.example.cs496_week2_client.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.User;

public class FeedFragment extends Fragment {
    Api api;
    RecyclerView posts;
    User user;
    String token;

    public static FeedFragment newInstance(User user, String token) {
        return new FeedFragment(user, token);
    }

    public FeedFragment(User user, String token) {
        this.user = user;
        this.token = token;
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
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        TextView nickName = view.findViewById(R.id.nickname_text);
        if (user == null) {
            nickName.setText("로그인 해주세요");
        }
        else {
            nickName.setText(user.getNickName());
        }
        return view;
    }

}