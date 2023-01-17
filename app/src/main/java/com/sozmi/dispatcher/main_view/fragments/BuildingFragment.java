package com.sozmi.dispatcher.main_view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.main_view.adapters.CarViewAdapter;
import com.sozmi.dispatcher.model.objects.Building;
import com.sozmi.dispatcher.model.server.DataException;
import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Tag;


public class BuildingFragment extends Fragment {

    private RecyclerView mCarView;

    private Building building;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_building, container, false);
        final TextView mNameView = view.findViewById(R.id.nameBuilding);
        final TextView mTypeView = view.findViewById(R.id.typeBuilding);
        final Button backBtn = view.findViewById(R.id.buttonBackToListBuilding);
        final Button buyCarBtn = view.findViewById(R.id.buttonBuyCar);
        final ImageView mImageView = view.findViewById(R.id.imageBuilding);
        mCarView = view.findViewById(R.id.list_cars_building);


        Bundle bundle = getArguments();
        if (bundle != null) {
            building = (Building) bundle.getSerializable(Tag.building.toString());

            mNameView.setText(building.getName());
            mTypeView.setText(building.getType().toString());
            mImageView.setImageResource(building.getImage());
            CarViewAdapter childItemAdapter = new CarViewAdapter(building.getCars(), view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mCarView.getContext(), LinearLayoutManager.VERTICAL, false);
            layoutManager.setInitialPrefetchItemCount(building.getCars().size());
            mCarView.setLayoutManager(layoutManager);
            mCarView.setAdapter(childItemAdapter);
        }
        backBtn.setOnClickListener(v -> OnClickBackButton());
        buyCarBtn.setOnClickListener(v -> buyCarButton_onClick());
        return view;
    }

    private void OnClickBackButton() {
        MyFM.OpenFragment(new ListBuildingsFragment(), null);
    }

    private void buyCarButton_onClick() {

        try {
            ServerData.addCar(building,building.getTypeCar());
        } catch (NetworkException e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (DataException e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        mCarView.getAdapter().notifyDataSetChanged();
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName();
    }
}