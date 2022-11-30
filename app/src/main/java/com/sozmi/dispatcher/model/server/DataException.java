package com.sozmi.dispatcher.model.server;

public class DataException extends Exception{
    private final String type;
    DataException(String mess,String type){
        super(mess);
        this.type=type;
    }

    public String getType() {
        return type;
    }
}
