package com.sozmi.dispatcher.model.objects;

import android.content.Context;

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
    private Route route;
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

    public void setRoute(Route route) {
        this.route = route;
    }

    private Queue<GeoPoint> reverse(Queue<GeoPoint> queue) {
        List<GeoPoint> collect = new ArrayList<>(queue);
        Collections.reverse(collect);
        return new LinkedList<>(collect);
    }

    public Route getRoute() {
        return route;
    }


    public void setReverse_root() {
        this.reverse_root = new Route(route);
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

    public void addListener(CarListener toAdd,String TAG) {
        listeners.putIfAbsent(TAG,toAdd);
    }

    public void removeListener(String key) {
        listeners.entrySet().removeIf(entry -> entry.getKey().equals(key));
    }

    public void moving(GeoPoint finish, StatusCar status) {
        if (finish == getBuilding().getPosition()) {
            setRoute(reverse_root);
        } else {
            Route route = Routing.Road(getPosition(), finish);
            setRoute(route);
            setReverse_root();
        }
        if (Map.isInit()) addListener(Map.getMap(),"MapClass");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                GeoPoint point = getRoute().getPoints().poll();
                if (point == null) {
                    setStatus(status);
                    cancel();
                } else {
                    setPosition(point);
                    onPositionChanged();
                }
            }
        };

        Timer timer = new Timer("CarMovingTimer");
        long period = (long) (route.getTime()*100 / route.getCount_point());

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
