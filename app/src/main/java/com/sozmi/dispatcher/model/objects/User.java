package com.sozmi.dispatcher.model.objects;


import android.util.Log;

import com.sozmi.dispatcher.model.listeners.DataListner;
import com.sozmi.dispatcher.model.system.BaseObject;

import java.util.concurrent.ConcurrentHashMap;

public class User extends BaseObject {

    private String name;
    private int money;
    private  int maxCountTask;
    private final ConcurrentHashMap<String, DataListner<Integer>> listeners = new ConcurrentHashMap<>();

    public User() {
        super(-100);
        this.money =-10000;
        this.name =null;
        this.maxCountTask =20;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        updateMoney(money);
    }

    public void addListener(DataListner<Integer> toAdd, String TAG) {
        listeners.putIfAbsent(TAG, toAdd);
    }

    private void updateMoney(int money) {
        listeners.forEach((key, value) -> {
            var elem = listeners.get(key);
            if (elem != null) {
                elem.onChangeData(money);
            }
        });
    }

    public  void removeListener(String key) {
        listeners.entrySet().removeIf(entry -> entry.getKey().equals(key));
    }

    public void setName(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public boolean loadData(String data) {
        var d = data.split("\\|");
        if (d.length ==3) {
            setID(Integer.parseInt(d[0]));
            setName(d[1]);
            setMoney(Integer.parseInt(d[2]));
            return  true;
        } else {
            try {
                Log.d("SOCKET", "Get message: "+data);
                throw new Exception("Передали не пользователя");

            } catch (Exception e) {
                Log.d("SOCKET", e.getMessage());
                Log.d("SOCKET", "Get message: "+data);
            e.printStackTrace();
            }
            return false;
        }
    }
}