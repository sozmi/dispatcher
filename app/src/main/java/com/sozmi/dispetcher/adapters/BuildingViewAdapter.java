package com.sozmi.dispetcher.adapters;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sozmi.dispetcher.R;
import com.sozmi.dispetcher.databinding.FragmentItemBinding;
import com.sozmi.dispetcher.model.Building;
import com.sozmi.dispetcher.ui.MyListView;

import java.util.List;
/**
 * получает данные и выдает View для отображения пункта списка.*/
public class BuildingViewAdapter extends RecyclerView.Adapter<BuildingViewAdapter.ViewHolder> {

    @SuppressWarnings("unused")
    private final List<Building> mValues;
    /**
     *
     */
    @SuppressWarnings("unused")
    private final View view;

    public BuildingViewAdapter(List<Building> items,View v) {
        mValues = items;
        this.view=v;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Building build = mValues.get(position);
        holder.mNameView.setText(build.getName());
        holder.mTypeView.setText(build.getType().toString());
        holder.mImageView.setImageResource(build.getImage());
        holder.mCarView.setAdapter(new CarAdapter(view.getContext(), R.layout.item_car,build.getCar()));

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mNameView,mTypeView;

        public final ImageView mImageView;

        public final MyListView mCarView;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mNameView = binding.nameBuilding;
            mTypeView = binding.typeBuilding;
            mImageView = binding.imageBuilding;
            mCarView = binding.carBuilding;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mTypeView.getText() + "'";
        }
    }
}

