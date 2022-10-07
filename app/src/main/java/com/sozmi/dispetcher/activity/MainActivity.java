package com.sozmi.dispetcher.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.sozmi.dispetcher.fragment.BuildFragment;
import com.sozmi.dispetcher.fragment.MapFragment;
import com.sozmi.dispetcher.R;
import com.sozmi.dispetcher.model.MyMap;
import com.sozmi.dispetcher.model.TypeBuilding;
import com.yandex.mapkit.map.PlacemarkMapObject;


public class MainActivity extends AppCompatActivity {
    ImageButton addBuildingButton;
    FrameLayout panel_building;
    PlacemarkMapObject mapObject;
    String _current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyMap.setApi();
        OpenFragment(new MapFragment(),"MapFragment");

        addBuildingButton = findViewById(R.id.addBuildingButton);
        Button cancelBuildButton  = findViewById(R.id.cancelBuildButton);
        ImageButton buildButton  = findViewById(R.id.buildButton);
        ImageButton mapButton  = findViewById(R.id.mapButton);

        panel_building  = findViewById(R.id.panel_build);

        addBuildingButton.setOnClickListener(view -> addBuildingButtonOnClick());
        buildButton.setOnClickListener(view -> buildButtonOnClick());
        cancelBuildButton.setOnClickListener(view -> cancelBuildButtonOnClick());
        mapButton.setOnClickListener(view -> mapButtonOnClick());
    }

    /**
     * Метод открывающий заменяющий текущий фрагмент нужным
     * @param fragment - фрагмент который будет отображаться
     * @param name - имя фрагмента
     */
    private void OpenFragment(Fragment fragment,String name){
        _current=name;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_view, fragment);
        ft.commit();
    }

    /**
     *Поведение при нажатии кнопки "Добавление нового здания" на карте
     */
    private void addBuildingButtonOnClick(){
        mapObject = MyMap.addMarker(MyMap.getTargetCamera(), TypeBuilding.none);
        addBuildingButton.setVisibility(View.INVISIBLE);
        panel_building.setVisibility(View.VISIBLE);
    }

    /**
     *Поведение при нажатии кнопки для вызова фрагмента строительства
     */
    private void buildButtonOnClick(){
        addBuildingButton.setVisibility(View.INVISIBLE);
        panel_building.setVisibility(View.INVISIBLE);
        OpenFragment(new BuildFragment(), "BuildFragment");
    }

    /**
     *Поведение при нажатии кнопки для вызова фрагмента карты
     */
    private void mapButtonOnClick(){
        addBuildingButton.setVisibility(View.VISIBLE);
        OpenFragment(new MapFragment(),"MapFragment");
    }

    /**
     *Поведение при нажатии кнопки отмены при выборе координат на карте
     */
    private void cancelBuildButtonOnClick(){
        MyMap.delMarker(mapObject);
        addBuildingButton.setVisibility(View.VISIBLE);
        panel_building.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed(){
        if(_current.equals("MapFragment"))
            super.onBackPressed();
        else
           mapButtonOnClick();
    }
}