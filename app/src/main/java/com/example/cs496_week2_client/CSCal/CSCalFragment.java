package com.example.cs496_week2_client.CSCal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class CSCalFragment extends Fragment {

    public CSCalFragment() {
    }

    public static CSCalFragment newInstance() {
        CSCalFragment fragment = new CSCalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
