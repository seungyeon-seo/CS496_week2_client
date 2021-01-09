package com.example.cs496_week2_client.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.Post;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.util.RequestCode;
import com.example.cs496_week2_client.util.ResponseCode;
import com.example.cs496_week2_client.util.UserUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    View view;
    LoginButton loginButton;
    CallbackManager callbackManager;
    Api api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        api = Api.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (AccessToken.getCurrentAccessToken() != null) {
            String token = AccessToken.getCurrentAccessToken().getToken();
            // TODO: verify token
            Log.i("Login Token", token);
            api.getUserService().auth.login(token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        // TODO response 객체로부터 id, nickname, posts 읽어오기
                        Toast.makeText(getApplicationContext(), "안녕하세요.", Toast.LENGTH_SHORT)
                                .show();
                        User user = response.body();
                        setResult(ResponseCode.LOGIN_SUCCESSFUL, UserUtils.getUserIntent(user.getId(), user.getNickName(), (ArrayList<String>) user.getPosts(), token));
                    } else if (response.code() == ResponseCode.HTTP_FORBIDDEN) {
                        Log.i("LoginActivity", "해당 유저가 존재하지 않습니다: " + response.body());
                        Toast.makeText(getApplicationContext(), "처음 뵙겠습니다!", Toast.LENGTH_SHORT)
                                .show();
                        setResult(ResponseCode.REGISTER_REQUIRED);
                    } else {
                        Log.i("LoginActivity", "데이터베이스에 접속할 수 없습니다: " + response.body());
                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요!", Toast.LENGTH_SHORT)
                                .show();
                        setResult(ResponseCode.DATABASE_FAILURE);
                    }
                    finish();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("LoginActivity", "서버에 접속할 수 없습니다: ");
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        // login button 초기화
        loginButton = findViewById(R.id.btn_facebook_login);
        loginButton.setReadPermissions("email");

        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new LoginCallback());
    }


    // 페이스북에서 자체적으로 버튼을 누르면 startActivityForResult 가 실행되게 해놨을 것
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.i("Login Token", AccessToken.getCurrentAccessToken().getToken());

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String token = accessToken.getToken();
        if (token != null) {
            api.getUserService().auth.login(token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        // TODO response 객체로부터 id, nickname, posts 읽어오기
                        Toast.makeText(getApplicationContext(), "안녕하세요.", Toast.LENGTH_SHORT)
                                .show();
                        User user = response.body();
                        setResult(ResponseCode.LOGIN_SUCCESSFUL, UserUtils.getUserIntent(user.getId(), user.getNickName(), (ArrayList<String>) user.getPosts(), token));
                    } else if (response.code() == ResponseCode.HTTP_FORBIDDEN) {
                        Log.i("LoginActivity", "해당 유저가 존재하지 않습니다: " + response.body());
                        Toast.makeText(getApplicationContext(), "처음 뵙겠습니다!", Toast.LENGTH_SHORT)
                                .show();
                        setResult(ResponseCode.REGISTER_REQUIRED);
                    } else {
                        Log.i("LoginActivity", "데이터베이스에 접속할 수 없습니다: " + response.body());
                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요!", Toast.LENGTH_SHORT)
                                .show();
                        setResult(ResponseCode.DATABASE_FAILURE);
                    }
                    finish();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("LoginActivity", "서버에 접속할 수 없습니다: ");
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } else {
            Log.e("LoginActivity", "token 이 없어요..");
        }
    }


}