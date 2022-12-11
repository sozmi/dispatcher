package com.sozmi.dispatcher.model.system;

import android.util.Log;

import com.sozmi.dispatcher.model.listeners.TaskListener;
import com.sozmi.dispatcher.model.navigation.Coordinate;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.objects.Requirement;
import com.sozmi.dispatcher.model.objects.StatusTask;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.objects.TypeCar;
import com.sozmi.dispatcher.model.objects.TypeGroupTask;
import com.sozmi.dispatcher.model.objects.TypeTask;
import com.sozmi.dispatcher.model.server.ServerData;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

public class GenerateTasksTimer extends TimerTask {
    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        Log.d("tag","timer t");
        var buildings = ServerData.getBuildings();
        var tasks =ServerData.getTasks();
        var user =ServerData.getUser();
        if (buildings.size() <= 0) return;
        Log.i(String.valueOf(SystemTag.timer), "start TimerGenerateTasks");
        if (tasks.size() >= user.getMaxCountTask()) return;
        int taskID = tasks.size();
        GeoPoint point = Coordinate.getLocationInLatLngRad(10000, getRandomBuilding(buildings).getPosition());
        ArrayList<Requirement> requirements = new ArrayList<>();
        requirements.add(new Requirement(1, TypeCar.police));
        requirements.add(new Requirement(3, TypeCar.fire_truck));
        requirements.add(new Requirement(1, TypeCar.ambulance));
        Random random = new Random();
        int index = random.nextInt(TypeTask.values().length);
        int time = 60000;
        Task task = new Task(taskID, point,  TypeTask.getTypeTask(index).toString(), TypeGroupTask.personal, 500, TypeTask.getTypeTask(index), StatusTask.not_executed, requirements, time);

        task.addListener(new TaskListener() {
            @Override
            public void onChangedRequirement(String res) {
            }

            @Override
            public void onChangeStatusTask(Task task) {
                if (task.getStatusTask() == StatusTask.executed) {
                    task.setIndex(tasks.indexOf(task));
                    ServerData.getUser().addMoney(task.getCost());
                    tasks.remove(task);
                    if (tasks.size() > user.getMaxCountTask())
                        ServerData.generateTask();
                }
            }

            @Override
            public void onChangeTimerTask(Task task) {
            }
        }, toString());
        tasks.add(task);
        ServerData.addTaskListener(task);
    }
    private static Building getRandomBuilding(ArrayList<Building> buildings) {
        Random random = new Random();
        int index = random.nextInt(buildings.size());
        return buildings.get(index);
    }
}
