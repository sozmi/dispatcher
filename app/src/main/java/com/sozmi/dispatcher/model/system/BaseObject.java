package com.sozmi.dispatcher.model.system;

public class BaseObject {
    private int ID;

    public BaseObject(){
        ID =-1;
    }
    public BaseObject(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
