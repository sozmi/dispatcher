package com.sozmi.dispatcher.model.objects;

import androidx.annotation.NonNull;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.objects.TypeCar;

public enum TypeBuilding implements BaseType{
    hospital,
    police,
    fire_station;

    @NonNull
    @Override
    public String toString(){
        switch (this){
            case fire_station: return "Пожарная станция";
            case hospital: return "Больница";
            case police: return "Полицейский участок";
            default: return "Неизвестно";
        }
    }

    @Override
    public int toImageId(){
        switch (this){
            case fire_station: return R.drawable.ic_firemen;
            case hospital: return R.drawable.ic_hospital;
            case police: return R.drawable.ic_police;
            default: return -1;
        }
    }

    public int toCost(){
        switch (this){
            case fire_station:
            case police:
                return 25000;
            case hospital: return 50000;
            default: return -222220;
        }
    }

    public TypeCar toCar() {
        switch (this){
            case fire_station: return TypeCar.fireTrack;
            case hospital: return TypeCar.ambulance;
            case police: return TypeCar.police;
            default: return null;
        }
    }
}

