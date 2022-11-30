package com.sozmi.dispatcher.main_view.adapters;

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
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.server.DataException;
import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;

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
        holder.mAddButton.setOnClickListener(view1 -> {
            try {
                ServerData.addCar(building, building.getTypeCar());
                var v = holder.mCarView.getAdapter();
                assert v != null;
                v.notifyItemInserted(mValues.size());

            } catch (NetworkException | DataException e) {
                Toast toast = Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT);
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

        public final RecyclerView mCarView;

        public final ImageButton mAddButton;

        public ViewHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.nameBuilding);
            mTypeView = view.findViewById(R.id.typeBuilding);
            mImageView = view.findViewById(R.id.imageBuilding);
            mCarView = view.findViewById(R.id.carBuilding);
            mAddButton = view.findViewById(R.id.buttonAddCar);
        }
    }
}

