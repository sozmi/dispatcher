package com.sozmi.dispetcher;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.yandex.mapkit.geometry.Point;

public class MapFragment extends Fragment {

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view =(View) inflater.inflate(R.layout.yandex_map, container, false);
        Map.init(view.getContext());
        Map.setMapView(view);
        Map.setLogoPosition();
        Map.moveTo(new Point(55.751574, 37.573856));
        return view;
    }

    public void onStart() {
        super.onStart();
        Map.OnStart();
    }

    public void onStop() {
        super.onStop();
        Map.OnStop();
    }
}
