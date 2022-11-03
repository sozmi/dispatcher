package com.sozmi.dispatcher.model.objects;

public class CarCheck {
    private boolean isCheck;
    private Car car;


    public CarCheck(Car car) {
        setCar(car);
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean getCheck() {
        return isCheck;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
