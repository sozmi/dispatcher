package com.sozmi.dispatcher.model.system;

import android.os.Handler;
import android.os.Looper;

public class User extends ServerMod {

    private int money = 150000;
    private final int maxCountTask = 20;

    public User(int id, int money) {
        super(id);
        setMoney(money);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        new Handler(Looper.getMainLooper()).post(() -> DataController.setData(money + " руб."));

    }

    public boolean isNoMoney(int money) {
        return this.money < money;
    }

    public void addMoney(int add) {
        setMoney(money + add);
    }

    public void minMoney(int min) {
        setMoney(money - min);
    }

    public int getMaxCountTask() {
        return maxCountTask;
    }

}
