package com.sozmi.dispatcher.model.objects;

import android.util.Log;

import androidx.annotation.NonNull;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.listeners.TaskListener;
import com.sozmi.dispatcher.model.system.SystemTag;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class Task extends Object<TypeTask> implements CarListener, Serializable {
    private TypeGroupTask typeGroup;
    private StatusTask statusTask;
    private final ArrayList<Car> cars = new ArrayList<>();
    private final ArrayList<Requirement> requirements;
    private final ConcurrentHashMap<String, TaskListener> listeners = new ConcurrentHashMap<>();
    private final int execute_time;
    private int currentTime = 0;
    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
        onChangeTimerTask();
    }

    public Task(int id, GeoPoint point, String name, TypeGroupTask typeGroup, int baseCost, TypeTask typeTask, StatusTask statusTask, ArrayList<Requirement> requirements, int time) {
        super(id, name, point, typeTask, typeGroup.toModifier() * baseCost);
        this.requirements = requirements;
        this.execute_time = time;
        this.statusTask = statusTask;
        setTypeGroup(typeGroup);
    }

    public int getExecute_time() {
        return execute_time;
    }

    public String getCurrentTimeToString() {
        int s = currentTime / 1000;
        int sec = s % 60;
        int min = (s / 60) % 60;
        int hours = (s / 60) / 60;
        return hours + ":" + min + ":" + sec;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    @Override
    public int getImage() {
        switch (getType()) {
            case fire:
                switch (statusTask) {
                    case execute:
                        return R.drawable.ic_fire_green;
                    case wait:
                        return R.drawable.ic_fire_yellow;
                    case not_executed:
                        return R.drawable.ic_fire_red;
                }
            case robbery:
                switch (statusTask) {
                    case execute:
                        return R.drawable.ic_gun_green;
                    case wait:
                        return R.drawable.ic_gun_yellow;
                    case not_executed:
                        return R.drawable.ic_gun_red;
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
                return R.drawable.ic_map_marker;
        }
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
        onChangeStatusTask();
    }

    public int getColorStatus() {
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
            car.moving(getPosition(), StatusCar.OnCall);
            car.addListener(this, toString());
        }

        if (cars.size() != 0) {
            setStatusTask(StatusTask.wait);
        }
    }

    public void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Log.i(String.valueOf(SystemTag.timer), "start executed task");
                setStatusTask(StatusTask.executed);
                for (Car car : cars) {
                    {
                        Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            /**
                             * The action to be performed by this timer task.
                             */
                            @Override
                            public void run() {
                                car.removeListener(toString());
                                car.setStatus(StatusCar.Moving);
                                car.moving(car.getBuilding().getPosition(), StatusCar.Available);
                            }
                        }, 100);
                    }
                }
            }
        }, execute_time);

        Timer timerCounterTime = new Timer("TimerCounterTime");
        Log.i(String.valueOf(SystemTag.timer), "start TimerCounterTime");
        timerCounterTime.scheduleAtFixedRate(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                i++;
                setCurrentTime(execute_time - i * 1000);
                if (execute_time - i * 1000 <= 0) {
                    cancel();
                    Log.i(String.valueOf(SystemTag.timer), "stop TimerCounterTime");
                }
            }
        }, 0, 1000);
    }

    public void addListener(TaskListener toAdd, String TAG) {
        listeners.putIfAbsent(TAG, toAdd);
    }

    public void removeListener(String key) {
        listeners.entrySet().removeIf(entry -> entry.getKey().equals(key));
    }

    public void onChangeRequirement(String res) {
        listeners.forEach((key, value) -> {
            var elem = listeners.get(key);
            if (elem != null) {
                elem.onChangedRequirement(res);
            }
        });
    }

    public void onChangeStatusTask() {
        listeners.forEach((key, value) -> {
            var elem = listeners.get(key);
            if (elem != null) {
                elem.onChangeStatusTask(this);
            }
        });
    }

    public void onChangeTimerTask() {
        listeners.forEach((key, value) -> {
            var elem = listeners.get(key);
            if (elem != null) {
                elem.onChangeTimerTask(this);
            }
        });
    }

    @Override
    public void onStatusChanged(Car car) {
        String res = getRequirements();

        if (res.length() == 0) {
            if (getStatusTask() != StatusTask.execute && getStatusTask() != StatusTask.executed) {
                setStatusTask(StatusTask.execute);
                startTimer();
                onChangeRequirement(null);
            }
        } else {
            onChangeRequirement(res);
        }
    }

    @NonNull
    public String getRequirements() {
        StringBuilder res = new StringBuilder();
        for (Requirement req : requirements) {
            if (req.isNoDone(cars)) res.append(req);
        }
        return res.toString();
    }

    @Override
    public void onPositionChanged(Car car) {
    }
}
