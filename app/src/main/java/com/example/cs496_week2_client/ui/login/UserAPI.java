package com.example.cs496_week2_client.ui.login;

import android.content.Intent;
import android.util.Log;

import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.Post;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.util.ResponseCode;
import com.facebook.AccessToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class UserAPI {
    static void login(String token) {
        Api api = Api.getInstance();
        api.getUserService().auth.login(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent();
                    // TODO response 객체로부터 id, nickname, posts 읽어오기
                    intent.putExtra("userId", "test_id");
                    intent.putExtra("nickname", "test_nickname");
                    intent.putExtra("posts", new ArrayList<String>());
                    Log.i("Login", "안녕하세요 ");
                } else if (response.code() == 190) {
                    Log.i("LoginActivity", "해당 유저가 존재하지 않습니다: " + response.body());
                } else { // 데이터베이스에 접속할 수 없습니다
                    Log.i("LoginActivity", "데이터베이스에 접속할 수 없습니다: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("LoginActivity", "서버에 접속할 수 없습니다: ");
            }
        });
    }
}
