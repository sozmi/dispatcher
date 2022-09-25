package com.mishasozontov.dispetcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yandex.mapkit.MapKitFactory;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = BuildConfig.ApiKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("Ваш API-ключ");
        setContentView(R.layout.activity_main);
    }
}