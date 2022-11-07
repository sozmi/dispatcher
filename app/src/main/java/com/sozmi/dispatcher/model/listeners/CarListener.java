package com.sozmi.dispatcher.model.listeners;

import com.sozmi.dispatcher.model.objects.Car;

public interface CarListener {
    void onStatusChanged(Car car);

    void onPositionChanged(Car car);
}
