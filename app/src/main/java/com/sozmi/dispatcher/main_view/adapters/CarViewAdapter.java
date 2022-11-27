package com.sozmi.dispatcher.main_view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.databinding.FragmentItemCarBinding;
import com.sozmi.dispatcher.model.objects.Car;

import java.util.List;

/**
 * получает данные и выдает View для отображения пункта списка.
 */
public class CarViewAdapter extends RecyclerView.Adapter<CarViewAdapter.ViewHolder> {

    private final List<Car> mValues;

    private final View view;

    public CarViewAdapter(List<Car> items, View v) {
        mValues = items;
        this.view = v;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemCarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    //В методе адаптера onBindViewHolder() связываем используемые текстовые метки с данными
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Car car = mValues.get(position);
        holder.mNameView.setText(car.getName());
        holder.mTypeView.setText(car.getType().toString());
        holder.mImageView.setImageResource(car.getImage());
        holder.mStatusView.setText(car.getStatusToString());
        holder.mStatusView.setBackgroundColor(car.getColor(view.getContext()));
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

        public ViewHolder(FragmentItemCarBinding binding) {
            super(binding.getRoot());
            mNameView = binding.nameCar;
            mTypeView = binding.typeCar;
            mImageView = binding.imageCar;
            mStatusView = binding.statusCar;
        }
    }
}

