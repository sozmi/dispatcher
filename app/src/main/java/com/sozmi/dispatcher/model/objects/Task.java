package com.sozmi.dispatcher.model.objects;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.system.Server;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Task extends Object<TypeTask> {
    private TypeGroupTask typeGroup;
    private StatusTask statusTask;
    private ArrayList<Car> cars = new ArrayList<>();
    Timer timer;
    int time = 60;

    public Task(int id, GeoPoint point, String name, TypeGroupTask typeGroup, int baseCost, TypeTask image, StatusTask statusTask) {
        super(id, name, point, image, typeGroup.toModifier()*baseCost);
        setStatusTask(statusTask);
        setTypeGroup(typeGroup);
    }

    @Override
    public int getImage() {
        switch (getType()) {
            case fire:
                switch (statusTask) {
                    case executed:
                        return R.drawable.ic_epidemic_green;
                    case wait:
                        return R.drawable.ic_epidemic_yellow;
                    case not_executed:
                        return R.drawable.ic_epidemic_red;
                }
            case robbery:
                switch (statusTask) {
                    case executed:
                        return R.drawable.ic_epidemic_green;
                    case wait:
                        return R.drawable.ic_epidemic_yellow;
                    case not_executed:
                        return R.drawable.ic_epidemic_red;
                }
            case epidemic:
                switch (statusTask) {
                    case executed:
                        return R.drawable.ic_epidemic_green;
                    case wait:
                        return R.drawable.ic_epidemic_yellow;
                    case not_executed:
                        return R.drawable.ic_epidemic_red;
                }
            default:
                return -1;
        }
    }

    public TypeGroupTask getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(TypeGroupTask typeGroup) {
        this.typeGroup = typeGroup;
    }


    public int getImageGroupTask() {
        return typeGroup.toImageId();
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public int getBackgroundStatusTask() {
        return statusTask.toColor();
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void sendCars(ArrayList<CarCheck> cars_check) {
        ArrayList<Car> cars = new ArrayList<>();

        for (CarCheck car : cars_check) {
            car.getCar().setStatus(StatusCar.MovingOnCall);
            cars.add(car.getCar());
        }
        for (Car car : cars) {
            Map.sendOnRoute(car.getPoint(), getPoint(),car);
        }

        this.cars = cars;

    }

    public void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Server.removeTask(getID());
            }
        }, time);
    }

    public void pause() {
        timer.cancel();
    }
}
