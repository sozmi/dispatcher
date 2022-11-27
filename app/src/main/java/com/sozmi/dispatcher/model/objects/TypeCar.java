package com.sozmi.dispatcher.model.objects;

import androidx.annotation.NonNull;

import com.sozmi.dispatcher.R;


public enum TypeCar implements BaseType {
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
                return "unknown";
        }
    }

    @Override
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

    @Override
    public String toType() {
        switch (this) {
            case fireTrack:
                return "fireTrack";
            case ambulance:
                return "ambulance";
            case police:
                return "police";
            default:
                return "unknown";
        }
    }

    public int toCost() {
        switch (this) {
            case fireTrack:
                return 5000;
            case ambulance:
                return 10000;
            case police:
                return 9000;
            default:
                return -1;
        }
    }
}
