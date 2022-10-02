package com.sozmi.dispetcher;

import static com.yandex.runtime.Runtime.getApplicationContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MapFragment extends Fragment {

    MapView mapView;
    View view;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.yandex_map, container, false);
        mapView = view.findViewById(R.id.mapview);
        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        addMarker();
        return view;
    }

public  void addMarker(){
    mapView.getMap().getMapObjects().addPlacemark(new Point(55.751574, 37.573856),
            ImageProvider.fromResource(getApplicationContext(), R.drawable.building));
    Toast toast = Toast.makeText(getApplicationContext(),
            "Пора покормить кота!", Toast.LENGTH_SHORT);
    toast.show();
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
