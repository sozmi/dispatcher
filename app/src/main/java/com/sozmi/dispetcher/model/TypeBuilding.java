package com.sozmi.dispetcher.model;

import androidx.annotation.NonNull;

import com.sozmi.dispetcher.R;

public enum TypeBuilding {
    hospital,
    police,
    fire_station,
    none;

    @NonNull
    @Override
    public String toString(){
        switch (this){
            case none: return "нет";
            case fire_station: return "Пожарная станция";
            case hospital: return "Больница";
            case police: return "Полицейский участок";
            default: return "null";
        }
    }

    public int toImageId(){
        switch (this){
            case none: return R.drawable.ic_map_marker;
            case fire_station: return R.drawable.ic_firemen;
            case hospital: return R.drawable.ic_hospital;
            case police: return R.drawable.ic_police;
            default: return -1;
        }
    }
}

