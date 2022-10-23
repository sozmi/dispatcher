package com.sozmi.dispatcher.model.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sozmi.dispatcher.BuildConfig;
import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.Server;
import com.sozmi.dispatcher.model.objects.TypeBuilding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class Map {
    public static GeoPoint tempPoint;
    private static MapView map;
    private static List<Marker> markers;

    public static void init(View view) {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        map = view.findViewById(R.id.mapView);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        map.setMultiTouchControls(true);
        moveCamTo(getUserLocation(view.getContext()));
        addBuilding(Server.getBuildings());
        map.invalidate();
    }

    public static void addBuilding(ArrayList<Building> buildings) {
        for (Building building : buildings) {
            addMarker(building.getPoint(), building.getType());
        }
    }

    public static void moveCamTo(GeoPoint point) {
        IMapController mapController = map.getController();
        mapController.setZoom(15.0);
        mapController.setCenter(point);
    }

    public static GeoPoint getCamPoint() {
        return (GeoPoint) map.getMapCenter();
    }

    public static GeoPoint getMarkerPoint(Marker marker) {
        return marker.getPosition();
    }

    public static GeoPoint getUserLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);

        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return new GeoPoint(0.0, 0.0);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        return new GeoPoint(location);
    }

    public static Marker addMarker(GeoPoint point, TypeBuilding type) {

        Marker marker = new Marker(map);
        marker.setIcon(getDrawable(map.getContext(), type.toImageId()));
        marker.setPosition(point);
        marker.setOnMarkerClickListener((marker1, mapView) -> true);
        map.getOverlays().add(marker);

        map.invalidate();
        return marker;
    }
    public static Marker addMarkerIndicator(GeoPoint point) {

        Marker marker = new Marker(map);
        marker.setIcon(getDrawable(map.getContext(), R.drawable.ic_map_marker));
        marker.setPosition(point);
        marker.setOnMarkerClickListener((marker1, mapView) -> true);
        marker.setDraggable(true);
        map.getOverlays().add(marker);
        map.invalidate();
        return marker;
    }
    public static void removeMarker(Marker marker) {
        marker.remove(map);
        map.invalidate();
    }

    private static Drawable getDrawable(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }

}
