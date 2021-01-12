package com.example.cs496_week2_client.ui.my_page;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.ui.login.LoginActivity;
import com.example.cs496_week2_client.ui.login.UserService;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cs496_week2_client.util.UserUtils.getUserBundle;
import static com.example.cs496_week2_client.util.UserUtils.parseUserBundleGetUser;

public class MyPageFragment extends Fragment {
    TextView nameView, numView, leaveButton;
    ShapeableImageView preview;
    MyService dataService;

    User user;
    String token;

    public MyPageFragment() {
        // Required empty public constructor
    }

    public static MyPageFragment newInstance(User user, String token) {
        MyPageFragment myPageFragment = new MyPageFragment();
        myPageFragment.setArguments(getUserBundle(user, token));
        return myPageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = parseUserBundleGetUser(getArguments());
        dataService = Api.getInstance().getMyService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        // Find views
        nameView = (TextView) view.findViewById(R.id.nickname_edit_text);
        numView = (TextView) view.findViewById(R.id.phone_num);
        preview = (ShapeableImageView) view.findViewById(R.id.image_preview);
        leaveButton = (TextView) view.findViewById(R.id.leave_button);

        // Set views using user information
        nameView.setText(user.getNickName());
        numView.setText(user.getPhoneNum());
        Glide.with(this).load("http://192.249.18.231/image" + user.getProfilePath()).into(preview);

        // Set leave button
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyPageFragment", "leave button clicked");

                dataService.group.exitGroup(user.getGroupCode(), user.getId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            Log.e("MyPageFragment", "Exit Group Response is not successful");
                            Toast.makeText(getContext(), "그룹 탈퇴 실패", Toast.LENGTH_SHORT);
                            return;
                        }
                        Log.i("MyPageFragment", "Exit Group Response");
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("MyPageFragment", "Failed to exit group");
                        Toast.makeText(getContext(), "그룹 탈퇴 실패", Toast.LENGTH_SHORT);
                    }
                });
                // TODO (완료) UserService 의 exitGroup 호출, LoginActivity 로 이동시키기
                // TODO 서버에서 exit 한 다음 바로 join 이나 create 가능한지 확인하기
            }
        });

        return view;
    }
}