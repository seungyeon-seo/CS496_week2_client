package com.example.cs496_week2_client.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cs496_week2_client.R;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.util.UserStatusCode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.imageview.ShapeableImageView;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    User user;
    Fragment fragment;

    public MarkerInfoWindowAdapter(Context context, User user, Fragment fragment) {
        this.context = context.getApplicationContext();
        this.user = user;
        this.fragment = fragment;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.map_marker_popup, null);
        TextView status = v.findViewById(R.id.status);
        ShapeableImageView profile = v.findViewById(R.id.profile_image);

        status.setText(getStatusText(user.getStatus()));
        Glide.with(fragment).load("http://192.249.18.231/image/" + user.getProfilePath()).into(profile);
        return v;
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

