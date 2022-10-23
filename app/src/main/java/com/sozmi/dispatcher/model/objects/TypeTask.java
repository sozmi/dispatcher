package com.sozmi.dispatcher.model.objects;

import com.sozmi.dispatcher.R;

public enum TypeTask {
    personal,
    common,
    premium,
    group;

    public int toImage(){
        switch (this){
            case personal:
                return R.drawable.ic_user;
            case common:
                return R.drawable.ic_common;
            case premium:
                return R.drawable.ic_star;
            case group:
                return R.drawable.ic_group;
            default:
                return -1;
        }
    }
}
