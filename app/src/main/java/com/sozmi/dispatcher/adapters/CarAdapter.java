package com.sozmi.dispatcher.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.objects.Car;

import java.util.ArrayList;

public class CarAdapter extends ArrayAdapter<Car> {
    private final LayoutInflater inflater;
    private final int layout;
    private final ArrayList<Car> carArrayList;

    public CarAdapter(Context context, int resource, ArrayList<Car> cars) {
        super(context, resource, cars);
        this.carArrayList = cars;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Car car = carArrayList.get(position);

        viewHolder.mNameView.setText(car.getName());
        viewHolder.mTypeView.setText(car.getType().toString());
        viewHolder.mImageView.setImageResource(car.getImage());
        viewHolder.mStatusView.setText(car.getStatusToString());
        viewHolder.mStatusView.setBackgroundColor(car.getColor(getContext()));

        return convertView;
    }

    private static class ViewHolder {
        public final TextView mNameView,mTypeView,mStatusView;
        public final ImageView mImageView;

        @SuppressWarnings("unused")
        public ViewHolder(View view){
            mNameView = view.findViewById(R.id.nameCar);
            mTypeView = view.findViewById(R.id.typeCar);
            mImageView = view.findViewById(R.id.imageCar);
            mStatusView =view.findViewById(R.id.statusCar);
        }
    }
}