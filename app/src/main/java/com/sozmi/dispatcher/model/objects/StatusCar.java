package com.sozmi.dispatcher.model.objects;

import androidx.annotation.NonNull;

import com.sozmi.dispatcher.R;

public enum StatusCar {
    Available,//доступна
    OnCall,//на месте вызова
    Moving,
    MovingOnCall,//возврат на базу
    Unavailable; //недоступна

    @Override
    @NonNull
    public String toString(){
        switch (this){
            case Available: return "0";
            case Moving: return "1";
            case OnCall: return "2";
            case Unavailable: return "3";
            case MovingOnCall:return "4";
            default: return  "-1";
        }
    }

    public int toColor(){
        switch (this){
            case Available: return R.color.green;
            case Moving: return R.color.yellow;
            case OnCall: return R.color.blue;
            case Unavailable: return R.color.dark_gray;
            case MovingOnCall:return R.color.green_dark;
            default: return  0;
        }
    }
}
