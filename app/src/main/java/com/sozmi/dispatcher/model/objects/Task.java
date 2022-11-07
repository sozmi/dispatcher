package com.sozmi.dispatcher.model.objects;

import androidx.annotation.NonNull;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.listeners.TaskListener;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.system.Server;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Task extends Object<TypeTask> implements CarListener {
    private TypeGroupTask typeGroup;
    private StatusTask statusTask;
    private final ArrayList<Car> cars = new ArrayList<>();
    private final ArrayList<Requirement> requirements;
    private final ArrayList<TaskListener> listeners =new ArrayList<>();
    int time = 60000;

    public Task(int id, GeoPoint point, String name, TypeGroupTask typeGroup, int baseCost, TypeTask typeTask, StatusTask statusTask, ArrayList<Requirement> requirements) {
        super(id, name, point, typeTask, typeGroup.toModifier() * baseCost);
        this.requirements = requirements;
        setStatusTask(statusTask);
        setTypeGroup(typeGroup);
    }

    @Override
    public int getImage() {
        switch (getType()) {
            case fire:
                switch (statusTask) {
                    case execute:
                        return R.drawable.ic_epidemic_green;
                    case wait:
                        return R.drawable.ic_epidemic_yellow;
                    case not_executed:
                        return R.drawable.ic_epidemic_red;
                }
            case robbery:
                switch (statusTask) {
                    case execute:
                        return R.drawable.ic_epidemic_green;
                    case wait:
                        return R.drawable.ic_epidemic_yellow;
                    case not_executed:
                        return R.drawable.ic_epidemic_red;
                }
            case epidemic:
                switch (statusTask) {
                    case execute:
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
        onChangeRequirement(getRequirements());
        for (CarCheck check : cars_check) {
            var car = check.getCar();
            car.setStatus(StatusCar.MovingOnCall);
            car.setTaskId(getID());
            car.moving(getPosition());
        }

        if (cars.size() != 0) {
            setStatusTask(StatusTask.wait);
            Map.getMap().changeMarkerImage(getMarker(), getImage());
        }
    }

    public void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Server.removeTask(getID());
            }
        }, time);
    }

    public void addListener(TaskListener toAdd) {
        listeners.add(toAdd);
    }

    public void onChangeRequirement(String res) {
        // Notify everybody that may be interested.
        for (TaskListener hl : listeners)
            hl.onChangedRequirement(res);
    }

    @Override
    public void onStatusChanged(Car car) {
        String res = getRequirements();

        if (res.length() == 0){statusTask = StatusTask.execute;
            startTimer();
            onChangeRequirement(null);
        }
        else {
            onChangeRequirement(res);
        }

    }

    @NonNull
    public String getRequirements() {
        StringBuilder res = new StringBuilder();
        for (Requirement req : requirements) {
            if (req.isNoDone(cars))
                res.append(req);
        }
        return res.toString();
    }

    @Override
    public void onPositionChanged(Car car) {

    }
}
