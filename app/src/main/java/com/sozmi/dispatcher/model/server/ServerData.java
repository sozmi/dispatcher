package com.sozmi.dispatcher.model.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.listeners.ServerListener;
import com.sozmi.dispatcher.model.listeners.TaskListener;
import com.sozmi.dispatcher.model.navigation.Coordinate;
import com.sozmi.dispatcher.model.navigation.HaversineAlgorithm;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.CarCheck;
import com.sozmi.dispatcher.model.objects.Requirement;
import com.sozmi.dispatcher.model.objects.StatusCar;
import com.sozmi.dispatcher.model.objects.StatusTask;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.objects.TypeBuilding;
import com.sozmi.dispatcher.model.objects.TypeCar;
import com.sozmi.dispatcher.model.objects.TypeGroupTask;
import com.sozmi.dispatcher.model.objects.TypeTask;
import com.sozmi.dispatcher.model.objects.User;
import com.sozmi.dispatcher.model.system.SystemTag;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class ServerData {
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_EMAIL = "User_email";
    public static final String APP_PREFERENCES_PASSWD = "User_password";
    static SharedPreferences mSettings; //файл настроек
    private static final ArrayList<Building> buildings = new ArrayList<>();
    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static final ConcurrentHashMap<String, Car> carInMovement = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ServerListener> listeners = new ConcurrentHashMap<>();
    private static final User user = new User();
    private static int carIndex = 0;
    private static final String host = "82.179.140.18";
    private static final int port = 45555;

    public static boolean Authorization(String email, String passwd) throws InterruptedException, IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = new Connection(host, port);
        connection.sendData("get_user;" + email + "|" + email.substring(0, email.indexOf("@")) + "|" + passwd);
        String s = connection.getData();
        connection.disconnect();
        if (user.loadData(s)) {
            return loader();
        }
        return false;
    }

    private static boolean loader() throws IOException, InterruptedException {

        Connection connection = new Connection(host, port);
        connection.sendData("get_build;" + getUser().getID());
        String build_str = connection.getData();
        if(build_str.equals("no_find")) return true;
        String[] b_str = build_str.split(";");
        Log.d("BUILD_INFO", "Building count:" + b_str.length);
        for (String sb : b_str) {
                Building build = new Building(sb);
//            connection.sendData("get_cars;"+build.getID());
//            String s = connection.getData();
//
//            build.addCar(s);
            buildings.add(build);
        }

        connection.disconnect();
        generateTask();
        return true;
    }

    public static void loadSettings(Activity activity) {
        mSettings = activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void addEmail(String email) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_EMAIL, email);
        editor.apply();
        Log.d("Config", "add email to config setting");
    }

    public static void addPasswd(String passwd) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_EMAIL, passwd);
        editor.apply();
        Log.d("Config", "add password to config setting");
    }

    public static void addListener(ServerListener toAdd, String TAG) {
        listeners.putIfAbsent(TAG, toAdd);
    }

    public static void addTask(Task task) {
        listeners.forEach((key, value) -> {
            var elem = listeners.get(key);
            if (elem != null) {
                elem.addTask(task);
            }
        });
    }

    public static void removeListener(String key) {
        listeners.entrySet().removeIf(entry -> entry.getKey().equals(key));
    }

    public static void addCarInMovement(Car toAdd, String TAG) {
        carInMovement.putIfAbsent(TAG, toAdd);
    }

    public static void removeCarInMovement(String key) {
        carInMovement.entrySet().removeIf(entry -> entry.getKey().equals(key));
    }

    public static ArrayList<Building> getBuildings() {
        return buildings;
    }

    public static ArrayList<Task> getTasks() {
        return tasks;
    }

    public static void generateTask() {
        Timer timer = new Timer("TimerGenerateTasks");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (buildings.size() <= 0) return;
                Log.i(String.valueOf(SystemTag.timer), "start TimerGenerateTasks");
                if (tasks.size() >= user.getMaxCountTask()) cancel();
                int taskID = tasks.size();
                GeoPoint point = Coordinate.getLocationInLatLngRad(10000, getRandomBuilding().getPosition());
                ArrayList<Requirement> requirements = new ArrayList<>();
                requirements.add(new Requirement(1, TypeCar.police));
                requirements.add(new Requirement(3, TypeCar.fireTrack));
                requirements.add(new Requirement(1, TypeCar.ambulance));
                Random random = new Random();
                int index = random.nextInt(TypeTask.values().length);
                int time = 60000;
                Task task = new Task(taskID, point, "Пожар" + taskID, TypeGroupTask.personal, 500, TypeTask.getTypeTask(index), StatusTask.not_executed, requirements, time);

                task.addListener(new TaskListener() {
                    @Override
                    public void onChangedRequirement(String res) {
                    }

                    @Override
                    public void onChangeStatusTask(Task task) {
                        if (task.getStatusTask() == StatusTask.executed) {
                            task.setIndex(tasks.indexOf(task));
                            user.addMoney(task.getCost());
                            tasks.remove(task);
                            if (tasks.size() > user.getMaxCountTask()) generateTask();
                        }
                    }

                    @Override
                    public void onChangeTimerTask(Task task) {
                    }
                }, toString());
                tasks.add(task);
                addTask(task);

            }
        }, 0, 60000);
    }

    private static Building getRandomBuilding() {
        Random random = new Random();
        int index = random.nextInt(buildings.size());
        return buildings.get(index);
    }

    public static boolean isLoginSaved() {
        boolean res = mSettings.contains(APP_PREFERENCES_EMAIL) && mSettings.contains(APP_PREFERENCES_PASSWD);
        Log.d("Config", "Login is save in config:" + res);
        return res;
    }


    public static String addBuild(String name, GeoPoint point, TypeBuilding typeBuilding) {
        if (user.isNoMoney(typeBuilding.toCost())) {
            return "Недостаточно средств";
        } else if (isBuildingExist(point)) {
            return "Здание находится слишком близко к уже существующим";
        } else {
            user.minMoney(typeBuilding.toCost());
            Building b = new Building(buildings.size(), name, typeBuilding, point);
            buildings.add(b);

            return null;
        }
    }

    /**
     * Если здание существует рядом
     *
     * @param position позиция нового здания
     * @return Если здание существует рядом - true
     */
    private static boolean isBuildingExist(GeoPoint position) {
        final int MAX = 1000;//в метрах
        for (Building building : buildings) {
            if (HaversineAlgorithm.HaversineInM(building.getPosition(), position) < MAX)
                return true;
        }
        return false;
    }

    public static boolean addCar(Building building, TypeCar typeCar) {
        if (user.isNoMoney(typeCar.toCost())) return false;
        user.minMoney(typeCar.toCost());

        Car car = new Car(carIndex++, typeCar.name(), typeCar, StatusCar.Available, building.getPosition(), building);
        car.addListener(new CarListener() {
            @Override
            public void onStatusChanged(Car car) {
                if (StatusCar.OnCall == car.getStatus() || car.getStatus() == StatusCar.Available)
                    removeInMovement(car);
                else if (car.getStatus() == StatusCar.Moving || car.getStatus() == StatusCar.MovingOnCall)
                    addInMovement(car);
            }

            @Override
            public void onPositionChanged(Car car) {

            }
        }, "ClassServer");
        building.addCar(car);
        return true;
    }

    public static ArrayList<CarCheck> getFreeCars() {
        ArrayList<CarCheck> cars = new ArrayList<>();
        for (Building build : buildings)
            for (Car car : build.getCars())
                if (car.getStatus() == StatusCar.Available) cars.add(new CarCheck(car));
        return cars;
    }

    public static User getUser() {
        return user;
    }

    public static ArrayList<Car> getInMovement() {
        return new ArrayList<>(carInMovement.values());
    }

    public static void addInMovement(Car car) {
        addCarInMovement(car, car.getID() + "");
    }

    public static void removeInMovement(Car car) {
        removeCarInMovement(car.getID() + "");
    }

    public static boolean AuthorizationSave() throws IOException, InterruptedException, NetworkException {
        return Authorization(mSettings.getString(APP_PREFERENCES_EMAIL, ""), mSettings.getString(APP_PREFERENCES_PASSWD, ""));
    }

    @NonNull
    @Override
    public String toString() {
        return "ServerClass";
    }

    public static void unloader() throws IOException, InterruptedException, NetworkException {
        Connection connection = new Connection(host, port);
        connection.sendData("upd_user;" + getUser().getID() + "|" + getUser().getMoney());
        connection.getData();
        connection.disconnect();

    }
}

