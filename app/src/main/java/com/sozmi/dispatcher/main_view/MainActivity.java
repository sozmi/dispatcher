package com.sozmi.dispatcher.main_view;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sozmi.dispatcher.LoadFragment;
import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.main_view.fragments.BuildFragment;
import com.sozmi.dispatcher.main_view.fragments.ListBuildingsFragment;
import com.sozmi.dispatcher.main_view.fragments.ListTasksFragment;
import com.sozmi.dispatcher.main_view.fragments.MapFragment;
import com.sozmi.dispatcher.model.listeners.DataListner;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Permission;
import com.sozmi.dispatcher.model.system.Tag;

import org.osmdroid.util.GeoPoint;

public class MainActivity extends AppCompatActivity implements DataListner<Integer> {
    private static GeoPoint point = null;

    @Override
    public void onBackPressed() {
        if (MyFM.getCurrentName().equals(MapFragment.class.getName()))
            super.onBackPressed();
        else
            mapButtonOnClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = new LoadFragment();
        ft.add(f, f.toString());
        ft.commit();
        var th = new Thread(() -> {
            try {
                ServerData.loader();
                runOnUiThread(this::init);


            } catch (NetworkException e) {
                runOnUiThread(() -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
        th.start();

        ServerData.getUser().addListener(this, toString());
        ServerData.getUser().addMoney(0);

    }


    private void init() {
        Permission.get(this);
        new Map(this);
        MyFM.setFM(getSupportFragmentManager());
        MyFM.OpenFragment(new MapFragment(), null);
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
        MyFM.OpenFragment(new ListBuildingsFragment(), null);
    }

    /**
     * Поведение при нажатии кнопки для вызова фрагмента строительства
     */
    private void buildButtonOnClick() {
        point = Map.getCamPoint();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tag.point.toString(), Map.getUserLocation());
        MyFM.OpenFragment(new BuildFragment(), bundle);
    }

    /**
     * Поведение при нажатии кнопки для вызова фрагмента карты
     */
    private void mapButtonOnClick() {
        if (point != null) {
            point = Map.getCamPoint();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Tag.point.toString(), point);
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
        MyFM.OpenFragment(new ListTasksFragment(), null);
    }

    private boolean isNoInit = true;

    @Override
    public void onChangeData(Integer money) {
        //public void run() {
        runOnUiThread(() -> {
            TextView moneyText = findViewById(R.id.money);
            moneyText.setText(String.valueOf(money));
            if (isNoInit) {
                TextView name = findViewById(R.id.nameUser);
                name.setText(ServerData.getUser().getName());
                isNoInit = false;
            }
        });

    }


    @Override
    protected void onPause() {
        try {
            ServerData.unloader();
        } catch (NetworkException e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
        super.onPause();
    }
}