package com.example.icomman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // boundaidlserver
    }

    public static List<Person> getPersonList() {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            personList.add(new Person("name " + i, i));
            Log.i(TAG, "Position is: " + i);
        }
        return personList;
    }
}
