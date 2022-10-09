package com.sozmi.dispetcher.model;

import androidx.annotation.NonNull;

import com.sozmi.dispetcher.R;

public enum TypeCar {
    ambulance,
    police,
    fireTrack,
    none;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case none:
                return "нет";
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
            case none:
                return R.drawable.ic_map_marker;
            case fireTrack:
                return R.drawable.ic_firemen;
            case ambulance:
                return R.drawable.ic_hospital;
            case police:
                return R.drawable.ic_police;
            default:
                return -1;
        }
    }
}
