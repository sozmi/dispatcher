package com.sozmi.dispatcher.model.navigation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.sozmi.dispatcher.BuildConfig;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.Route;
import com.sozmi.dispatcher.model.system.MyTimerTask;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;

public class Routing {
    private static GraphHopperRoadManager roadManager;
    private static MapView map;

    public static void init() {
        roadManager = new GraphHopperRoadManager(BuildConfig.API_KEY, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //TODO: убрать запуск в главном потоке
        StrictMode.setThreadPolicy(policy);
    }

    public static void setMap(MapView map) {
        Routing.map = map;
    }

    public static Route Road(GeoPoint start, GeoPoint end) {
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(start);
        waypoints.add(end);
        Road road = roadManager.getRoad(waypoints);

        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);
        map.invalidate();
        double time = road.mDuration;
        Queue<GeoPoint> points = getAllPoints(roadOverlay.getActualPoints());
        return new Route(points, time);
    }


    public static void Movement(Car car) {
        if (map == null) return;
        Drawable nodeIcon = getDrawable(map.getContext(),car.getType().toImageId());
        Marker nodeMarker = new Marker(map);
        nodeMarker.setIcon(nodeIcon);
        nodeMarker.setPosition(car.getRoute().getPoints().poll());
        map.getOverlays().add(nodeMarker);
        MyTimerTask task = new MyTimerTask(car, map, nodeMarker);
        Timer timer = new Timer();
        long period = (long)(car.getRoute().getTime()*100/car.getRoute().getCount_point());
        Log.i("INT:","long ="+period);
        if(period==0)period=1;
        timer.scheduleAtFixedRate(task, 0, period);
    }

    private static Drawable getDrawable(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }

    private static Queue<GeoPoint> getAllPoints(List<GeoPoint> points) {
        Queue<GeoPoint> finish_points = new LinkedList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            var start_point = points.get(i);
            var finish_point = points.get(i + 1);
            var count_point = HaversineAlgorithm.HaversineInM(start_point.getLatitude(), start_point.getLongitude(), finish_point.getLatitude(), finish_point.getLongitude()) - 1;
            var all = SplitLine(start_point, finish_point, count_point);
            finish_points.addAll(all);
        }
        return finish_points;
    }

    public static ArrayList<GeoPoint> SplitLine(GeoPoint a, GeoPoint b, int count) {

        count = count + 1;

        final double alat = a.getLatitude();
        final double blat = b.getLatitude();
        final double along = a.getLongitude();
        final double blong = b.getLongitude();

        double d = Math.sqrt((alat - blat) * (alat - blat) + (along - blong) * (along - blong)) / count;
        double fi = Math.atan2(blong - along, blat - alat);

        ArrayList<GeoPoint> points = new ArrayList<>(count + 1);

        for (int i = 0; i <= count; ++i)
            points.add(new GeoPoint(alat + i * d * Math.cos(fi), along + i * d * Math.sin(fi)));

        return points;
    }


    private static class HaversineAlgorithm {

        static final double _eQuatorialEarthRadius = 6378.1370D;
        static final double _d2r = (Math.PI / 180D);

        public static int HaversineInM(double lat1, double long1, double lat2, double long2) {
            return (int) (1000D * HaversineInKM(lat1, long1, lat2, long2));
        }

        public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
            double dlong = (long2 - long1) * _d2r;
            double dlat = (lat2 - lat1) * _d2r;
            double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                    * Math.pow(Math.sin(dlong / 2D), 2D);
            double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));

            return _eQuatorialEarthRadius * c;
        }

    }
}
