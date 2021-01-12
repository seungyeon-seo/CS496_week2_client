package com.example.cs496_week2_client.ui.map;

import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs496_week2_client.api.Api;
import com.example.cs496_week2_client.models.User;
import com.example.cs496_week2_client.ui.contacts.ContactService;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class MapViewModel extends ViewModel {
    public MutableLiveData<ArrayList<User>> members;
    Activity activity;
    LocationDataService locationDataService;
    User user;

    MapViewModel(Activity activity, User member) {
        this.activity = activity;
        this.user = user;

        locationDataService = Api.getInstance().getLocationDataService();
        members = new MutableLiveData<ArrayList<User>>();
        members.setValue(new ArrayList<User>());
        getMembers();
    }

    // 서버에서 같은 그룹 유저 정보 가져오기
    public void getMembers() {
        locationDataService.location.getMembers(user.getId()).enqueue(new Callback<ArrayList<User>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ArrayList<User>> call,
                                   Response<ArrayList<User>> response) {
                Log.i("GetContactServer", "Response");
                if (response.isSuccessful()) {
                    members.setValue(response.body());
                } else try {
                    throw new Exception("response is not successful");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.d("GetContactServer", "Failed to get data");
                t.printStackTrace();
            }

        });
    }
}


class MapViewModelFactory implements ViewModelProvider.Factory {
    Activity activity;
    User user;

    MapViewModelFactory(Application application, Activity activity, User user) {
        this.activity = activity;
        this.user = user;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(activity, user);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}