package com.sozmi.dispatcher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.Map;
import com.sozmi.dispatcher.model.MyFM;

import org.osmdroid.util.GeoPoint;

public class BuildFragment extends Fragment {
    private final GeoPoint point;
    public BuildFragment(){
        this.point = Map.getCamPoint();
    }
    public BuildFragment(GeoPoint point){
        this.point=point;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build, container, false);
        ImageButton changeCoordinate = view.findViewById(R.id.buttonChange);
        EditText pointBuild = view.findViewById(R.id.coordinateOut);
        pointBuild.setText(point.toString().replace(",",",\n"));
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