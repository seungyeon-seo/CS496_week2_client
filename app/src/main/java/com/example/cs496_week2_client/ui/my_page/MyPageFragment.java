package com.example.cs496_week2_client.ui.my_page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.models.User;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import static com.example.cs496_week2_client.util.UserUtils.getUserBundle;
import static com.example.cs496_week2_client.util.UserUtils.parseUserBundleGetUser;

public class MyPageFragment extends Fragment {
    TextView nameView, numView, leaveButton;
    ShapeableImageView preview;
    
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

    // temporary function
//    public static MyPageFragment newInstance(User user, String token) {
//        return new MyPageFragment();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = parseUserBundleGetUser(getArguments());
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
        // TODO user 객체 정보로부터 뷰 설정하기.setText("제니");
        numView.setText("010-1234-5678");
        Glide.with(this).load("http://img.wkorea.com/w/2020/01/style_5e28242002d20-539x700.jpg").into(preview);

        // Set leave button
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyPageFragment", "leave button clicked");
                // TODO UserService 의 exitGroup 호출, LoginActivity 로 이동시키기
                // TODO 서버에서 exit 한 다음 바로 join 이나 create 가능한지 확인하기
            }
        });

        return view;
    }
}