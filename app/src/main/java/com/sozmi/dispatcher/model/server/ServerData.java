package com.sozmi.dispatcher.model.server;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.sozmi.dispatcher.BuildConfig;
import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.listeners.ServerListener;
import com.sozmi.dispatcher.model.navigation.HaversineAlgorithm;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.CarCheck;
import com.sozmi.dispatcher.model.objects.StatusCar;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.objects.TypeBuilding;
import com.sozmi.dispatcher.model.objects.TypeCar;
import com.sozmi.dispatcher.model.objects.User;
import com.sozmi.dispatcher.model.system.GenerateTasksTimer;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
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


    public static void loadLastVersion(Activity activity) throws NetworkException {
        final boolean[] isDownload = {true};
        Connection connection = new Connection(host, port);
        connection.sendData("get_version;" + BuildConfig.VERSION_NAME);
        String url = connection.getData();
        connection.disconnect();
        Log.d("Download", "Is last version:" + url.equals("true"));

        if (url.equals("true"))
            return;
        String destination = activity.getApplicationContext().getExternalFilesDir(DOWNLOAD_SERVICE) + "/";
        String fileName = "dispatcherMCS.apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        File file = new File(destination);
        if (file.exists()) {
            file.delete();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Update");
        request.setTitle("MyApp");
        request.setDestinationUri(uri);

        final DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Intent intentUpdate;
                File toInstall = new File(activity.getApplicationContext().getExternalFilesDir(DOWNLOAD_SERVICE), fileName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkUri = FileProvider.getUriForFile(activity.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", toInstall);
                    intentUpdate = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intentUpdate.setData(apkUri);
                    intentUpdate.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    Uri apkUri = Uri.fromFile(toInstall);
                    intentUpdate = new Intent(Intent.ACTION_VIEW);
                    intentUpdate.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intentUpdate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                activity.getApplicationContext().startActivity(intentUpdate);
                toInstall.deleteOnExit();

                activity.getApplicationContext().unregisterReceiver(this);
                activity.finish();
                isDownload[0] =false;
            }
        };
        activity.getApplicationContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        while (isDownload[0]){}
    }


    public static boolean Authorization(String email, String passwd, boolean isSave) throws NetworkException, DataException {
        Connection connection = new Connection(host, port);
        connection.sendData("get_user;" + email + "|" + passwd);
        String s = connection.getData();
        connection.disconnect();
        if (s.equals("no_find")) {
            throw new DataException("Неверный логин или пароль", "no_find");
        }
        if (user.loadData(s)) {
            if (isSave) {
                ServerData.addEmail(email);
                ServerData.addPasswd(passwd);
            }

            return true;
        }
        return false;
    }

    public static boolean Registration(String email, String passwd, String name) throws NetworkException, DataException {
        Connection connection = new Connection(host, port);
        connection.sendData("add_user;" + email + "|" + name + "|" + passwd);
        String s = connection.getData();
        connection.disconnect();
        if (s.equals("email_exist"))
            throw new DataException("Email уже зарегистрирован", "email");
        else if (s.equals("name_exist"))
            throw new DataException("Username уже существует.", "passwd");

        if (user.loadData(s)) {
            ServerData.addEmail(email);
            ServerData.addPasswd(passwd);
            return loader();
        }
        connection.disconnect();
        return false;
    }

    public static boolean loader() throws NetworkException {
        Connection connection = new Connection(host, port);
        connection.sendData("get_build;" + getUser().getID());
        String build_str = connection.getData();
        if (build_str.equals("no_find")) return true;
        String[] b_str = build_str.split(";");
        Log.d("BUILD_INFO", "Building count:" + b_str.length);
        for (String sb : b_str) {
            Building build = new Building(sb);
            connection.sendData("get_cars;" + build.getID());
            String s = connection.getData();
            if (!s.equals("no_find"))
                build.addCar(s);
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
        editor.putString(APP_PREFERENCES_PASSWD, passwd);
        editor.apply();
        Log.d("Config", "add password to config setting");
    }

    public static void addListener(ServerListener toAdd, String TAG) {
        listeners.putIfAbsent(TAG, toAdd);
    }

    public static void addTaskListener(Task task) {
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
        timer.scheduleAtFixedRate(new GenerateTasksTimer(), 0, 60000);
    }

    public static boolean isLoginSaved() {
        boolean res = mSettings.contains(APP_PREFERENCES_EMAIL) && mSettings.contains(APP_PREFERENCES_PASSWD);
        Log.d("Config", "Login is save in config:" + res);
        return res;
    }


    public static void addBuild(String name, GeoPoint point, TypeBuilding typeBuilding) throws NetworkException, DataException {
        if (user.isNoMoney(typeBuilding.toCost())) {
            throw new DataException("Недостаточно средств", "no_money");
        } else if (isBuildingExist(point)) {
            throw new DataException("Здание находится слишком близко к уже существующим", "no_money");
        } else if (user.getMaxCountBuilding() < buildings.size()) {
            throw new DataException("Превышено максимальное число построек. Нельзя построить больше" + user.getMaxCountBuilding() + " зданий", "no_create");
        } else {

            Building b = new Building(buildings.size(), name, typeBuilding, point);

            Connection connection = new Connection(host, port);
            connection.sendData("add_build;" + b.getPositionString() + "|" + name + "|" + typeBuilding.toType() + "|" + getUser().getID());
            String s = connection.getData();
            if (s.equals("false"))
                throw new DataException("Не удалось создать здание", "no_create");
            else {
                b.setID(Integer.parseInt(s));
                user.minMoney(typeBuilding.toCost());
                buildings.add(b);

            }
            connection.disconnect();

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

    public static void addCar(Building building, TypeCar typeCar) throws NetworkException, DataException {
        if (user.isNoMoney(typeCar.toCost())) {
            throw new DataException("Недосаточно средств", "no_create");
        } else if (user.getMaxCarInBuilding() < building.getCars().size()) {
            throw new DataException("В здании может быть не больше " + user.getMaxCarInBuilding() + " машин", "no_create");
        } else {
            Car car = new Car(carIndex++, typeCar.name(), typeCar, StatusCar.Available, building.getPosition(), building);
            Connection connection = new Connection(host, port);
            connection.sendData("add_car;" + building.getPositionString() + "|" + typeCar.name() + "|" + typeCar.toType() + "|" + car.getStatus().toType() + "|" + building.getID());
            String s = connection.getData();
            if (s.equals("false"))
                throw new DataException("Не удалось создать машину", "no_create");
            else {
                car.setID(Integer.parseInt(s));
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
                user.minMoney(typeCar.toCost());
            }
            connection.disconnect();
        }
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

    public static boolean AuthorizationSave() throws NetworkException, DataException {
        return Authorization(mSettings.getString(APP_PREFERENCES_EMAIL, ""), mSettings.getString(APP_PREFERENCES_PASSWD, ""), false);
    }

    public static boolean authorization(Activity activity) throws NetworkException, DataException {
        loadSettings(activity);
        if (isLoginSaved()) {
            return AuthorizationSave();
        }
        return false;
    }


    @NonNull
    @Override
    public String toString() {
        return "ServerClass";
    }

    public static void unloader() throws NetworkException {
        Connection connection = new Connection(host, port);
        connection.sendData("upd_user;" + getUser().getID() + "|" + getUser().getMoney());
        connection.getData();
        connection.disconnect();

    }
}

