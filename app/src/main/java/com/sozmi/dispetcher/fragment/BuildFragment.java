package com.sozmi.dispetcher.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.sozmi.dispetcher.R;
import com.sozmi.dispetcher.model.MyFM;
import com.yandex.mapkit.geometry.Point;

public class BuildFragment extends Fragment {
    Point point;
    public BuildFragment(){

    }
    public BuildFragment(Point point){
        this.point=point;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build, container, false);
        Button changeCoordinate = view.findViewById(R.id.buttonChange);
        changeCoordinate.setOnClickListener(v->onButtonChangeClick());
        CreateSpinner(view);
        return view;
    }

    private void CreateSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.spinner_build);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.building_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    private void onButtonChangeClick(){
        MyFM.OpenFragment(new MapFragment(true,false),"MyMap");
    }
}