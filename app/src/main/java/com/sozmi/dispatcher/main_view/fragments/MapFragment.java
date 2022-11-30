package com.sozmi.dispatcher.main_view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Tag;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {
    ImageButton addBuildingButton, move_btn;
    FrameLayout panel_building;
    private Marker build;
    private Map map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        map = Map.getMap();
        addBuildingButton = view.findViewById(R.id.buttonAddBuilding);
        Button cancelBuildingButton = view.findViewById(R.id.buttonCancelBuildOnPanel);
        Button buildBuildingButton = view.findViewById(R.id.buttonBuildOnPanel);
        panel_building = view.findViewById(R.id.panel_build);
        move_btn = view.findViewById(R.id.buttonMoveToPosition);
        move_btn.setOnClickListener(v -> Map.moveCamToUser());
        addBuildingButton.setOnClickListener(v -> buttonAddBuildingOnClick());
        buildBuildingButton.setOnClickListener(v -> buildButtonOnClick());
        cancelBuildingButton.setOnClickListener(v -> cancelBuildButtonOnClick());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        map.init(requireView());
        Bundle bundle = getArguments();
        if (bundle != null) {
            GeoPoint point = bundle.getParcelable(Tag.Point.toString());
            boolean viewPanel = bundle.getBoolean(Tag.viewPanel.toString());
            if (viewPanel) {
                showMarker(point);
            }
            map.moveCamTo(point);
        }else {
            Map.moveCamToUser();
        }
    }

    /**
     * Поведение при нажатии кнопки отмены при выборе координат на карте
     */
    private void cancelBuildButtonOnClick() {
        map.removeMarker(build);
        build = null;
        addBuildingButton.setVisibility(View.VISIBLE);
        move_btn.setVisibility(View.VISIBLE);
        panel_building.setVisibility(View.INVISIBLE);
    }

    /**
     * Поведение при нажатии кнопки для вызова фрагмента строительства
     */
    private void buildButtonOnClick() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tag.Point.toString(), build.getPosition());
        MyFM.OpenFragment(new BuildFragment(), bundle);
    }

    /**
     * Поведение при нажатии кнопки "Добавление нового здания" на карте
     */
    private void buttonAddBuildingOnClick() {
        showMarker(Map.getCamPoint());
    }

    private void showMarker(GeoPoint point) {
        build = map.drawMarkerIndicator(point);
        panel_building.setVisibility(View.VISIBLE);
        addBuildingButton.setVisibility(View.INVISIBLE);
        move_btn.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Map.onDestroy();
    }
}
