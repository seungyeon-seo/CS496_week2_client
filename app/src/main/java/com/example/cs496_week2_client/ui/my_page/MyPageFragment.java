package com.example.cs496_week2_client.ui.my_page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.example.cs496_week2_client.MainActivity;
import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.ui.login.JoinGroupActivity;
import com.example.cs496_week2_client.ui.login.LoginActivity;
import com.example.cs496_week2_client.ui.login.RegisterActivity;
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
    Button inviteButton;
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
        if (user.getGroupCode() == "0") {
            Intent intent = new Intent(getContext(), JoinGroupActivity.class);
            intent.putExtra("userId", user.getId());
            startActivityForResult(intent, 1001);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            MainActivity main = (MainActivity) getActivity();
            main.setFragment2();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        // Find views
        nameView = view.findViewById(R.id.nickname_edit_text);
        numView = view.findViewById(R.id.phone_num);
        preview = view.findViewById(R.id.image_preview);
        leaveButton = view.findViewById(R.id.leave_button);
        if (user.getGroupCode() == "0") leaveButton.setText("그룹 가입");
        else leaveButton.setText("그룹 탈퇴");
        inviteButton = view.findViewById(R.id.group_invite_button);
        // Set views using user information
        nameView.setText(user.getNickName());
        numView.setText(user.getPhone());
        inviteButton.setText("초대하기 (코드: " + user.getGroupCode() + ")");
        Glide.with(this).load("http://192.249.18.231/image/" + user.getProfilePath()).into(preview);

        // Set leave button
        leaveButton.setOnClickListener(v -> {
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
                    Intent intent = new Intent(getContext(), JoinGroupActivity.class);
                    intent.putExtra("userId", user.getId());
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("MyPageFragment", "Failed to exit group");
                    Toast.makeText(getContext(), "그룹 탈퇴 실패", Toast.LENGTH_SHORT);
                }
            });
            // TODO 서버에서 exit 한 다음 바로 join 이나 create 가능한지 확인하기
        });

        inviteButton.setOnClickListener(v -> {
            try {
                String message = "[어디야? 뭐해?]\n" + user.getNickName() + "님의 초대가 도착했어요!\n초대 코드: " + user.getGroupCode();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setPackage("com.kakao.talk");
                this.startActivity(intent);
            } catch (Exception e) {
                // 카카오톡 미설치 에러
                AlertDialog.Builder kakaoAlertBuilder = new AlertDialog.Builder(getContext());
                kakaoAlertBuilder.setTitle("공유할 수 없음");
                kakaoAlertBuilder.setMessage("이 기기에 카카오톡이 설치되어 있지 않습니다.\n설치하시겠습니까?");
                kakaoAlertBuilder.setPositiveButton("예", (dialog, which) -> {
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    installIntent.setData(Uri.parse("market://details?id=com.kakao.talk"));
                    startActivity(installIntent);
                });

                kakaoAlertBuilder.setNegativeButton("아니오", (dialog, which) -> {
                    Toast.makeText(getContext(), "취소함", Toast.LENGTH_SHORT).show();
                });

                kakaoAlertBuilder.create().show();
            }
        });

        return view;
    }
}