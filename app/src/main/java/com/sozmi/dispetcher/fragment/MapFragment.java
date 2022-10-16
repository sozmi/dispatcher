package com.sozmi.dispetcher.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import com.sozmi.dispetcher.R;
import com.sozmi.dispetcher.model.Driving;
import com.sozmi.dispetcher.model.MyFM;
import com.sozmi.dispetcher.model.MyMap;
import com.sozmi.dispetcher.model.TypeBuilding;

public class MapFragment extends Fragment {
    ImageButton addBuildingButton;
    FrameLayout panel_building;
    Boolean viewPanel=false, viewAllMenu=false;
    public MapFragment(){
    }
    public MapFragment(Boolean viewPanel,Boolean viewAllMenu){
        this.viewPanel=viewPanel;
        this.viewAllMenu=viewAllMenu;
    }
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        MyMap.init(view);
        MyMap.SetUserLocation();
        MyMap.camMoveTo(MyMap.getCoordinateUser());
        Driving dr =new Driving(view.getContext());
        if(viewAllMenu){
            FrameLayout topMenu = getActivity().findViewById(R.id.top_menu);
            LinearLayout bottomMenu = getActivity().findViewById(R.id.bottom_menu);
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
    @Override
    public void onStart() {
        super.onStart();
        MyMap.OnStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        MyMap.OnStop();
    }
    /**
     *Поведение при нажатии кнопки отмены при выборе координат на карте
     */
    private void cancelBuildButtonOnClick(){
        MyMap.delMarker(MyMap.getPlMapObject());
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
        MyMap.addMarker(MyMap.getTargetCamera(), TypeBuilding.none);
        panel_building.setVisibility(View.VISIBLE);
        addBuildingButton.setVisibility(View.INVISIBLE);
    }
}
