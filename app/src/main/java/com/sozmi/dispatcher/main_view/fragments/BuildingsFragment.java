package com.sozmi.dispatcher.main_view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.main_view.adapters.BuildingViewAdapter;
import com.sozmi.dispatcher.model.server.ServerData;

/**
 * A fragment representing a list of Items.
 */
public class BuildingsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new BuildingViewAdapter(ServerData.getBuildings(), view));
        }
        return view;
    }

    @NonNull
    @Override
    public String toString() {
        return "BuildingFragment";
    }
}