package com.sozmi.dispatcher.model.objects;

import android.content.Context;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.navigation.Route;
import com.sozmi.dispatcher.model.navigation.Routing;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс, описывающий машину как объект
 */
public class Car extends Object<TypeCar> {
    private StatusCar status;
    private Route main_route_points;
    private Route reverse_root;
    private Building building;
    private final ConcurrentHashMap<String, CarListener> listeners = new ConcurrentHashMap<>();

    /**
     * Конструктор
     *
     * @param name     пользовательское название машины
     * @param type     тип машины по её классификации
     * @param status   статус машины
     * @param point    координаты машины
     * @param building домашнее здание
     */
    public Car(int id, String name, TypeCar type, StatusCar status, GeoPoint point, Building building) {
        super(id, name, point, type, type.toCost());
        setBuilding(building);
        setStatus(status);
    }

    public Car(String data, Building building) {
        super();
        var d = data.split("\\|");
        if (d.length == 5) {
            int id = Integer.parseInt(d[0]);
            String[] position = d[1].split(",");
            GeoPoint point = new GeoPoint(Double.parseDouble(position[0].replace("(", "")), Double.parseDouble(position[1].replace(")", "")));
            String name = d[2];
            var type = TypeCar.valueOf(d[3]);

            initObject(id, name, point, type, type.toCost());
            setBuilding(building);
            setStatus(StatusCar.valueOf(d[4]));
        } else {
            try {
                Log.d("SOCKET", "Get message: " + data);
                throw new Exception("Передали не машину");

            } catch (Exception e) {
                Log.d("SOCKET", e.getMessage());
                Log.d("SOCKET", "Get message: " + data);
                e.printStackTrace();
            }
        }
    }


    private Queue<GeoPoint> reverse(Queue<GeoPoint> queue) {
        List<GeoPoint> collect = new ArrayList<>(queue);
        Collections.reverse(collect);
        return new LinkedList<>(collect);
    }


    public void setReverse_root() {
        this.reverse_root = new Route(main_route_points);
        this.reverse_root.setPoints(reverse(reverse_root.getPoints()));
    }

    /**
     * Получение статуса машины
     *
     * @return статус машины
     */
    public String getStatusToString() {
        return status.toString();
    }

    /**
     * Получение статуса машины
     *
     * @return статус машины
     */
    public StatusCar getStatus() {
        return status;
    }

    /**
     * Получение цвета статуса машины
     *
     * @return цвет статуса машины
     */
    public int getColor(Context context) {
        return ResourcesCompat.getColor(context.getResources(), status.toColorID(), null);
    }


    /**
     * Установка статуса машины
     *
     * @param status статус машины
     */
    public void setStatus(StatusCar status) {
        this.status = status;
        onStatusChanged();
    }

    public void onPositionChanged() {
        listeners.forEach((key, value) -> {
            var elem = listeners.get(key);
            if (elem != null) {
                elem.onPositionChanged(this);
            }
        });
    }

    public void onStatusChanged() {
        listeners.forEach((key, value) -> {
            var elem = listeners.get(key);
            if (elem != null) {
                elem.onStatusChanged(this);
            }
        });
    }

    public void addListener(CarListener toAdd, String TAG) {
        listeners.putIfAbsent(TAG, toAdd);
    }

    public void removeListener(String key) {
        listeners.entrySet().removeIf(entry -> entry.getKey().equals(key));
    }

    private Queue<GeoPoint> points_rout;

    public void moving(GeoPoint finish, StatusCar status) {
        if (finish == getBuilding().getPosition()) {
            main_route_points = reverse_root;
        } else {
            main_route_points = Routing.Road(getPosition(), finish);
            setReverse_root();
        }
        points_rout = new LinkedList<>(Routing.getAllPoints(main_route_points.pollPoint(), main_route_points.peekPoint()));
        if (Map.isInit())
            addListener(Map.getMap(), "MapClass");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (points_rout.isEmpty()) {
                    var start = main_route_points.pollPoint();
                    var finishf = main_route_points.peekPoint();
                    if (finishf == null) {
                        setStatus(status);
                        cancel();
                        return;
                    }
                    points_rout.addAll(Routing.getAllPoints(start, finishf));
                }
                setPosition(points_rout.poll());
                onPositionChanged();
            }
        };

        Timer timer = new Timer("CarMovingTimer");
        long period = (long) (main_route_points.getTime() / main_route_points.getCount_point());
        Log.d("Route", "peiod: " + period);
        if (period == 0) period = 1;
        timer.scheduleAtFixedRate(task, 0, period);
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
