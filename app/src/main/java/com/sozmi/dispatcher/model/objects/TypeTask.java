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
                return R.drawable.ic_fire;
            case robbery:
                return R.drawable.ic_robbery;
            default:
                return 0;
        }
    }
}
