package com.sozmi.dispetcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.yandex.mapkit.MapKitFactory;


public class MainActivity extends AppCompatActivity {
    protected static final String MAPKIT_API_KEY = BuildConfig.ApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_view, mapFragment);
        fTrans.commit();
    }


}