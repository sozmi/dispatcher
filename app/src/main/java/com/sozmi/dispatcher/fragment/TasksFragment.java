package com.sozmi.dispatcher.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.adapters.TaskViewAdapter;
import com.sozmi.dispatcher.model.listeners.TaskListener;
import com.sozmi.dispatcher.model.objects.StatusTask;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.system.Server;

import java.util.Objects;


public class TasksFragment extends Fragment implements TaskListener {
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        var tasks = Server.getTasks();
        for (Task task : tasks
        ) {
            task.addListener(this,toString());
        }
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new TaskViewAdapter(tasks));
            ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        var tasks = Server.getTasks();
        for (Task task : tasks
        ) {
            task.removeListener(toString());
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "TasksFragment";
    }

    @Override
    public void onChangedRequirement(String res) {

    }

    @Override
    public void onChangeStatusTask(Task task) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (task.getStatusTask() == StatusTask.executed){
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRemoved(task.getIndex());
            }
            else {
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(task.getIndex());
            }
        });
    }

    @Override
    public void onChangeTimerTask(Task task) {
        int id = Server.getTasks().indexOf(task);
        new Handler(Looper.getMainLooper()).post(() -> {
            if (task.getStatusTask() == StatusTask.execute){
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(id);
            }
        });
    }
}