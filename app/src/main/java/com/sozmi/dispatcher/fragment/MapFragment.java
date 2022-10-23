package com.sozmi.dispatcher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.MyFM;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {
    ImageButton addBuildingButton;
    FrameLayout panel_building;
    Boolean viewPanel=false;
    private Marker build;

    public MapFragment(){
    }
    public MapFragment(Boolean viewPanel){
        this.viewPanel=viewPanel;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        addBuildingButton = view.findViewById(R.id.buttonAddBuilding);
        Button cancelBuildingButton  = view.findViewById(R.id.buttonCancelBuildOnPanel);
        Button buildBuildingButton  = view.findViewById(R.id.buttonBuildOnPanel);
        panel_building  = view.findViewById(R.id.panel_build);

        addBuildingButton.setOnClickListener(v -> buttonAddBuildingOnClick());
        buildBuildingButton.setOnClickListener(v -> buildButtonOnClick());
        cancelBuildingButton.setOnClickListener(v -> cancelBuildButtonOnClick());
        return view;
    }
    @Override
    public void onStart() {
        Map.init(requireView());

        super.onStart();

        if (viewPanel) {
            showMarker(Map.tempPoint);
            Map.moveCamTo(Map.tempPoint);
        }
    }

    /**
     *Поведение при нажатии кнопки отмены при выборе координат на карте
     */
    private void cancelBuildButtonOnClick(){
        Map.removeMarker(build);
        build =null;
        addBuildingButton.setVisibility(View.VISIBLE);
        panel_building.setVisibility(View.INVISIBLE);
    }
    /**
     *Поведение при нажатии кнопки для вызова фрагмента строительства
     */
    private void buildButtonOnClick(){
        MyFM.OpenFragment(new BuildFragment(Map.getMarkerPoint(build)), "BuildFragment");
    }

    /**
     *Поведение при нажатии кнопки "Добавление нового здания" на карте
     */
    private void buttonAddBuildingOnClick(){
        showMarker(Map.getCamPoint());
    }

    private void showMarker(GeoPoint point){
        build = Map.addMarkerIndicator(point);
        panel_building.setVisibility(View.VISIBLE);
        addBuildingButton.setVisibility(View.INVISIBLE);
    }
}
