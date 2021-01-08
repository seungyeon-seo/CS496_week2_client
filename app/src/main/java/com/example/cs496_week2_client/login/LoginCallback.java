package com.example.cs496_week2_client.login;

import android.util.Log;

import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.User;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LoginCallback implements FacebookCallback<LoginResult> {

    // 로그인 성공 시 호출 됩니다. Access Token 발급 성공.
    @Override
    public void onSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();
        String token = accessToken.getToken();

        Api.getInstance().getUserService().auth.login(token).enqueue(new Callback<User>() {
            // 토큰 검증 결과에 따라 로그 출력
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.i("LoginCallback", "정상적인 토큰입니다");
                } else {
                    Log.i("LoginCallback", "정상 토큰이 아닙니다");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("LoginCallback", "로그인(auth/{token}) API 호출에 실패했습니다");
                t.printStackTrace();
            }
        });

    }

    // 로그인 창을 닫을 경우, 호출됩니다.
    @Override
    public void onCancel() {
        Log.e("Callback :: ", "onCancel");
    }

    // 로그인 실패 시에 호출됩니다.
    @Override
    public void onError(FacebookException error) {
        Log.e("Callback :: ", "onError : " + error.getMessage());
    }

}
