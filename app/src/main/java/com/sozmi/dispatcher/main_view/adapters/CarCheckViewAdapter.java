package com.sozmi.dispatcher.main_view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.CarCheck;

import java.util.ArrayList;
import java.util.List;

public class CarCheckViewAdapter extends RecyclerView.Adapter<CarCheckViewAdapter.ViewHolder> {

    private final List<CarCheck> mValues;

    private final View view;

    public ArrayList<CarCheck> getValues() {
        return (ArrayList<CarCheck>) mValues;
    }

    public CarCheckViewAdapter(List<CarCheck> items, View v) {
        mValues = items;
        this.view = v;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_car_check, parent, false);
        return new CarCheckViewAdapter.ViewHolder(view);
    }

    //В методе адаптера onBindViewHolder() связываем используемые текстовые метки с данными
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CarCheck carCheck = mValues.get(position);
        Car car =carCheck.getCar();
        holder.mNameView.setText(car.getName());
        holder.mTypeView.setText(car.getType().toString());
        holder.mImageView.setImageResource(car.getImage());
        holder.mStatusView.setText(car.getStatusToString());
        holder.mStatusView.setBackgroundColor(car.getColor(view.getContext()));
        holder.mCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
           carCheck.setCheck(isChecked);
            notifyItemChanged(position);
        });
        holder.mCheck.setChecked(carCheck.getCheck());
    }


    /**
     * @return количество элементов списка.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //Класс MyViewHolder на основе ViewHolder служит для оптимизации ресурсов.
    // Он служит своеобразным контейнером для всех компонентов, которые входят в элемент списка.
    // При этом RecyclerView создаёт ровно столько контейнеров, сколько нужно для отображения на экране.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView, mTypeView, mStatusView;
        public final ImageView mImageView;
        public final CheckBox mCheck;

        public ViewHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.nameCar);
            mTypeView = view.findViewById(R.id.typeCar);
            mImageView = view.findViewById(R.id.imageCar);
            mStatusView = view.findViewById(R.id.statusCar);
            mCheck = view.findViewById(R.id.checkbox);
        }
    }
}

