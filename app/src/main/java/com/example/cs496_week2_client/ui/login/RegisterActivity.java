package com.example.cs496_week2_client.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.Post;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.util.ResponseCode;
import com.example.cs496_week2_client.util.UserUtils;
import com.facebook.AccessToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class RegisterActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Api api = Api.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button submitButton = findViewById(R.id.nickname_submit_button);
        EditText nicknameEditText = findViewById(R.id.nickname_edit_text);
        submitButton.setOnClickListener(
                v -> {
                    String nickname = nicknameEditText.getText().toString();
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();

                    if (accessToken == null || accessToken.getToken() == null) {
                        // SOMETHING WENT WRONG
                        try {
//                            Log.e("RegisterActivity", "AccessToken 이 없으면 RegisterActivity 를 시작할 수 없습니다 ");
                            throw new Exception("AccessToken 이 없으면 RegisterActivity 를 시작할 수 없습니다 ");
                        } catch (Exception e) {
                            e.printStackTrace();
                            finish();
                        }
                    } else {
                        String token = AccessToken.getCurrentAccessToken().getToken();
                        api.getUserService().auth.register(token, nickname).enqueue(new Callback<User>() {
                            // 토큰 검증 결과에 따라 로그 출력
                            @Override
                            @EverythingIsNonNull
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "안녕하세요.", Toast.LENGTH_SHORT)
                                            .show();
                                    User user = response.body();
                                    setResult(ResponseCode.REGISTER_SUCCESSFUL, UserUtils.getUserIntent(user.getId(), user.getNickName(), user.getPosts(), token));
                                    finish();
                                } else if (response.code() == ResponseCode.HTTP_CONFLICT) {
                                    Log.i("RegisterCallback", "이미 존재하는 유저입니다: " + response.body());
                                    setResult(ResponseCode.REGISTER_FAILURE);
                                } else {
                                    Log.i("RegisterCallback", "유저 등록에 실패했습니다: " + response.body());
                                    setResult(ResponseCode.REGISTER_FAILURE);
                                }
                            }

                            @Override
                            @EverythingIsNonNull
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.d("RegisterCallback", "Register (user/{token}/{nickname}) API 호출에 실패했습니다");
                                t.printStackTrace();
                                setResult(ResponseCode.SERVER_FAILURE);
                            }
                        });
                    }
                }
        );
    }
}
