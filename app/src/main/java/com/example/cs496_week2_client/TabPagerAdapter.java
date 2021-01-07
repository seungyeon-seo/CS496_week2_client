package com.example.cs496_week2_client;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cs496_week2_client.chat.ChatFragment;
import com.example.cs496_week2_client.Gallery.GalleryFragment;
import com.example.cs496_week2_client.contacts.ContactFragment;

public class TabPagerAdapter extends FragmentStateAdapter {
    private int tabCount;

    public TabPagerAdapter(FragmentActivity fa, int count) {
        super(fa);
        tabCount = count;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
            default:
                return ContactFragment.newInstance();
            case 1:
                return GalleryFragment.newInstance();
            case 2:
                return ChatFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }
}
