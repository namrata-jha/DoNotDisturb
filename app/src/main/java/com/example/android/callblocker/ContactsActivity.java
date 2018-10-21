package com.example.android.callblocker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Objects;

public class ContactsActivity extends AppCompatActivity {

    private static final int SELECT_PHONE_NUMBER = 1;
    private MyDatabaseHelper contactData;
    TextView emptyListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        emptyListText = findViewById(R.id.empty_list);

        contactData = new MyDatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = contactData.getAll();
        if(!cursor.moveToFirst())
            emptyListText.setVisibility(View.VISIBLE);

        else {
            emptyListText.setVisibility(View.INVISIBLE);
            MyContactAdapter contactAdapter = new MyContactAdapter(this, cursor, true);
            ListView listView = findViewById(R.id.contactList);
            listView.setAdapter(contactAdapter);
        }
    }

    public void onClickAdd(View view) {

        Intent i= new Intent(Intent.ACTION_PICK);
        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(i, SELECT_PHONE_NUMBER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
//                    null, null, null);
            Cursor cursor = this.getContentResolver().query(contactUri, projection, null,null,null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = Objects.requireNonNull(cursor).getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);
                // Do something with the phone number
                contactData.insertData(name, number);
            }

            Objects.requireNonNull(cursor).close();
        }


    }
}
