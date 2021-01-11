package com.example.cs496_week2_client.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.Group;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.util.ResponseCode;
import com.example.cs496_week2_client.util.UserUtils;
import com.facebook.AccessToken;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;


public class RegisterActivity extends AppCompatActivity {
    UserService userService;
    Uri selectedUri;
    byte[] imageData;
    Uri imageUri;
    EditText nicknameEditText;
    EditText codeEditText;
    String userId;
    EditText groupNameEditText;

    TextInputLayout codeWrapper;
    TextInputLayout groupNameWrapper;

    TextView createGroupText;
    TextView joinGroupText;

    int mode = Mode.JOIN_GROUP;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userService = Api.getInstance().getUserService();

        Button submitButton = findViewById(R.id.nickname_submit_button);
        ImageView imagePreview = findViewById(R.id.image_preview);
        Button imageSelectButton = findViewById(R.id.image_select_button);

        nicknameEditText = findViewById(R.id.nickname_edit_text);
        codeEditText = findViewById(R.id.code);
        groupNameEditText = findViewById(R.id.group_name);
        groupNameWrapper = findViewById(R.id.group_name_wrapper);
        codeWrapper = findViewById(R.id.code_wrapper);

        createGroupText = findViewById(R.id.create_group);
        joinGroupText = findViewById(R.id.join_group);

        joinGroupViewSetup();
        joinGroupText.setOnClickListener(v -> joinGroupViewSetup());
        createGroupText.setOnClickListener(v -> createGroupViewSetup());

        imageSelectButton.setOnClickListener(v -> selectImage());

        submitButton.setOnClickListener(v -> {
            // TODO 필수 필드 입력 안하면 submit 버튼 못 누르게 하기

            if (imageData == null || imageData.length == 0) {
                Toast.makeText(getApplicationContext(), "프로필 사진을 선택해주세요!", Toast.LENGTH_SHORT)
                        .show();
            }
            else registerUser();
        });
    }

    private void joinGroupViewSetup() {
        codeWrapper.setVisibility(View.VISIBLE);
        groupNameWrapper.setVisibility(View.GONE);

        createGroupText.setVisibility(View.VISIBLE);
        joinGroupText.setVisibility(View.GONE);
        mode = Mode.JOIN_GROUP;
    }

    private void createGroupViewSetup() {
        codeWrapper.setVisibility(View.GONE);
        groupNameWrapper.setVisibility(View.VISIBLE);

        createGroupText.setVisibility(View.GONE);
        joinGroupText.setVisibility(View.VISIBLE);
        mode = Mode.CREATE_GROUP;
    }

    private String getToken() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.getToken() == null) {
            Log.e("RegisterActivity", "AccessToken 이 없으면 RegisterActivity 를 시작할 수 없습니다 ");
            finish();
        }
        assert accessToken != null;
        return accessToken.getToken();
    }

    private String getNickname() {
        return nicknameEditText.getText().toString();
    }

    private String getCode() {
        return codeEditText.getText().toString();
    }

    private void registerUser() {
        userService.auth.register(getToken(), getNickname()).enqueue(new Callback<User>() {
            // 토큰 검증 결과에 따라 로그 출력
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "안녕하세요.", Toast.LENGTH_SHORT)
                            .show();
                    User user = response.body();
                    assert user != null;
                    userId = user.getId();
                    
                    postImage(userId);
                    setResult(ResponseCode.REGISTER_SUCCESSFUL, UserUtils.getUserIntent(user.getId(), user.getNickName(), user.getPosts(), getToken()));
                } else if (response.code() == ResponseCode.HTTP_CONFLICT) {
                    Log.i("RegisterCallback", "이미 존재하는 유저입니다: " + response.body());
                    setResult(ResponseCode.REGISTER_FAILURE);
                } else {
                    Log.i("RegisterCallback", "유저 등록에 실패했습니다: " + response.body());
                    setResult(ResponseCode.REGISTER_FAILURE);
                }

                if (mode == Mode.CREATE_GROUP) createGroup(userId);
                else joinGroup(userId);
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

    private String getGroupName() {
        return groupNameEditText.getText().toString();
    }


    private void joinGroup(String userId) {
        Log.i("joinGroup", "그룹에 들어가는 중");
        userService.group.joinGroup(getCode(), userId).enqueue(new Callback<Group>() {
            // 토큰 검증 결과에 따라 로그 출력
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Group> call, Response<Group> response) {
                Log.i("joinGroup", response.message());
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
                Log.d("JoinGroup", "joinGroup (group/join/{code}/{userId}) API 호출에 실패했습니다");
                t.printStackTrace();
                setResult(ResponseCode.SERVER_FAILURE);
            }
        });
    }

    private void createGroup(String userId) {
        userService.group.createGroup(getGroupName(), userId).enqueue(new Callback<Group>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Group> call, Response<Group> response) {
                Log.i("createGroup", response.message());
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
                Log.d("CreateGroup", "createGroup (group/create/{name}/{userId}) API 호출에 실패했습니다");
                t.printStackTrace();
                setResult(ResponseCode.SERVER_FAILURE);
            }
        });
    }

    private void selectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, RequestCode.SELECT_FROM_GALLERY);
    }

    public void postImage(String userId) {
        RequestBody reqfile = RequestBody.create(MediaType.parse("image/*"), imageData);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", "abc", reqfile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
        Call<ResponseBody> req = userService.image.postImage(userId, body, name);
        req.enqueue(new Callback<ResponseBody>() {
                        @Override
                        @EverythingIsNonNull
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            // TODO glide 로 서버에서 받은 URL로 이미지를 imagePreview 에 띄우기
                            Toast.makeText(getApplicationContext(), response.code() + " onResponse", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        @EverythingIsNonNull
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.CREATE_GROUP && resultCode == ResponseCode.REGISTER_SUCCESSFUL) {
            finish();
        } else if (requestCode != RequestCode.SELECT_FROM_GALLERY)
            throw new IllegalStateException("Unexpected requestCode: " + requestCode);
        else if (data == null)
            Log.i("UploadImageActivity", "인텐트가 안왔습니다");
        else if (resultCode == RESULT_OK) {
            Log.i("UploadImageActivity", "갤러리에서 사진 선택했습니다");
            Uri selectedImage = data.getData();
            selectedUri = selectedImage;
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(buffer.length);
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                imageData = byteArrayOutputStream.toByteArray();
                Log.i("UploadImageActivity", "이미지 데이터 만듦 " + imageData.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else Log.i("UploadImageActivity", "액티비티가 취소되었거나 실패했습니다. ResultCode: " + resultCode);
    }
}

class RequestCode {
    static final int SELECT_FROM_GALLERY = 1;
    static final int CREATE_GROUP = 2;
}

class Mode {
    static final int JOIN_GROUP = 1;
    static final int CREATE_GROUP = 2;
}