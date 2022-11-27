package com.sozmi.dispatcher.model.objects;

import com.sozmi.dispatcher.R;

public enum TypeGroupTask implements BaseType {
    personal,
    common,
    premium,
    group;

    @Override
    public int toImageId(){
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

    @Override
    public String toType() {
        switch (this){
            case personal:
                return "personal";
            case common:
                return "common";
            case premium:
                return "premium";
            case group:
                return "group";
            default:
                return "unknown";
        }
    }

    public int toModifier() {
        switch (this){
            case personal:
                return 3;
            case common:
                return 1;
            case premium:
                return 10;
            case group:
                return 5;
            default:
                return -1;
        }
    }
}
