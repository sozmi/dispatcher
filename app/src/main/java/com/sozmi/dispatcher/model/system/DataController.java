package com.sozmi.dispatcher.model.system;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DataController {
    private static final MutableLiveData<String> liveData = new MutableLiveData<>();

    public static LiveData<String> getData() {
        return liveData;
    }

    public static void setData(String txt) {
        liveData.setValue(txt);
    }
}
