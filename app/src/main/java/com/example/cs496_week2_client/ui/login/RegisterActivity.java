package com.example.cs496_week2_client.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.util.ResponseCode;
import com.example.cs496_week2_client.util.UserUtils;
import com.facebook.AccessToken;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button submitButton = findViewById(R.id.nickname_submit_button);
        EditText nicknameEditText = findViewById(R.id.nickname_edit_text);
        ImageView imagePreview = findViewById(R.id.image_preview);
        Button imageSelectButton = findViewById(R.id.image_select_button);

        userService = Api.getInstance().getUserService();

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

                        userService.auth.register(token, nickname).enqueue(new Callback<User>() {
                            // 토큰 검증 결과에 따라 로그 출력
                            @Override
                            @EverythingIsNonNull
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "안녕하세요.", Toast.LENGTH_SHORT)
                                            .show();
                                    User user = response.body();
                                    setResult(ResponseCode.REGISTER_SUCCESSFUL, UserUtils.getUserIntent(user.getId(), user.getNickName(), user.getPosts(), token));
                                    postImage();
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

        imageSelectButton.setOnClickListener(v -> selectImage());


    }

    private void selectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, RequestCode.SELECT_FROM_GALLERY);
    }


    public void postImage() {
        RequestBody reqfile = RequestBody.create(MediaType.parse("image/*"), imageData);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", "abc", reqfile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
        Call<ResponseBody> req = userService.image.postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(getApplicationContext(), response.code() + " onResponse", Toast.LENGTH_SHORT).show();
                        }

                        @Override
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

        if (requestCode != RequestCode.SELECT_FROM_GALLERY)
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
}