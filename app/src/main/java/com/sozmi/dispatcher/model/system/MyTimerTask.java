package com.sozmi.dispatcher.model.system;

import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.Route;
import com.sozmi.dispatcher.model.objects.StatusCar;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Queue;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    private final Queue<GeoPoint> points;
    private final MapView map;
    private final Marker marker;
    private final Car car;

    public MyTimerTask(Car car, MapView map, Marker marker) {
        this.points = car.getRoute().getPoints();
        this.map = map;
        this.car =car;
        this.marker = marker;
    }

    @Override
    public void run() {
        if (map == null)
            cancel();
        GeoPoint point = points.poll();
        if (point == null){
            car.setStatus(StatusCar.OnCall);
            car.setRoute(car.getReverse_root());
            Map.removeMarker(marker);
            cancel();
        }else {
            marker.setPosition(point);
            assert map != null;
            map.postInvalidate();
        }


    }
}
