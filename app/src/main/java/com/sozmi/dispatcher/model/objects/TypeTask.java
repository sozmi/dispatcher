package com.sozmi.dispatcher.model.objects;

import com.sozmi.dispatcher.R;

public enum TypeTask implements BaseType {
    fire,
    epidemic,
    robbery;

    @Override
    public int toImageId() {
        switch (this) {
            case epidemic:
                return R.drawable.ic_epidemic_green;
            case fire:
                return R.drawable.ic_gun;
            case robbery:
                return R.drawable.ic_fire;
            default:
                return 0;
        }
    }

    @Override
    public String toType() {
        switch (this) {
            case epidemic:
                return "epidemic";
            case fire:
                return "fire";
            case robbery:
                return  "robbery";
            default:
                return "unknown";
        }
    }

    public static TypeTask getTypeTask(int index){
        switch (index) {
            case 0:
                return fire;
            case 1:
                return epidemic;
            case 2:
                return robbery;
            default:
                return null;
        }
    }
}
