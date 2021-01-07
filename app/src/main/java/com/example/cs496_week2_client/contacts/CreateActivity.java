package com.example.cs496_week2_client.contacts;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_week2_client.R;

import java.util.ArrayList;

public class CreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Store views
        EditText createName = findViewById(R.id.create_phone_name);
        EditText createNumber = findViewById(R.id.create_phone_number);
        Button createButton = findViewById(R.id.create_phone_submit);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get string from view
                String name = createName.getText().toString();
                String number = createNumber.getText().toString();

                // if some view is empty
                if (name == "" || number == "") {
                    Toast.makeText(getApplicationContext(),
                            "이름과 전화번호는 공백일 수 없습니다.", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                    onResume();
                }
                // normal input
                else {
                    ArrayList<ContentProviderOperation> ops =
                            new ArrayList<ContentProviderOperation>();
                    ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(
                            ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
                    ops.add(op.build());

                    op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                    ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
                    ops.add(op.build());

                    op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                    ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                            .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "LABEL?")
                            .withValue(
                                    ContactsContract.CommonDataKinds.Phone.TYPE,
                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                            );
                    ops.add(op.build());
                    try {
                        getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    } catch (OperationApplicationException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_LONG).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                    onResume();
                }
            }

        });
    }
}
