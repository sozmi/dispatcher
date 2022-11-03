package com.sozmi.dispatcher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.adapters.CarAdapter;
import com.sozmi.dispatcher.adapters.CarCheckAdapter;
import com.sozmi.dispatcher.model.objects.CarCheck;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Server;
import com.sozmi.dispatcher.model.system.Tag;
import com.sozmi.dispatcher.ui.MyListView;

import java.util.ArrayList;

public class TaskFragment extends Fragment {
    Task task;
    MyListView free_car, on_call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        TextView name = view.findViewById(R.id.nameTask);
        Button send = view.findViewById(R.id.sendCarTask);
        Button back = view.findViewById(R.id.backTask);
        free_car = view.findViewById(R.id.lst_free_car);
        on_call = view.findViewById(R.id.lst_on_call);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int id = bundle.getInt(Tag.TaskID.toString());
            task = Server.getTask(id);
            on_call.setAdapter(new CarAdapter(view.getContext(), R.layout.fragment_item_car, task.getCars()));
            free_car.setAdapter(new CarCheckAdapter(view.getContext(), R.layout.fragment_item_car_check, Server.getFreeCars()));
            name.setText(task.getName());
            send.setOnClickListener(v -> OnClickSendButton());
            back.setOnClickListener(v -> OnClickBackButton());
        }

        return view;
    }

    private void OnClickSupportButtom() {

    }

    private void OnClickSendButton() {
        CarCheckAdapter adapter = (CarCheckAdapter) free_car.getAdapter();
        CarAdapter adapter_on_call = (CarAdapter) on_call.getAdapter();
        ArrayList<CarCheck> cars_lst =adapter.getItems();
        ArrayList<CarCheck> cars =new ArrayList<>();
        for (int i=0;i<cars_lst.size();i++){
            CarCheck car = cars_lst.get(i);
            if(car.getCheck()){
                adapter_on_call.add(car.getCar());
                adapter.remove(car);
                cars.add(car);
                i--;
            }
        }
        task.sendCars(cars);
        adapter.notifyDataSetChanged();
        adapter_on_call.notifyDataSetChanged();

    }

    private void OnClickBackButton() {
        MyFM.OpenFragment(new TasksFragment(), null);
    }

    @NonNull
    @Override
    public String toString() {
        return "TaskFragment";
    }


}