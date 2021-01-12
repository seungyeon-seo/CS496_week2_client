package com.example.cs496_week2_client.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.ui.my_page.MyPageFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

// TODO 뷰모델 만들어서 group/members API로 받아온 member list 받아오도록 하기
// TODO member list 데이터 가공해서 marker 지도에 표시하기
// TODO marker 위에 유저 프로필 사진 glide로 받아오기

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

import static com.example.cs496_week2_client.util.UserUtils.getUserBundle;
import static com.example.cs496_week2_client.util.UserUtils.parseUserBundleGetUser;

public class MapsFragment extends Fragment {
    Location myLocation;
    FusedLocationProviderClient mFusedLocationClient;
    LocationDataService dataService;
    User user;

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
        user = parseUserBundleGetUser(getArguments());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        dataService = new LocationDataService();
        myLocation = new Location(LocationManager.GPS_PROVIDER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("MapsFragment", "onCreateView");
        return inflater.inflate(R.layout.fragment_maps, container, false);
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
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                MarkerOptions markerOptions = new MarkerOptions();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                Log.i("getMyLocation", String.valueOf(latitude)+" "+String.valueOf(longitude));
                markerOptions.position(latLng);
                markerOptions.title("내 위치");
                map.addMarker(markerOptions);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14F));
                myLocation = location;
                //uploadLocation();
                getMemberLocation(map);
            }
        });
    }

    private void uploadLocation() {
        dataService.location.setUserStatusLocation(user.getId() , user.getStatus(), user.getLatitude(), user.getLongitude()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Log.i("SetLocation", "user: " + user + "\nmessage: " + response.message());
                    return;
                }
                else Log.i("SetLocation", "memlocation is null");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("SetLocation", "Fail to set location");
            }
        });
    }

    private void getMemberLocation(GoogleMap map) {
        // TODO: get locations from server (이건 test)
        MarkerOptions markerOptions = new MarkerOptions();

        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude() + 0.01;

        LatLng latLng = new LatLng(latitude, longitude);
        Log.i("getMemberLocation", String.valueOf(latitude)+" "+String.valueOf(longitude));
        markerOptions.position(latLng);
        markerOptions.title("provider");
        map.addMarker(markerOptions);
    }
}