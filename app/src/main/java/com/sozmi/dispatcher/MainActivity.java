package com.sozmi.dispatcher;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.sozmi.dispatcher.fragment.BuildFragment;
import com.sozmi.dispatcher.fragment.BuildingsFragment;
import com.sozmi.dispatcher.fragment.LoginFragment;
import com.sozmi.dispatcher.fragment.MapFragment;
import com.sozmi.dispatcher.fragment.TasksFragment;
import com.sozmi.dispatcher.model.DataController;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.MyFM;
import com.sozmi.dispatcher.model.Permission;
import com.sozmi.dispatcher.model.Server;

public class MainActivity extends AppCompatActivity {
    final LiveData<String> liveData = DataController.getData();

    @Override
    public void onBackPressed() {
        if (MyFM.getCurrentName().equals("MapFragment") || MyFM.getCurrentName().equals("LoginFragment"))
            super.onBackPressed();
        else
            mapButtonOnClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permission.get(this);

        MyFM.setFM(getSupportFragmentManager());
        TextView money = findViewById(R.id.money);
        liveData.observe(this, money::setText);
        DataController.setData(Server.getMoney() + " руб.");
        if (Server.isAuth()) {
            MyFM.OpenFragment(new MapFragment());
            FrameLayout topMenu = findViewById(R.id.top_menu);
            LinearLayout bottomMenu = findViewById(R.id.bottom_menu);
            topMenu.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);
        } else {
            MyFM.OpenFragment(new LoginFragment());
        }

        ImageButton mapButton = findViewById(R.id.buttonMap);
        ImageButton buildingsButton = findViewById(R.id.buttonBuildings);
        ImageButton buildButton = findViewById(R.id.buttonBuild);
        ImageButton taskButton =findViewById(R.id.buttonTasks);

        buildButton.setOnClickListener(view -> buildButtonOnClick());
        mapButton.setOnClickListener(view -> mapButtonOnClick());
        buildingsButton.setOnClickListener(view -> BuildingsButtonOnClick());
        taskButton.setOnClickListener(view -> taskButtonOnClick());
    }

    private void BuildingsButtonOnClick() {
        MyFM.OpenFragment(new BuildingsFragment());
    }

    /**
     * Поведение при нажатии кнопки для вызова фрагмента строительства
     */
    private void buildButtonOnClick() {
        MyFM.OpenFragment(new BuildFragment(Map.getUserLocation(this)));
    }

    /**
     * Поведение при нажатии кнопки для вызова фрагмента карты
     */
    private void mapButtonOnClick() {
        MyFM.OpenFragment(new MapFragment());
    }

    /**
     * Поведение при нажатии кнопки для вызова фрагмента заданий
     */
    private void taskButtonOnClick() {
        MyFM.OpenFragment(new TasksFragment());
    }


}