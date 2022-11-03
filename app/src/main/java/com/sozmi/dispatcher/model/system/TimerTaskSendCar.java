package com.sozmi.dispatcher.model.system;

import com.sozmi.dispatcher.model.navigation.Routing;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.Route;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TimerTask;

public class TimerTaskSendCar extends TimerTask {
    private final Queue<Car> cars;

    public TimerTaskSendCar(ArrayList<Car> cars) {
        this.cars = new LinkedList<>(cars);
    }

    @Override
    public void run() {
        Car car = cars.poll();
        if (car == null)
            cancel();
        else
            Routing.Movement(car);
    }
}
