package com.example.cs496_week2_client.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.Group;
import com.example.cs496_week2_client.util.ResponseCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class JoinGroupActivity extends AppCompatActivity {
    EditText groupCode, groupName;
    Button submitButton;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        userService = Api.getInstance().getUserService();

        // Find views
        groupCode = (EditText) findViewById(R.id.code);
        groupName = (EditText) findViewById(R.id.group_name);
        submitButton = (Button) findViewById(R.id.submit_button);

        // Set button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String id = bundle.get("userId").toString();
                if (groupCode.getText() != null) {
                    joinGroup(id, groupCode.getText().toString());
                } else if (groupName.getText() != null) {
                    createGroup(id, groupName.getText().toString());
                    Toast.makeText(JoinGroupActivity.this, "그룹이 생성되었습니다.", Toast.LENGTH_SHORT);
                }
                finish();
            }
        });
    }

    private void joinGroup(String userId, String code) {
        Log.i("joinGroupActivity", "그룹에 들어가는 중");
        userService.group.joinGroup(code, userId).enqueue(new Callback<Group>() {
            // 토큰 검증 결과에 따라 로그 출력
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Group> call, Response<Group> response) {
                Log.i("joinGroupActivity", response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "그룹에 가입되었습니다!", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "그룹 코드를 다시 확인해주세요!", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Group> call, Throwable t) {
                Log.d("JoinGroupActivity", "joinGroup (group/join/{code}/{userId}) API 호출에 실패했습니다");
                t.printStackTrace();
                setResult(ResponseCode.SERVER_FAILURE);
            }
        });
    }

    private void createGroup(String id, String name) {
        userService.group.createGroup(name, id).enqueue(new Callback<Group>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Group> call, Response<Group> response) {
                Log.i("JoinGroupActivity", response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "그룹을 생성하고, 가입했습니다", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "그룹을 생성하지 못했습니다", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Group> call, Throwable t) {
                Log.d("JoinGroupActivity", "createGroup (group/create/{name}/{userId}) API 호출에 실패했습니다");
                t.printStackTrace();
                setResult(ResponseCode.SERVER_FAILURE);
            }
        });
    }
}