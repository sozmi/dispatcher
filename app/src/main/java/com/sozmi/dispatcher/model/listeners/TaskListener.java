package com.sozmi.dispatcher.model.listeners;

import com.sozmi.dispatcher.model.objects.Task;

public interface TaskListener {
    void onChangedRequirement(String res);
    void onChangeStatusTask(Task task);
    void onChangeTimerTask(Task task);
}
