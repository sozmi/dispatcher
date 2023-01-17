package com.sozmi.dispatcher.model.system;

import androidx.annotation.NonNull;

public enum Tag {
    point,
    viewPanel,
    building,
    task;

    @NonNull
    @Override
    public String toString() {
        switch (this){
            case point:return "point";
            case task:return "taskID";
            case building: return  "building";
            case viewPanel:return "viewPanel";
            default: return "error";
        }
    }
}
