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
import com.example.cs496_week2_client.models.MemLocation;
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

// TODO gms 모듈 gradle 파일에 추가 
// TODO 뷰모델 만들어서 group/members API로 받아온 member list 받아오도록 하기
// TODO member list 데이터 가공해서 marker 지도에 표시하기
// TODO marker 위에 유저 프로필 사진 glide로 받아오기

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {
    Location myLocation;
    FusedLocationProviderClient mFusedLocationClient;
    LocationDataService dataService;

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

    public static Fragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        HashMap<String, Object> input = new HashMap<>();
        input.put("location", myLocation.getProvider());
        input.put("name", "제니"); //TODO : user의 이름 받아와서 넣어주기
        dataService.update.setLocation(input).enqueue(new Callback<MemLocation>() {
            @Override
            public void onResponse(Call<MemLocation> call, Response<MemLocation> response) {
                if (!response.isSuccessful()) {
                    Log.i("SetLocation", "response is not successful");
                    return;
                }
                MemLocation memLocation = response.body();
                if (memLocation != null) {
                    Log.i("SetLocation", "success " + response.message());
                }
                else Log.i("SetLocation", "memlocation is null");
            }

            @Override
            public void onFailure(Call<MemLocation> call, Throwable t) {
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