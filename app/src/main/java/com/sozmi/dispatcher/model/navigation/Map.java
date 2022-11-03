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
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.Route;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.system.TimerTaskSendCar;
import com.sozmi.dispatcher.model.system.Server;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Timer;

public class Map {
    private static MapView map;
    private static final ArrayList<Marker> markers = new ArrayList<>();
    private static final ArrayList<Car> inMovement = new ArrayList<>();

    public static void init() {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Routing.init();
    }

    public static void init(View view) {
        map = view.findViewById(R.id.mapView);
        Routing.setMap(map);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        map.setMultiTouchControls(true);
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(4.0);
        moveCamTo(getUserLocation(view.getContext()));
        addBuildings(Server.getBuildings());
        addTasks(Server.getTasks());
        addMovement();
        map.invalidate();

    }

    private static void addMovement() {
        if (Map.inMovement.size() == 0)
            return;
        TimerTaskSendCar task = new TimerTaskSendCar(inMovement);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 50);

    }

    public static void addBuildings(ArrayList<Building> buildings) {
        for (Building building : buildings) {
            addMarkerBuilding(building);
        }
    }

    public static void addTasks(ArrayList<Task> tasks) {
        for (Task task : tasks) {
            addMarkerTask(task);
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

    public static Marker addMarkerBuilding(Building building) {

        Marker marker = new Marker(map);
        marker.setIcon(getDrawable(map.getContext(), building.getType().toImageId()));
        marker.setPosition(building.getPoint());
        marker.setTitle(building.getName());
        // marker.setOnMarkerClickListener((marker1, mapView) -> true);
        map.getOverlays().add(marker);
        map.invalidate();
        return marker;
    }

    public static Marker addMarkerTask(Task task) {
        Marker marker = new Marker(map);
        marker.setIcon(getDrawable(map.getContext(), task.getImage()));
        marker.setPosition(task.getPoint());
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

    public static void sendOnRoute(GeoPoint from, GeoPoint to, Car car) {
        Route route = Routing.Road(from, to);
        car.setRoute(route);
        car.setReverse_root();
        inMovement.add(car);
    }
}
