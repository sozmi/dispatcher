package com.sozmi.dispatcher.model.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sozmi.dispatcher.BuildConfig;
import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.listeners.ServerListener;
import com.sozmi.dispatcher.model.listeners.TaskListener;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.StatusCar;
import com.sozmi.dispatcher.model.objects.StatusTask;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.system.Server;
import com.sozmi.dispatcher.model.system.SystemTag;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class Map implements CarListener, TaskListener, ServerListener {
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

    public static boolean isInit() {
        return isInit;
    }

    public static void onDestroy() {
        isInit = false;
        for (Car car : Server.getInMovement()) {
            car.removeListener("MapClass");
        }
        for (Task task : Server.getTasks()) {
            task.removeListener("MapClass");
        }
        Server.removeListener("MapClass");
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
        Server.addListener(this,toString());
        addBuildings(Server.getBuildings());
        addTasks(Server.getTasks());
        addCars(Server.getInMovement());
        mapView.invalidate();
    }

    public void addBuildings(ArrayList<Building> buildings) {
        for (Building building : buildings) {
            drawBuilding(building);
        }
    }

    public void addTasks(ArrayList<Task> tasks) {
        for (Task task : tasks) {
            drawTask(task);
            task.addListener(this, toString());
        }
    }

    public void addCars(ArrayList<Car> cars) {
        for (Car car : cars) {
            car.addListener(this, toString());
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
            return new GeoPoint(0.0, 0.0);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        return new GeoPoint(location);
    }

    public void drawBuilding(Building building) {
        Marker marker = drawMarker(building.getName(), building.getImage(), building.getPosition(), false, true);
        building.setMarker(marker);
    }

    public void drawCar(Car car){
        Marker marker = drawMarker(car.getName(), car.getImage(),car.getPosition(),false,false);
        car.setMarker(marker);
    }
    public void drawTask(Task task) {
        Marker marker = drawMarker(task.getName(), task.getImage(), task.getPosition(), false, false);
        task.setMarker(marker);
        Log.i("Draw", "drawTask + setMarker");
    }

    public Marker drawMarkerIndicator(GeoPoint point) {
        return drawMarker("", R.drawable.ic_map_marker, point, true, false);
    }

    public void removeMarker(Marker marker) {
        marker.remove(mapView);
        mapView.invalidate();
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
        mapView.postInvalidate();
        return marker;
    }

    @Override
    public void onStatusChanged(Car car) {
        if (car.getStatus() == StatusCar.OnCall || car.getStatus() == StatusCar.Available) {
            if (car.getMarker() != null) {
                removeMarker(car.getMarker());
                car.setMarker(null);
            }
            car.removeListener(toString());
        }
    }

    @Override
    public void onPositionChanged(Car car) {
        if (isInit) {
            if (car.getMarker() == null) {
                drawCar(car);
            }
            else {
                car.getMarker().setPosition(car.getPosition());
                mapView.postInvalidate();
            }
        } else {
            if (car.getMarker() != null) {
                removeMarker(car.getMarker());
                car.setMarker(null);
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "MapClass";
    }

    @Override
    public void onChangedRequirement(String res) {

    }

    @Override
    public void onChangeStatusTask(Task task) {
        if (isInit) {
            if (task.getMarker() == null) {
                drawTask(task);
            }
            else {
                if(task.getStatusTask()==StatusTask.executed){
                    removeMarker(task.getMarker());
                    task.setMarker(null);
                    mapView.invalidate();
                }
                else {
                    task.getMarker().setIcon(getDrawable(mapView.getContext(), task.getImage()));
                    mapView.invalidate();
                }
            }
        } else {
            if (task.getMarker() != null) {
                removeMarker(task.getMarker());
                task.setMarker(null);
            }
        }
    }

    @Override
    public void onChangeTimerTask(Task task) {

    }

    @Override
    public void addTask(Task task) {
        if(isInit()){
            drawTask(task);
            task.addListener(this, toString());
            mapView.invalidate();
            Log.i(String.valueOf(SystemTag.map),"draw new task on map");
        }
    }
}
