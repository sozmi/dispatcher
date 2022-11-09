package com.sozmi.dispatcher.model.system;

import androidx.annotation.NonNull;

public enum SystemTag {
    timer,
    map,
    method;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case timer:
                return "Timer";
            case method:
                return "Method";
            case map:
                return "MapClass";
            default:
                return "Error Enum";
        }
    }
}
