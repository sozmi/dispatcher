package com.sozmi.dispatcher;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.sozmi.dispatcher.fragment.LoginFragment;
import com.sozmi.dispatcher.fragment.BuildingFragment;
import com.sozmi.dispatcher.fragment.BuildFragment;
import com.sozmi.dispatcher.fragment.MapFragment;
import com.sozmi.dispatcher.model.MyFM;
import com.sozmi.dispatcher.model.Server;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        if(MyFM.getCurrentName().equals("MapFragment") || MyFM.getCurrentName().equals("LoginFragment"))
            super.onBackPressed();
        else
            mapButtonOnClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyFM.setFM(getSupportFragmentManager());
        if(Server.isAuth())
            MyFM.OpenFragment(new MapFragment(false,true),"MapFragment");
        else
            MyFM.OpenFragment(new LoginFragment(),"LoginFragment");

        ImageButton mapButton  = findViewById(R.id.buttonMap);
        ImageButton buildingsButton = findViewById(R.id.buttonBuildings);
        ImageButton buildButton =findViewById(R.id.buttonBuild);

        buildButton.setOnClickListener(view -> buildButtonOnClick());
        mapButton.setOnClickListener(view -> mapButtonOnClick());
        buildingsButton.setOnClickListener(view->BuildingsButtonOnClick());
    }

    private void  BuildingsButtonOnClick(){
        MyFM.OpenFragment(new BuildingFragment(),"BuildingsFragment");
    }
    /**
     *Поведение при нажатии кнопки для вызова фрагмента строительства
     */
    private void buildButtonOnClick(){
        MyFM.OpenFragment(new BuildFragment(), "BuildFragment");
    }

    /**
     *Поведение при нажатии кнопки для вызова фрагмента карты
     */
    private void mapButtonOnClick(){MyFM.OpenFragment(new MapFragment(),"MapFragment");}




}