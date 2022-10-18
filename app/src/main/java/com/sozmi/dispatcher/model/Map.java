package com.sozmi.dispatcher.model;

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

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class Map {

    private static MapView map;
    private static GeoPoint userPoint;

    public static void  init(View view) {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        map = view.findViewById(R.id.mapView);

        map.setMultiTouchControls(true);
        setUserLocation(view.getContext());
        moveCamTo(getUserPoint());
        map.invalidate();
    }

    public static void moveCamTo(GeoPoint point) {
        IMapController mapController = map.getController();
        mapController.setZoom(15.0);
        mapController.setCenter(point);
    }

    public static GeoPoint getCamPoint() {
        return (GeoPoint) map.getMapCenter();
    }

    public static GeoPoint getUserPoint() {
        return userPoint;
    }
    public static GeoPoint getMarkerPoint(Marker marker) {
        return marker.getPosition();
    }
    public static void setUserPoint(GeoPoint point) {
        Map.userPoint = point;
    }

    public static void setUserLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            criteria.setSpeedRequired(true);
        }

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
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            setUserPoint(new GeoPoint(location));
        } else setUserPoint(new GeoPoint(0.0, 0.0));


    }

    public static Marker addMarker(GeoPoint point, TypeBuilding type) {

        Marker marker = new Marker(map);
        marker.setIcon(getDrawable(map.getContext(), type.toImageId()));
        marker.setPosition(point);
        if (type == TypeBuilding.none)
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
