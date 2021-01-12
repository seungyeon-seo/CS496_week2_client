package com.example.cs496_week2_client.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.cs496_week2_client.MainActivity;
import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.ui.my_page.MyPageFragment;
import com.example.cs496_week2_client.util.UserStatusCode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// TODO 뷰모델 만들어서 group/members API로 받아온 member list 받아오도록 하기
// TODO marker 위에 유저 프로필 사진 glide로 받아오기

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

import static com.example.cs496_week2_client.util.UserUtils.getUserBundle;
import static com.example.cs496_week2_client.util.UserUtils.parseUserBundleGetUser;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MapsFragment extends Fragment {
    Location myLocation;
    FusedLocationProviderClient mFusedLocationClient;
    LocationDataService dataService;
    User user;
    FloatingActionButton statusButton, statusFood, statusSleep, statusStudy;
    boolean isFabOpen = false;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.i("MapsFragment", "onMapReady");
            getMyLocation(googleMap);
        }
    };

//    public static MapsFragment newInstance() {
//        MapsFragment fragment = new MapsFragment();
//        return fragment;
//    }

    public static MapsFragment newInstance(User user, String token) {
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.setArguments(getUserBundle(user, token));
        return mapsFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        dataService = Api.getInstance().getLocationDataService();
        myLocation = new Location(LocationManager.GPS_PROVIDER);
        user = parseUserBundleGetUser(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("MapsFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Init status button
        statusButton = (FloatingActionButton) view.findViewById(R.id.statusButton);
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();
            }
        });
        statusFood = (FloatingActionButton) view.findViewById(R.id.status_food);
        statusFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(UserStatusCode.EAT);
            }
        });
        statusStudy = (FloatingActionButton) view.findViewById(R.id.status_study);
        statusStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(UserStatusCode.STUDY);
            }
        });
        statusSleep = (FloatingActionButton) view.findViewById(R.id.status_sleep);
        statusSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(UserStatusCode.SLEEP);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("MapsFragment", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void getMyLocation(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
//            MarkerOptions markerOptions = new MarkerOptions();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
//            markerOptions.position(latLng);
//            markerOptions.title("내 위치");
//
//
//            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14F));
//            myLocation = location;
            uploadLocation(latitude, longitude);
            getMemberLocation(map);
        });
    }

    private void uploadLocation(Double latitude, Double longitude) {
        dataService.location.setUserLocation(user.getId(), latitude.toString(), longitude.toString()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.e("SetLocation", "response is not successful");
                    return;
                }
                user = response.body();
                if (user != null) {
                    Log.i("SetLocation", "success " + response.message());
                } else Log.e("SetLocation", "user is null");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("SetLocation", "Fail to set location");
            }
        });
    }

    private void getMemberLocation(GoogleMap map) {
        dataService.location.getMembers(user.getGroupCode()).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (!response.isSuccessful()) {
                    Log.e("MapsFragment", "getMembers response is not successful");
                    Toast.makeText(getContext(), "멤버들의 정보를 불러올 수 없습니다", Toast.LENGTH_SHORT);
                    return;
                }

                Log.i("MapsFragment", "getMembers: " + response.body());

                ArrayList<User> users = response.body();
                for (int i = 0; users != null && i < users.size(); i++) {
                    User curUser = users.get(i);

                    if (curUser.getLatitude() == null || curUser.getLongitude() == null)
                        continue;

                    String url = "http://192.249.18.231/image/" + user.getProfilePath();
                    Log.i("MapsFragment", url);

                    Glide.with(getApplicationContext()).asBitmap().load(url)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    double latitude = Double.parseDouble(curUser.getLatitude());
                                    double longitude = Double.parseDouble(curUser.getLongitude());

                                    LatLng latLng = new LatLng(latitude, longitude);
                                    Log.i("getMemberLocation", curUser.getNickName());
                                    final float scale = getContext().getResources().getDisplayMetrics().density;
                                    int pixels = (int) (50 * scale + 0.5f);
                                    Bitmap bitmap = getCroppedBitmap(Bitmap.createScaledBitmap(resource, pixels, pixels, true));

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.snippet(getStatusText(curUser.getStatus()));
                                    markerOptions.position(latLng);
                                    markerOptions.title(curUser.getNickName());
                                    markerOptions
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                    map.addMarker(markerOptions);
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e("MapsFragment", "Fail to get members");
                Toast.makeText(getContext(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT);
            }
        });
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }


    private void toggleFab() {
        if (isFabOpen) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
            statusFood.startAnimation(animation);
            statusStudy.startAnimation(animation);
            statusSleep.startAnimation(animation);
            statusFood.setClickable(false);
            statusStudy.setClickable(false);
            statusSleep.setClickable(false);
            isFabOpen = false;
        } else {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
            statusFood.startAnimation(animation);
            statusStudy.startAnimation(animation);
            statusSleep.startAnimation(animation);
            statusFood.setClickable(true);
            statusStudy.setClickable(true);
            statusSleep.setClickable(true);
            isFabOpen = true;
        }
    }

    private void setStatus(int stat) {
        /* Status Code
         *   0: food
         *   1: study
         *   2: sleep     */
        dataService.location.setUserStatus(user.getId(), String.valueOf(stat)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.e("Set Status", "response is not successful");
                    return;
                }
                user = response.body();
                MainActivity main = (MainActivity) getActivity();
                main.setFragment2();
                Log.i("Set Status", user.getNickName() + "'s status is set " + user.getStatus());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Set Status", "Failed");
                t.printStackTrace();
            }
        });
    }

    private String getStatusText(String status) {
        if (status == null) return "아무것도 안하는 중";
        int statusCode = Integer.parseInt(status);
        switch (statusCode) {
            case UserStatusCode.EAT:
                return "밥먹는 중";

            case UserStatusCode.STUDY:
                return "공부 중";

            case UserStatusCode.SLEEP:
                return "자는 중";

            default:
                return "아무것도 안하는 중";
        }
    }
}