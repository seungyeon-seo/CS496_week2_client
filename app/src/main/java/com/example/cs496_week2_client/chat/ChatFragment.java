package com.example.cs496_week2_client.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cs496_week2_client.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public class ChatFragment extends Fragment {
    View view;
    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView userNameText;

    public ChatFragment() {
    }

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        Log.i("Chat", "view inflated");

        // login button 초기화
        loginButton = (LoginButton) view.findViewById(R.id.btn_facebook_login);
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);

        userNameText = (TextView) view.findViewById(R.id.user_name);
        userNameText.setText("왜이래..");


        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new LoginCallback());
        Log.i("ChatFragment", "callback registered");

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
