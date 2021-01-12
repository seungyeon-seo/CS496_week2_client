package com.example.cs496_week2_client.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_week2_client.R;

public class ContactInfoActivity extends AppCompatActivity {
    ImageView imageView;
    TextView nameView;
    TextView numberView;
    ImageButton callButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        // Init views
        imageView = findViewById(R.id.phone_show_image);
        nameView = findViewById(R.id.phone_show_name);
        numberView = findViewById(R.id.phone_show_phone_number);
        callButton = findViewById(R.id.phone_show_phone_btn);

        // Get data from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String image = bundle.getString("Image");
        String name = bundle.getString("Name");
        String number = bundle.getString("Number");

        // Set data of views
        if (!image.equals("")) imageView.setImageURI(Uri.parse(image));
        nameView.setText(name);
        numberView.setText(number);

        // Init Call button
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

    }
}