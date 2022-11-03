package com.sozmi.dispatcher.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.objects.Car;
import com.sozmi.dispatcher.model.objects.CarCheck;

import java.util.ArrayList;

public class CarCheckAdapter extends ArrayAdapter<CarCheck> {
    private final LayoutInflater inflater;
    private final int layout;
    private final ArrayList<CarCheck> carArrayList;

    @SuppressWarnings("unused")
    public CarCheckAdapter(Context context, int resource, ArrayList<CarCheck> cars) {
        super(context, resource, cars);
        this.carArrayList = cars;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public ArrayList<CarCheck> getItems() {
        return carArrayList;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CarCheck check =carArrayList.get(position);
        final Car car = check.getCar();

        holder.mNameView.setText(car.getName());
        holder.mTypeView.setText(car.getType().toString());
        holder.mImageView.setImageResource(car.getImage());
        holder.mStatusView.setText(car.getStatusToString());
        holder.mStatusView.setBackgroundColor(ContextCompat.getColor(convertView.getContext(), car.getColor()));
        holder.mCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            check.setCheck(isChecked);
            notifyDataSetChanged();
        });
        holder.mCheck.setChecked(check.getCheck());
        return convertView;
    }

    private static class ViewHolder {
        public final TextView mNameView, mTypeView, mStatusView;
        public final ImageView mImageView;
        public final CheckBox mCheck;

        @SuppressWarnings("unused")
        public ViewHolder(View view) {
            mNameView = view.findViewById(R.id.nameCar);
            mTypeView = view.findViewById(R.id.typeCar);
            mImageView = view.findViewById(R.id.imageCar);
            mStatusView = view.findViewById(R.id.statusCar);
            mCheck = view.findViewById(R.id.checkbox);
        }


    }
}