package com.sozmi.dispatcher.model.system;

public class User extends ServerMod {

    private int money=150000;
    public User(int id,int money) {
        super(id);
        setMoney(money);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        DataController.setData(money+" руб.");
    }

    public boolean isNoMoney(int money){
        return this.money<money;
    }

    public void addMoney(int add){
        setMoney(money+add);
    }

    public void minMoney(int min){
        setMoney(money-min);
    }
}
