package com.example.cs496_week2_client.ui.my_page;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cs496_week2_client.R;

public class TempFragment extends Fragment {
    ImageView imageView;

    TempFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.tempfragment, container, false);
        imageView = (ImageView) view.findViewById(R.id.tempView);
        Glide.with(this).load("http://img.wkorea.com/w/2020/01/style_5e28242002d20-539x700.jpg").into(imageView);
//        Glide.with(this).load("http://192.249.18.231:80/upload/abc1610285837269.JPEG").into(imageView);

        return view;
    }

    public static Fragment newInstance() {
        TempFragment frag = new TempFragment();
        return frag;
    }
}
