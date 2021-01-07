package com.example.cs496_week2_client.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_week2_client.R;

import org.w3c.dom.Text;

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


/*
class PhoneBookShowActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_book_show)

        val phoneBookShowImage: ImageView = phone_show_image
        val phoneBookShowName: TextView = phone_show_name
        val phoneBookShowNum: TextView = phone_show_phone_number

        val bundle: Bundle = intent.extras!!

        val profileImage = bundle.getInt("ProfileImage")
        val colorFilter = bundle.getString("ColorFilter")
        val name = bundle.getString("Name")
        val phoneNumber = bundle.getString("PhoneNumber")

        phoneBookShowImage.setImageResource(profileImage)
        phoneBookShowImage.setColorFilter(Color.parseColor(colorFilter))
        phoneBookShowName.text = name
        phoneBookShowNum.text = phoneNumber

        phone_show_phone_btn.setOnClickListener{
        val uri = Uri.parse("tel:${phoneNumber.toString()}")
        val intent = Intent(Intent.ACTION_CALL, uri)
        startActivity(intent)
        }
        }
        }

 */