package com.sozmi.dispatcher.model.listeners;

import com.sozmi.dispatcher.model.objects.Task;

public interface ServerListener {
    void addTask(Task task);
}
