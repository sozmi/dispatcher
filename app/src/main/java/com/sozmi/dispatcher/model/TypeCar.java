package com.sozmi.dispatcher.model;

import androidx.annotation.NonNull;

import com.sozmi.dispatcher.R;


public enum TypeCar {
    ambulance,
    police,
    fireTrack;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case fireTrack:
                return "Пожарная машина";
            case ambulance:
                return "Скорая помощь";
            case police:
                return "Полицейская машина";
            default:
                return "null";
        }
    }

    public int toImageId() {
        switch (this) {
            case fireTrack:
                return R.drawable.ic_fire_truck;
            case ambulance:
                return R.drawable.ic_ambulance;
            case police:
                return R.drawable.ic_police_car;
            default:
                return -1;
        }
    }
}
