package com.example.cs496_week2_client.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public class LoginFragment extends Fragment {
    View view;
    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView userNameText;
    Api api;
    String token;


    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        api = Api.getInstance();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
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
        Log.i("LoginFragment", "callback registered");

        return view;
    }

    // 페이스북에서 자체적으로 버튼을 누르면 startActivityForResult 가 실행되게 해놨을 것
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // 다음 FeedFragment 로 넘기기
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String token = accessToken.getToken();
    }
}
