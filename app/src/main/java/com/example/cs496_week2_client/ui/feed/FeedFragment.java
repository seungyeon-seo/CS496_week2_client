package com.example.cs496_week2_client.ui.feed;

import android.content.Intent;
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
import com.example.cs496_week2_client.ui.login.LoginActivity;
import com.example.cs496_week2_client.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.cs496_week2_client.util.UserUtils.getUserBundle;
import static com.example.cs496_week2_client.util.UserUtils.parseUserBundleGetToken;
import static com.example.cs496_week2_client.util.UserUtils.parseUserBundleGetUser;

public class FeedFragment extends Fragment {
    Api api;
    RecyclerView posts;
    User user;
    String token;

    public static FeedFragment newInstance(User user, String token) {
        FeedFragment feedFragment = new FeedFragment();
//        feedFragment.setArguments(getUserBundle(user, token));
        return feedFragment;
    }

    public FeedFragment() {
        // 아무것도 하면 안 됨!
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        api = Api.getInstance();
//        user = parseUserBundleGetUser(getArguments());
//        token = parseUserBundleGetToken(getArguments());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        TextView nickName = view.findViewById(R.id.nickname_text);
        FloatingActionButton newPostButton = view.findViewById(R.id.new_post);
//        newPostButton.setOnClickListener( v -> {
//            Intent uploadImageIntent = new Intent(getActivity(), UploadImageActivity.class);
//            startActivity(uploadImageIntent);
//        });

        if (user == null) {
            nickName.setText("로그인 해주세요");
        }
        else {
            nickName.setText(user.getNickName());
        }
        return view;
    }

}