package com.sozmi.dispatcher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.Map;
import com.sozmi.dispatcher.model.MyFM;
import com.sozmi.dispatcher.model.TypeBuilding;

import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {
    ImageButton addBuildingButton;
    FrameLayout panel_building;
    Boolean viewPanel=false, viewAllMenu=false;
    private Marker build;

    public MapFragment(){
    }
    public MapFragment(Boolean viewPanel,Boolean viewAllMenu){
        this.viewPanel=viewPanel;
        this.viewAllMenu=viewAllMenu;
    }
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Map.init(view);
        if(viewAllMenu){
            FrameLayout topMenu = requireActivity().findViewById(R.id.top_menu);
            LinearLayout bottomMenu = requireActivity().findViewById(R.id.bottom_menu);
            topMenu.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);
        }

        addBuildingButton = view.findViewById(R.id.buttonAddBuilding);
        Button cancelBuildingButton  = view.findViewById(R.id.buttonCancelBuildOnPanel);
        Button buildBuildingButton  = view.findViewById(R.id.buttonBuildOnPanel);
        panel_building  = view.findViewById(R.id.panel_build);

        addBuildingButton.setOnClickListener(v -> buttonAddBuildingOnClick());
        buildBuildingButton.setOnClickListener(getAcView1 -> buildButtonOnClick());
        cancelBuildingButton.setOnClickListener(v -> cancelBuildButtonOnClick());
        if(viewPanel) {
            buttonAddBuildingOnClick();
        }


        return view;
    }

    /**
     *Поведение при нажатии кнопки отмены при выборе координат на карте
     */
    private void cancelBuildButtonOnClick(){
        Map.removeMarker(build);
        addBuildingButton.setVisibility(View.VISIBLE);
        panel_building.setVisibility(View.INVISIBLE);
    }
    /**
     *Поведение при нажатии кнопки для вызова фрагмента строительства
     */
    private void buildButtonOnClick(){
        MyFM.OpenFragment(new BuildFragment(), "BuildFragment");
    }

    /**
     *Поведение при нажатии кнопки "Добавление нового здания" на карте
     */
    private void buttonAddBuildingOnClick(){
        build = Map.addMarker(Map.getCamPoint(), TypeBuilding.none);
        panel_building.setVisibility(View.VISIBLE);
        addBuildingButton.setVisibility(View.INVISIBLE);
    }
}
