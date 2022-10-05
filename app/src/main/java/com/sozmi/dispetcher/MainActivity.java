package com.sozmi.dispetcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map.setApi();
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_view, mapFragment);
        fTrans.commit();
    }
}