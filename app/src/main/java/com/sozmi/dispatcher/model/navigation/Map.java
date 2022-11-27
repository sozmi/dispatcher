package com.sozmi.dispatcher.model.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.sozmi.dispatcher.BuildConfig;
import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.main_view.fragments.TaskFragment;
import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.listeners.ServerListener;
import com.sozmi.dispatcher.model.listeners.TaskListener;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.StatusCar;
import com.sozmi.dispatcher.model.objects.StatusTask;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.server.ServerData;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.SystemTag;
import com.sozmi.dispatcher.model.system.Tag;

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
    private static GeoPoint userPosition=new GeoPoint(55.7522, 37.6156);

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
        for (Car car : ServerData.getInMovement()) {
            car.removeListener("MapClass");
        }
        for (Task task : ServerData.getTasks()) {
            task.removeListener("MapClass");
        }
        ServerData.removeListener("MapClass");
    }

    public void init(View view) {
        mapView = view.findViewById(R.id.mapView);
        isInit = true;
        Routing.setMap(mapView);
        getLastLocationNewMethod(view.getContext());
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapView.setMultiTouchControls(true);
        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(4.0);
        moveCamToUser();
        ServerData.addListener(this, toString());
        addBuildings(ServerData.getBuildings());
        addTasks(ServerData.getTasks());
        addCars(ServerData.getInMovement());
        mapView.invalidate();
    }

    public static void moveCamToUser() {
        moveCamTo(getUserLocation());
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

    public static void moveCamTo(GeoPoint point) {
        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        mapController.setCenter(point);
    }

    public static GeoPoint getCamPoint() {
        return (GeoPoint) mapView.getMapCenter();
    }

    public static GeoPoint getUserLocation() {
        return userPosition;
    }



    private void getLastLocationNewMethod(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "Нет разрешений");
            return;
        }
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    // GPS location can be null if GPS is switched off
                    if (location != null) {
                        userPosition = new GeoPoint(location);
                    }
                });
    }

    public void drawBuilding(Building building) {
        Marker marker = drawMarker(building.getName(), building.getImage(), building.getPosition(), false, false);
        building.setMarker(marker);
    }

    public void drawCar(Car car) {
        Marker marker = drawMarker(car.getName(), car.getImage(), car.getPosition(), false, true);
        car.setMarker(marker);
    }

    public void drawTask(Task task) {
        Marker marker = drawMarker(task.getName(), task.getImage(), task.getPosition(), false, true);
        marker.setOnMarkerClickListener((marker1, mapView1) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Tag.TaskID.toString(), task);
            MyFM.OpenFragment(new TaskFragment(), bundle);
            return true;
        });
        task.setMarker(marker);
        Log.i("Draw", "drawTask + setMarker");
    }

    public Marker drawMarkerIndicator(GeoPoint point) {
        return drawMarker("", R.drawable.ic_map_marker, point, true, true);
    }

    public void removeMarker(Marker marker) {
        marker.remove(mapView);
        mapView.invalidate();
    }

    private Drawable getDrawable(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }

    private Marker drawMarker(String title, int id, GeoPoint point, boolean isDrag, boolean noShowTitle) {
        Marker marker = new Marker(mapView);
        marker.setIcon(getDrawable(mapView.getContext(), id));
        marker.setPosition(point);
        marker.setTitle(title);
        if (noShowTitle)
            marker.setOnMarkerClickListener((marker1, mapView) -> true);
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
            } else {
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
            } else {
                if (task.getStatusTask() == StatusTask.executed) {
                    removeMarker(task.getMarker());
                    task.setMarker(null);
                    mapView.invalidate();
                } else {
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
        if (isInit()) {
            drawTask(task);
            task.addListener(this, toString());
            mapView.invalidate();
            Log.i(String.valueOf(SystemTag.map), "draw new task on map");
        }
    }
}
