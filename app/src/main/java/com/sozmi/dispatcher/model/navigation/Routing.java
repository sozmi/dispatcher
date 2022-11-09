package com.sozmi.dispatcher.model.navigation;

import android.os.StrictMode;

import com.sozmi.dispatcher.BuildConfig;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    private static Queue<GeoPoint> getAllPoints(List<GeoPoint> points) {
        Queue<GeoPoint> finish_points = new LinkedList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            var start_point = points.get(i);
            var finish_point = points.get(i + 1);
            var count_point = (HaversineAlgorithm.HaversineInM(start_point, finish_point) - 1)/2;
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



}
