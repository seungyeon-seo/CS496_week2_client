package com.example.cs496_week2_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.ui.contacts.ContactFragment;
import com.example.cs496_week2_client.ui.login.LoginActivity;
import com.example.cs496_week2_client.ui.login.RegisterActivity;
import com.example.cs496_week2_client.ui.map.MapsFragment;
import com.example.cs496_week2_client.ui.my_page.MyPageFragment;
import com.example.cs496_week2_client.util.RequestCode;
import com.example.cs496_week2_client.util.ResponseCode;
import com.example.cs496_week2_client.util.UserUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {
    /* Permission variables */
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };
    public int[] grandResults = {-1, -1, -1, -1, -1, -1, -1, -1, -1};

    /* Tab variables */
    Fragment fragment1, fragment2, fragment3;
    User user;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Require Permission
        GetPermission();
        onRequestPermissionsResult(PERMISSIONS_REQUEST_CODE, REQUIRED_PERMISSIONS, grandResults);

        setContentView(R.layout.activity_main);

        // TODO LoginActivity, RegisterActivity 잘 실행되는지 확인
        // TODO 서버에서 fakeVerifyToken 을 verifyToken 으로 바꾸기

//        // 로그인 액티비티 실행
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, RequestCode.LOGIN_REQUEST_CODE);

        // 로그인 없이 실행
//        User fakeUser = UserUtils.getFakeUser();
//        String fakeToken = "fake token";
//
//        initViewPager(fakeUser, fakeToken);
        super.onCreate(savedInstanceState);
    }

    private void initTabLayout(User user, String token) {
        // TabLayout Initialization
        TabLayout tabLayout = findViewById(R.id.tabs);
        this.user = user;
        this.token = token;

        // ViewPager Initialization
        fragment1 = ContactFragment.newInstance(user, token);
        fragment2 = MapsFragment.newInstance(user, token);
        fragment3 = MyPageFragment.newInstance(user, token);

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        tabLayout.addTab(tabLayout.newTab().setText("Contact"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.addTab(tabLayout.newTab().setText("Feed"));

        // TabSelectedListener Initialization
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setViewPager(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void setViewPager(int pos) {
        Fragment selected = null;
        switch (pos) {
            case 0:
            default:
                selected = fragment1;
                break;
            case 1:
                selected = fragment2;
                break;
            case 2:
                selected = fragment3;
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
    }

    public void setFragment2() {
        fragment2 = MapsFragment.newInstance(user, token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.LOGIN_REQUEST_CODE:
                handleLoginActivityResult(resultCode, data);
                break;
            case RequestCode.REGISTER_REQUEST_CODE:
                handleRegisterActivityResult(resultCode, data);
                break;
            default:
                Log.e("MainActivity", "해당 requestCode 는 존재하지 않습니다 : " + requestCode);
        }
    }

    private void handleLoginActivityResult(int resultCode, Intent data) {
        switch (resultCode) {
            case ResponseCode.LOGIN_SUCCESSFUL:
                String token = UserUtils.parseUserIntentGetToken(data);
                User user = UserUtils.parseUserIntent(data);
                Log.i("MainActivity", "Login 성공 - nickName: " + user.getNickName());
                initTabLayout(user, token);
                break;
            case ResponseCode.REGISTER_REQUIRED: // Register Activity 시작
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivityForResult(registerIntent, RequestCode.REGISTER_REQUEST_CODE);
                break;
            default:
                Log.e("MainActivity", "LoginActivity 실패");
                break;
        }
    }

    private void handleRegisterActivityResult(int resultCode, Intent data) {
        if (resultCode == ResponseCode.REGISTER_SUCCESSFUL) {
            String token = UserUtils.parseUserIntentGetToken(data);
            User user = UserUtils.parseUserIntent(data);
            Log.i("MainActivity", "Register 성공 - nickName: " + user.getNickName());
            initTabLayout(user, token);
        } else {
            Log.e("MainActivity", "Register 실패");
            finish();
        }
    }

    public void GetPermission() {
        LinearLayout mLayout = findViewById(R.id.main_layout);
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[0]);
        int readContactPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[1]);
        int readCallPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[2]);
        int cameraPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[3]);
        int statePermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[4]);
        int writeContactPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[5]);
        int writeExternalStorage = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[6]);
        int coarseLocation = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[7]);
        int fineLocation = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[8]);

        grandResults[0] = readExternalStoragePermission;
        grandResults[1] = readContactPermission;
        grandResults[2] = readCallPermission;
        grandResults[3] = cameraPermission;
        grandResults[4] = statePermission;
        grandResults[5] = writeContactPermission;
        grandResults[6] = writeExternalStorage;
        grandResults[7] = coarseLocation;
        grandResults[8] = fineLocation;

        if (!(grandResults[0] == PackageManager.PERMISSION_GRANTED
                && grandResults[1] == PackageManager.PERMISSION_GRANTED
                && grandResults[2] == PackageManager.PERMISSION_GRANTED
                && grandResults[3] == PackageManager.PERMISSION_GRANTED
                && grandResults[4] == PackageManager.PERMISSION_GRANTED
                && grandResults[5] == PackageManager.PERMISSION_GRANTED
                && grandResults[6] == PackageManager.PERMISSION_GRANTED
                && grandResults[7] == PackageManager.PERMISSION_GRANTED
                && grandResults[8] == PackageManager.PERMISSION_GRANTED )) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[4])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[5])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[6])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[7])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[8])) {
                Snackbar.make(mLayout, "이 앱을 실행하려면 외부 저장소, 연락처, 전화 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", view -> {
                            // 3-3. 사용자에게 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                            ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                                    PERMISSIONS_REQUEST_CODE);
                        }).show();
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        LinearLayout mLayout = findViewById(R.id.main_layout);
        if (requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (!check_result) {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[3])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[4])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[5])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[6])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[7])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[8])) {

                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(findViewById(R.id.main_layout), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", view -> finish()).show();
                } else {
                    // “다시 묻지 않음”을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(findViewById(R.id.main_layout), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", view -> finish()).show();
                }
            }
        }
    }
}