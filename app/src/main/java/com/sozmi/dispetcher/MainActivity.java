package com.sozmi.dispetcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.PlacemarkMapObject;


public class MainActivity extends AppCompatActivity {
    ImageButton add_building;
    Button cancel_building;
    FrameLayout panel_building;
    PlacemarkMapObject mapObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map.setApi();
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_view, mapFragment);
        fTrans.commit();
        add_building  = findViewById(R.id.add_button);
        cancel_building  = findViewById(R.id.cancel_button);
        panel_building  = findViewById(R.id.panel_build);
        add_building.setOnClickListener(view ->AddBuilding());
        cancel_building.setOnClickListener(view ->CancelBuilding());
    }

    private void AddBuilding(){
        mapObject =Map.addMarker(new Point(0,0),TypeBuilding.none);
        add_building.setVisibility(View.INVISIBLE);
        panel_building.setVisibility(View.VISIBLE);
    }

    private void CancelBuilding(){
        Map.delMarker(mapObject);
        add_building.setVisibility(View.VISIBLE);
        panel_building.setVisibility(View.INVISIBLE);
    }
}