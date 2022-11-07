package com.sozmi.dispatcher.model.objects;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Requirement {
    private final int count;
    private final TypeCar type;
    private int temp=0;

    public Requirement(int count, TypeCar type) {
        this.count = count;
        this.type = type;
    }

    public boolean isNoDone(ArrayList<Car> cars){
        temp =0;
        for (Car car:cars) {
            if(car.getType()==type){
                temp++;
            }
        }
        return temp<count;
    }

    @NonNull
    @Override
    public String toString(){
        return type.toString()+"("+(count-temp)+"); ";
    }
}
