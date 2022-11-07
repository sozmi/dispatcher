package com.sozmi.dispatcher.model.objects;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.navigation.Route;
import com.sozmi.dispatcher.model.navigation.Routing;
import com.sozmi.dispatcher.model.system.Server;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс, описывающий машину как объект
 */
public class Car extends Object<TypeCar> {
    private StatusCar status;
    private Route route;
    private Route reverse_root;
    private int taskId = -1;
    private final ArrayList<CarListener> listeners = new ArrayList<>();

    /**
     * Конструктор
     *
     * @param name   пользовательское название машины
     * @param type   тип машины по её классификации
     * @param status статус машины
     * @param point  координаты машины
     */
    public Car(int id, String name, TypeCar type, StatusCar status, GeoPoint point) {
        super(id, name, point, type, type.toCost());
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

    public Route getReverse_root() {
        return reverse_root;
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

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void onPositionChanged() {
        // Notify everybody that may be interested.
        for (CarListener hl : listeners)
            hl.onPositionChanged(this);
    }

    public void onStatusChanged() {
        // Notify everybody that may be interested.
        for (CarListener hl : listeners)
            hl.onStatusChanged(this);
    }

    public void addListener(CarListener toAdd) {
        listeners.add(toAdd);
    }

    public void moving(GeoPoint finish) {
        Route route = Routing.Road(getPosition(), finish);
        setRoute(route);
        setReverse_root();
        Server.addInMovement(this);
        Map.getMap().addListenersPointsCarToDraw(this);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                GeoPoint point = route.getPoints().poll();
                if (point == null) {
                    setStatus(StatusCar.OnCall);
                    cancel();
                } else {
                    setPosition(point);
                    if(Map.isIsInit())
                        onPositionChanged();
                }
            }
        };

        Timer timer = new Timer("CarMovingTimer");
        long period = (long) (route.getTime()/ route.getCount_point()); //TODO умножить на 100

        if (period == 0) period = 1;
        timer.scheduleAtFixedRate(task, 0, period);
    }

    public void removeListener(CarListener toRemove) {
        listeners.remove(toRemove);
    }
}
