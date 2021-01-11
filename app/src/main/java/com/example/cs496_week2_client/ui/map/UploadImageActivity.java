//package com.example.cs496_week2_client.ui.feed;
//
//import android.content.ContentValues;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.cs496_week2_client.R;
//import com.example.cs496_week2_client.api.Api;
//import com.example.cs496_week2_client.ui.login.PostService;
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class UploadImageActivity extends AppCompatActivity {
//    PostService postService;
//    TextView resultMessage;
//    TextView filename;
//    ImageView imagePreview;
//    Uri selectedUri;
//    byte[] imageData;
//    Uri imageUri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_image);
//        resultMessage = findViewById(R.id.result_message);
//        imagePreview = findViewById(R.id.image_preview);
//        filename = findViewById(R.id.file_name);
//
//        Button imageUploadButton = findViewById(R.id.image_upload_button);
//        Button imageSelectButton = findViewById(R.id.image_select_button);
//
//        postService = Api.getInstance().getPostService();
//
////        imageUploadButton.
//
//        // 버튼을 클릭하면 이미지를 선택할 수 있는 Dialog 띄움
//        imageSelectButton.setOnClickListener(v -> selectImage());
//
//        // 버튼을 클릭하면 이미지를 업로드
//        imageUploadButton.setOnClickListener(view -> postImage());
//    }
//
//    public void postImage() {
//        RequestBody reqfile = RequestBody.create(MediaType.parse("image/*"), imageData);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", "abc", reqfile);
//        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
//        Call<ResponseBody> req = postService.image.postImage(body, name);
//        req.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            if (response.isSuccessful()) {
//                                resultMessage.setText("Uploaded");
//                                resultMessage.setTextColor(Color.BLUE);
//                            }
//                            Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
////                            finish();
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            resultMessage.setText("Upload Failed!");
//                            resultMessage.setTextColor(Color.RED);
//                            Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
//                            t.printStackTrace();
//                        }
//                    }
//        );
//    }
//
//    private void selectImage() {
//        final CharSequence[] options = {"사진 찍기", "갤러리에서 선택하기", "취소"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(UploadImageActivity.this);
//        builder.setTitle("프로필 사진을 선택하세요!");
//        builder.setItems(options, (dialog, item) -> {
//            ContentValues values = new ContentValues();
//
//
//            if (options[item].equals("사진 찍기")) {
//                values.put(MediaStore.Images.Media.TITLE, "MyPicture");
//                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
//                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(takePicture, RequestCode.TAKE_PICTURE);
//            } else if (options[item].equals("갤러리에서 선택하기")) {
//                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pickPhoto, RequestCode.SELECT_FROM_GALLERY);
//            } else if (options[item].equals("취소")) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (data == null)
//            Log.i("UploadImageActivity", "인텐트가 안왔습니다");
//        else if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case (RequestCode.TAKE_PICTURE): {
//                    Log.i("UploadImageActivity", "사진을 찍었습니다");
////                    Bitmap selectedImage;
////                    Bundle bundle = data.getExtras();
////                    Bitmap bitmap = (Bitmap) bundle.get("data");
////                    Uri ChangedUri = BitmapToUri(this.requireContext(), bitmap);
////                    FileList.add(new ImageUnit(ChangedUri));
//
////                    try {
////                        selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
////                        imagePreview.setImageBitmap(selectedImage);
////                    } catch (IOException e) {
////                        Log.e("UploadImageActivity", "파일이 존재하지 않습니다");
////                        e.printStackTrace();
////                    }
//                }
//                break;
//                case (RequestCode.SELECT_FROM_GALLERY): {
//                    Log.i("UploadImageActivity", "갤러리에서 사진 선택했습니다");
//                    Uri selectedImage = data.getData();
//                    selectedUri = selectedImage;
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
//                        byte[] buffer = new byte[1024];
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(buffer.length);
//                        int len = 0;
//                        while ((len = inputStream.read(buffer)) != -1) {
//                            byteArrayOutputStream.write(buffer, 0, len);
//                        }
//                        imageData = byteArrayOutputStream.toByteArray();
//                        Log.i("UploadImageActivity", "이미지 데이터 만듦 " + imageData.length);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//
//                default:
//                    throw new IllegalStateException("Unexpected value: " + requestCode);
//            }
//        } else Log.i("UploadImageActivity", "ResultCode: " + resultCode);
//
//    }
//
//
//}
//
//class RequestCode {
//    static final int TAKE_PICTURE = 0;
//    static final int SELECT_FROM_GALLERY = 1;
//}