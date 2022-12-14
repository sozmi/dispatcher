package com.sozmi.dispatcher.main_view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.R;
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_car, parent, false);
        return new CarViewAdapter.ViewHolder(view);
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

        public ViewHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.nameCar);
            mTypeView = view.findViewById(R.id.typeCar);
            mImageView = view.findViewById(R.id.imageCar);
            mStatusView = view.findViewById(R.id.statusCar);
        }
    }
}

