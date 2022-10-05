package com.sozmi.dispetcher;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.mapview.MapView;

public class MapFragment extends Fragment {
    MapView mapView;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view =(View) inflater.inflate(R.layout.yandex_map, container, false);
        Map.init(view.getContext());
        Map.setMapView(view);
        mapView=Map.getMapView();
        Map.moveTo(new Point(55.751574, 37.573856));
        Map.addMarker(new Point(55.751574, 37.573856),TypeBuilding.fire_station);
        Map.addMarker(new Point(55.651574, 37.673856),TypeBuilding.police);
        return view;
    }

    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    public void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }


}
