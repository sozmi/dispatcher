package com.sozmi.dispetcher;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

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

    private boolean IsAuth(){
        //TODO дописать проверку сохранен ли пароль и верен ли
        return 1!=1;
    }
}