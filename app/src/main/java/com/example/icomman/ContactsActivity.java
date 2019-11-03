package com.example.icomman;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    ListView listView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.contactsList);

        checkContactsPermission();

    }

    public void checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactsActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 001);
        } else {
            fetchContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 001:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Contacts Permission Granted!", Toast.LENGTH_SHORT).show();
                    fetchContacts();
                }
        }
    }

    public void fetchContacts() {

        ArrayList<String> contacts = new ArrayList<>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = "";
        String[] selectionArgs = null;
        String sortOrder = "";

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contacts.add(name + "\n" + num);

        }

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts));

    }

}
