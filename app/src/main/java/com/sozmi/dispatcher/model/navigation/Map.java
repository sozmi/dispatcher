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
import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.StatusCar;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.system.Server;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class Map implements CarListener {
    private static MapView mapView;
    private static boolean isInit = false;
    private static Map map;

    public static Map getMap() {
        return map;
    }

    public Map() {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Routing.init();
        map = this;
    }

    public static boolean isIsInit() {
        return isInit;
    }

    public static void setIsInit(boolean isInit) {
        Map.isInit = isInit;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void init(View view) {
        mapView = view.findViewById(R.id.mapView);
        isInit = true;
        Routing.setMap(mapView);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapView.setMultiTouchControls(true);
        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(4.0);
        moveCamTo(getUserLocation(view.getContext()));
        addBuildings(Server.getBuildings());
        addTasks(Server.getTasks());
        mapView.invalidate();
    }

    public void addListenersPointsCarToDraw(Car car) {
        car.addListener(this);
    }

    public void addBuildings(ArrayList<Building> buildings) {
        for (Building building : buildings) {
            drawBuilding(building);
        }
    }

    public void addTasks(ArrayList<Task> tasks) {
        for (Task task : tasks) {
            drawTask(task);
        }
    }

    public void moveCamTo(GeoPoint point) {
        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        mapController.setCenter(point);
    }

    public static GeoPoint getCamPoint() {
        return (GeoPoint) mapView.getMapCenter();
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
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public void drawBuilding(Building building) {
        Marker marker = drawMarker(building.getName(), building.getImage(), building.getPosition(), false, true);
        building.setMarker(marker);
    }

    public void drawTask(Task task) {
        Marker marker = drawMarker("", task.getImage(), task.getPosition(), false, false);
        task.setMarker(marker);
    }

    public Marker drawMarkerIndicator(GeoPoint point) {
        return drawMarker("", R.drawable.ic_map_marker, point, true, false);
    }

    public void removeMarker(Marker marker) {
        marker.remove(mapView);
        mapView.invalidate();
    }

    public void changeMarkerImage(Marker marker, int idRes) {
        marker.setIcon(getDrawable(mapView.getContext(), idRes));
        mapView.postInvalidate();
    }

    private Drawable getDrawable(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }

    private Marker drawMarker(String title, int id, GeoPoint point, boolean isDrag, boolean isShowTitle) {
        Marker marker = new Marker(mapView);
        marker.setIcon(getDrawable(mapView.getContext(), id));
        marker.setPosition(point);
        marker.setTitle(title);
        if (isShowTitle) marker.setOnMarkerClickListener((marker1, mapView) -> true);
        marker.setDraggable(isDrag);
        mapView.getOverlays().add(marker);
        mapView.invalidate();
        return marker;
    }

    @Override
    public void onStatusChanged(Car car) {
        if (car.getStatus() == StatusCar.OnCall && car.getMarker() != null) {
            removeMarker(car.getMarker());
            car.removeListener(this);
        }
    }

    @Override
    public void onPositionChanged(Car car) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                if (isInit) {
                    if (car.getMarker() == null) {
                        Marker marker = new Marker(mapView);
                        marker.setIcon(getDrawable(mapView.getContext(), car.getImage()));
                        marker.setPosition(car.getPosition());
                        marker.setOnMarkerClickListener((marker1, mapView) -> true);
                        if(car.getMarker()==null)
                            mapView.getOverlays().add(marker);
                        car.setMarker(marker);
                    }
                    car.getMarker().setPosition(car.getPosition());
                    mapView.postInvalidate();
                } else {
                    if (car.getMarker() != null) {
                        car.getMarker().remove(mapView);
                        mapView.postInvalidate();
                        car.setMarker(null);
                    }
                }
            }
        };
        thread.start();

    }
}
