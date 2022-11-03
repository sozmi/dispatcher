package com.sozmi.dispatcher.model.system;

import androidx.annotation.NonNull;

public enum Tag {
    Point,
    viewPanel,
    TaskID;

    @NonNull
    @Override
    public String toString() {
        switch (this){
            case Point:return "point";
            case TaskID:return "taskID";
            case viewPanel:return "viewPanel";
            default: return "error";
        }
    }
}
