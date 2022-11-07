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
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.system.DataController;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Permission;
import com.sozmi.dispatcher.model.system.Server;
import com.sozmi.dispatcher.model.system.Tag;

import org.osmdroid.util.GeoPoint;

public class MainActivity extends AppCompatActivity {
    private static GeoPoint point = null;
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
        DataController.setData(Server.getUser().getMoney() + " руб.");
        if (Server.isAuth()) {
            MyFM.OpenFragment(new MapFragment(), null);
            FrameLayout topMenu = findViewById(R.id.top_menu);
            LinearLayout bottomMenu = findViewById(R.id.bottom_menu);
            topMenu.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);
        } else {
            MyFM.OpenFragment(new LoginFragment(), null);
        }
//TODO: TEst
        Server.AddTestBuild();
        Map map =new Map();
        ImageButton mapButton = findViewById(R.id.buttonMap);
        ImageButton buildingsButton = findViewById(R.id.buttonBuildings);
        ImageButton buildButton = findViewById(R.id.buttonBuild);
        ImageButton taskButton = findViewById(R.id.buttonTasks);

        buildButton.setOnClickListener(view -> buildButtonOnClick());
        mapButton.setOnClickListener(view -> mapButtonOnClick());
        buildingsButton.setOnClickListener(view -> BuildingsButtonOnClick());
        taskButton.setOnClickListener(view -> taskButtonOnClick());
    }

    private void BuildingsButtonOnClick() {
        point = Map.getCamPoint();
        MyFM.OpenFragment(new BuildingsFragment(), null);
    }

    /**
     * Поведение при нажатии кнопки для вызова фрагмента строительства
     */
    private void buildButtonOnClick() {
        point = Map.getCamPoint();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tag.Point.toString(), Map.getUserLocation(this));
        MyFM.OpenFragment(new BuildFragment(), bundle);
    }

    /**
     * Поведение при нажатии кнопки для вызова фрагмента карты
     */
    private void mapButtonOnClick() {
        if (point != null) {
            point = Map.getCamPoint();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Tag.Point.toString(), point);
            MyFM.OpenFragment(new MapFragment(), bundle);
            return;
        }
        MyFM.OpenFragment(new MapFragment(), null);


    }

    /**
     * Поведение при нажатии кнопки для вызова фрагмента заданий
     */
    private void taskButtonOnClick() {
        point = Map.getCamPoint();
        MyFM.OpenFragment(new TasksFragment(), null);
    }
}