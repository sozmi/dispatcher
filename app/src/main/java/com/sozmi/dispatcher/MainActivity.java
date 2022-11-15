package com.sozmi.dispatcher;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sozmi.dispatcher.fragment.BuildFragment;
import com.sozmi.dispatcher.fragment.BuildingsFragment;
import com.sozmi.dispatcher.fragment.LoginFragment;
import com.sozmi.dispatcher.fragment.MapFragment;
import com.sozmi.dispatcher.fragment.TasksFragment;
import com.sozmi.dispatcher.model.listeners.DataListner;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.server.ServerData;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Permission;
import com.sozmi.dispatcher.model.system.Tag;

import org.osmdroid.util.GeoPoint;

public class MainActivity extends AppCompatActivity implements DataListner<Integer> {
    private static GeoPoint point = null;


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

        ServerData.getUser().addListener(this, toString());
        ServerData.loadData();
        if (ServerData.isAuth()) {
            MyFM.OpenFragment(new MapFragment(), null);
            FrameLayout topMenu = findViewById(R.id.top_menu);
            LinearLayout bottomMenu = findViewById(R.id.bottom_menu);
            topMenu.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);
        } else {
            MyFM.OpenFragment(new LoginFragment(), null);
        }
        new Map();

        //money.setText(ServerData.getUser().getMoney());
        actionButton();
    }

    private void actionButton() {
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

    private boolean isNoInit = true;

    @Override
    public void onChangeData(Integer money) {
        TextView moneyText = findViewById(R.id.money);
        moneyText.setText(String.valueOf(money));
        if (isNoInit) {
            TextView name = findViewById(R.id.nameUser);
            name.setText(ServerData.getUser().getName());
            isNoInit = false;
        }

    }
}