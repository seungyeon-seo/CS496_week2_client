package com.example.cs496_week2_client.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.cs496_week2_client.ui.gallery.GalleryService;
import com.example.cs496_week2_client.ui.login.UserService;
import com.example.cs496_week2_client.ui.feed.PostService;

// Retrofit 인스턴스를 전체 앱에서 한 번만 만들기 위한 Factory class (ViewModelProvider 같은 느낌)
public class Api {
    private static Api instance = null;
    public static final String BASE_URL = "http://192.249.18.231/";

    // retrofit 을 이용해서 만드는 서비스 (서버의 routes 에 해당)
    private com.example.cs496_week2_client.ui.feed.PostService PostService; // 게시글 CRUD
    private UserService UserService; // 로그인 등 인증 관련
    private GalleryService GalleryService; // 이미지 업로드 등 갤러리 관련


    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }

        return instance;
    }

    private Api() {
        buildRetrofit(BASE_URL);
    }

    private void buildRetrofit(String BASE_URL) {
        Retrofit retrofitClient =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(new OkHttpClient.Builder().build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        // 서비스 초기화
        this.PostService = new PostService(retrofitClient);
        this.UserService = new UserService(retrofitClient);
        this.GalleryService = new GalleryService(retrofitClient);
    }

    public PostService getPostService() {
        return this.PostService;
    }

    public UserService getUserService() {
        return this.UserService;
    }

    public com.example.cs496_week2_client.ui.gallery.GalleryService getGalleryService() {
        return GalleryService;
    }
}
