package com.sozmi.dispatcher.model.objects;

import com.sozmi.dispatcher.R;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Task extends Object<TypeIconTask> {

    private TypeGroupTask typeGroup;
    private int baseCost;
    private StatusTask statusTask;
    private ArrayList<Car> cars;

    public Task(GeoPoint point, String name, TypeGroupTask type, int baseCost, TypeIconTask image, StatusTask statusTask) {
        super(name,point,image);
        setStatusTask(statusTask);
        setTypeGroup(type);
        setBaseCost(baseCost);
    }
    @Override
    public int getImage(){
    switch (type){
        case fire:
            switch (statusTask){
                case executed: return R.drawable.ic_epidemic_green;
                case wait: return R.drawable.ic_epidemic_yellow;
                case not_executed: return R.drawable.ic_epidemic_red;
            }
        case robbery:
            switch (statusTask){
                case executed: return R.drawable.ic_epidemic_green;
                case wait: return R.drawable.ic_epidemic_yellow;
                case not_executed: return R.drawable.ic_epidemic_red;
            }
        case epidemic:
            switch (statusTask){
                case executed: return R.drawable.ic_epidemic_green;
                case wait: return R.drawable.ic_epidemic_yellow;
                case not_executed: return R.drawable.ic_epidemic_red;
            }
        default:return -1;
    }
    }
    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeGroupTask getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(TypeGroupTask typeGroup) {
        this.typeGroup = typeGroup;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
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
}
