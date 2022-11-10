package com.sozmi.dispatcher.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.adapters.CarCheckAdapter;
import com.sozmi.dispatcher.adapters.CarViewAdapter;
import com.sozmi.dispatcher.model.listeners.CarListener;
import com.sozmi.dispatcher.model.listeners.TaskListener;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.CarCheck;
import com.sozmi.dispatcher.model.objects.StatusTask;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Server;
import com.sozmi.dispatcher.model.system.Tag;
import com.sozmi.dispatcher.ui.MyListView;

import java.util.ArrayList;

public class TaskFragment extends Fragment implements TaskListener, CarListener {
    Task task;
    MyListView free_car;
    RecyclerView on_call;
    TextView requirement, timeView;
    ImageView imageView;
    ProgressBar progressBar;
    LinearLayout panelTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        TextView name = view.findViewById(R.id.nameTask);
        Button send = view.findViewById(R.id.sendCarTask);
        Button back = view.findViewById(R.id.backTask);
        imageView = view.findViewById(R.id.imageGroupTask);
        free_car = view.findViewById(R.id.lst_free_car);
        on_call = view.findViewById(R.id.lst_on_call);
        requirement = view.findViewById(R.id.requirement);
        panelTimer = view.findViewById(R.id.panelTimerTask);
        progressBar = view.findViewById(R.id.progressBarTask);
        timeView = view.findViewById(R.id.timerTask);

        Bundle bundle = getArguments();
        if (bundle != null) {
            task = (Task) bundle.getSerializable(Tag.TaskID.toString());

            on_call.setLayoutManager(new LinearLayoutManager(getContext()));
            on_call.setAdapter(new CarViewAdapter(task.getCars(), view));
            free_car.setAdapter(new CarCheckAdapter(view.getContext(), R.layout.fragment_item_car_check, Server.getFreeCars()));


            name.setText(task.getName());
            send.setOnClickListener(v -> OnClickSendButton());
            back.setOnClickListener(v -> OnClickBackButton());
            imageView.setImageResource(task.getImageGroupTask());
            imageView.setBackgroundResource(task.getColorStatus());
            task.addListener(this,toString());
            onChangedRequirement(task.getRequirements());
        }

        return view;
    }

    private void OnClickSupportButtom() {

    }

    private void OnClickSendButton() {
        CarCheckAdapter adapter = (CarCheckAdapter) free_car.getAdapter();
        CarViewAdapter adapter_on_call = (CarViewAdapter) on_call.getAdapter();
        ArrayList<CarCheck> cars_lst = adapter.getItems();
        ArrayList<CarCheck> cars = new ArrayList<>();
        for (int i = 0; i < cars_lst.size(); i++) {
            CarCheck car = cars_lst.get(i);
            if (car.getCheck()) {
                task.getCars().add(car.getCar());
                assert adapter_on_call != null;
                adapter_on_call.notifyItemInserted(task.getCars().size() - 2);
                car.getCar().addListener(this, toString());
                adapter.remove(car);
                cars.add(car);
                i--;
            }
        }
        task.sendCars(cars);
        adapter.notifyDataSetChanged();
    }

    private void OnClickBackButton() {
        MyFM.OpenFragment(new TasksFragment(), null);
    }

    @NonNull
    @Override
    public String toString() {
        return "TaskFragment";
    }


    @Override
    public void onChangedRequirement(String res) {
        if (res != null && !res.equals("")) {
            new Handler(Looper.getMainLooper()).post(() -> {
                requirement.setText(res);
                requirement.setVisibility(View.VISIBLE);
            });

        } else
            new Handler(Looper.getMainLooper()).post(() -> requirement.setVisibility(View.GONE));

    }

    @Override
    public void onChangeStatusTask(Task task) {
        if (task.getStatusTask() == StatusTask.execute) {
            new Handler(Looper.getMainLooper()).post(() -> {
                panelTimer.setVisibility(View.VISIBLE);
                progressBar.setMax(task.getExecute_time());
            });
        }
        new Handler(Looper.getMainLooper()).post(() -> {
            imageView.setImageResource(task.getImageGroupTask());
            imageView.setBackgroundResource(task.getColorStatus());
        });
    }

    @Override
    public void onChangeTimerTask(Task task) {
        new Handler(Looper.getMainLooper()).post(() -> {
            progressBar.setProgress(task.getCurrentTime());
            timeView.setText(task.getCurrentTimeToString());
        });
    }

    @Override
    public void onStatusChanged(Car car) {
        CarViewAdapter adapter = (CarViewAdapter) on_call.getAdapter();
        CarCheckAdapter adapter2 = (CarCheckAdapter) free_car.getAdapter();
        new Handler(Looper.getMainLooper()).post(() -> {
            assert adapter != null;
            adapter.notifyItemChanged(task.getCars().indexOf(car));
            adapter2.notifyDataSetChanged();
        });
    }

    @Override
    public void onPositionChanged(Car car) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}