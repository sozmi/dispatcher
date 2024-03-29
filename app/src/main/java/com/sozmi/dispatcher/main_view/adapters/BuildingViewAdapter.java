package com.sozmi.dispatcher.main_view.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.main_view.fragments.BuildFragment;
import com.sozmi.dispatcher.main_view.fragments.BuildingFragment;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.server.DataException;
import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Tag;

import java.util.List;

/**
 * получает данные и выдает View для отображения пункта списка.
 */
public class BuildingViewAdapter extends RecyclerView.Adapter<BuildingViewAdapter.ViewHolder> {
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private final List<Building> mValues;

    private final View view;

    public BuildingViewAdapter(List<Building> items, View v) {
        mValues = items;
        this.view = v;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_building, parent, false);
        return new ViewHolder(view);
    }

    //В методе адаптера onBindViewHolder() связываем используемые текстовые метки с данными
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Building building = mValues.get(position);
        holder.mNameView.setText(building.getName());
        holder.mTypeView.setText(building.getType().toString());
        holder.mImageView.setImageResource(building.getImage());
        CarViewAdapter childItemAdapter = new CarViewAdapter(building.getCars(),view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.mCarView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(building.getCars().size());
        holder.mCarView.setLayoutManager(layoutManager);
        holder.mCarView.setAdapter(childItemAdapter);
        holder.mCarView.setRecycledViewPool(viewPool);
        holder.mShowButton.setOnClickListener(view1 -> {
            if(holder.isShow){
                holder.isShow=false;
                holder.mShowButton.setImageResource(R.drawable.ic_arrow_down);
                holder.mCarView.setVisibility(View.GONE);
            }
            else{
                holder.isShow=true;
                holder.mShowButton.setImageResource(R.drawable.ic_arrow_up);
                holder.mCarView.setVisibility(View.VISIBLE);
            }
        });
        holder.mImageView.setOnClickListener(view1 -> showBuildingFragment(building));
        holder.mNameView.setOnClickListener(view1 -> showBuildingFragment(building));
    }

    private void showBuildingFragment(Building building){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Tag.building.toString(), building);
        MyFM.OpenFragment(new BuildingFragment(), bundle);
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

        public final RecyclerView mCarView;

        public final ImageButton mShowButton;

        public boolean isShow=false;

        public ViewHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.nameBuilding);
            mTypeView = view.findViewById(R.id.typeBuilding);
            mImageView = view.findViewById(R.id.imageBuilding);
            mCarView = view.findViewById(R.id.carBuilding);
            mShowButton = view.findViewById(R.id.buttonShow);
        }
    }
}

