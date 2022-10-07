package com.sozmi.dispetcher.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sozmi.dispetcher.R;
import com.sozmi.dispetcher.model.MyMap;
import com.yandex.mapkit.geometry.Point;

public class MapFragment extends Fragment {

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yandex_map, container, false);
        MyMap.init(view.getContext());
        MyMap.setMapView(view);
        MyMap.setLogoPosition();
        MyMap.moveTo(new Point(55.751574, 37.573856));
        return view;
    }

    public void onStart() {
        super.onStart();
        MyMap.OnStart();
    }

    public void onStop() {
        super.onStop();
        MyMap.OnStop();
    }
}
