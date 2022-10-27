package com.sozmi.dispatcher.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.databinding.FragmentItemBuildingBinding;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.Server;
import com.sozmi.dispatcher.ui.MyListView;

import java.util.List;

/**
 * получает данные и выдает View для отображения пункта списка.
 */
public class BuildingViewAdapter extends RecyclerView.Adapter<BuildingViewAdapter.ViewHolder> {

    private final List<Building> mValues;

    private final View view;

    public BuildingViewAdapter(List<Building> items, View v) {
        mValues = items;
        this.view = v;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBuildingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    //В методе адаптера onBindViewHolder() связываем используемые текстовые метки с данными
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Building building = mValues.get(position);
        holder.mNameView.setText(building.getName());
        holder.mTypeView.setText(building.getType().toString());
        holder.mImageView.setImageResource(building.getImage());
        holder.mCarView.setAdapter(new CarAdapter(view.getContext(), R.layout.fragment_item_car, building.getCars(), building));
        holder.mAddButton.setOnClickListener(view1 -> {
            if(Server.addCar(building, building.getTypeCar())){
                ((BaseAdapter)  holder.mCarView.getAdapter()).notifyDataSetChanged();
            }
            else {
                Toast toast = Toast.makeText(view.getContext(),
                        "Недостаточно средств", Toast.LENGTH_SHORT);
                toast.show();
            }

        });
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

        public final TextView mNameView, mTypeView;

        public final ImageView mImageView;

        public final MyListView mCarView;

        public final ImageButton mAddButton;

        public ViewHolder(FragmentItemBuildingBinding binding) {
            super(binding.getRoot());
            mNameView = binding.nameBuilding;
            mTypeView = binding.typeBuilding;
            mImageView = binding.imageBuilding;
            mCarView = binding.carBuilding;
            mAddButton = binding.buttonAddCar;
        }
    }
}

