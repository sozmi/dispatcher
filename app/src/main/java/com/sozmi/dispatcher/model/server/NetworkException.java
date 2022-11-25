package com.sozmi.dispatcher.model.server;

public class NetworkException extends RuntimeException{
    NetworkException(String mess){
        super(mess);
    }
}
