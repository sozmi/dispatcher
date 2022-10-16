package com.sozmi.dispetcher.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;


import com.sozmi.dispetcher.BuildConfig;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class Routing  {
    private static  GraphHopperRoadManager roadManager;
    private static MapView map;

    public static void init(MapView map){
        roadManager= new GraphHopperRoadManager(BuildConfig.API_KEY, false);
        Routing.map =map;
    }

    public static void Road(GeoPoint start, GeoPoint end){
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(start);
        waypoints.add(end);
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);
        map.invalidate();
    }

    public static void Movement(Polyline roadOverlay){
        Drawable nodeIcon = getDrawable(map.getContext(), TypeCar.ambulance.toImageId());
        Marker nodeMarker = new Marker(map);
        nodeMarker.setIcon(nodeIcon);
        nodeMarker.setPosition(roadOverlay.getActualPoints().get(0));
        Runnable task = () -> {
            map.getOverlays().add(nodeMarker);
            for (GeoPoint point : roadOverlay.getActualPoints()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nodeMarker.setPosition(point);
                map.invalidate();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
    private static Drawable getDrawable(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }
}
