package com.sozmi.dispetcher.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.sozmi.dispetcher.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Intent activity;
        if(IsAuth()) {
            activity = new Intent(LaunchActivity.this, MainActivity.class);
        }
        else {
            activity = new Intent(LaunchActivity.this, LoginActivity.class);
        }
        startActivity(activity);
    }

    @SuppressWarnings("SameReturnValue")
    private boolean IsAuth(){
        //TODO дописать проверку сохранен ли пароль и верен ли
        return false;
    }
}