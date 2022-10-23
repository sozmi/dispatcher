package com.sozmi.dispatcher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.navigation.Map;
import com.sozmi.dispatcher.model.MyFM;
import com.sozmi.dispatcher.model.Server;
import com.sozmi.dispatcher.model.objects.TypeBuilding;

import org.osmdroid.util.GeoPoint;

public class BuildFragment extends Fragment {
    private final GeoPoint point;
    private TypeBuilding typeBuilding;
    private EditText name;
    private EditText cost;


    public BuildFragment(GeoPoint point) {
        this.point = point;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build, container, false);
        ImageButton changeCoordinate = view.findViewById(R.id.buttonChange);
        Button build = view.findViewById(R.id.buttonBuildOnFragment);
        EditText pointBuild = view.findViewById(R.id.coordinateOut);
        name = view.findViewById(R.id.nameBuildingOnFragment);
        cost = view.findViewById(R.id.costBuilding);

        pointBuild.setText(point.toString().replace(",", ",\n"));
        changeCoordinate.setOnClickListener(v -> onButtonChangeClick());
        build.setOnClickListener(v -> onButtonBuildClick());
        CreateSpinner(view);
        return view;
    }

    private void CreateSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.spinner_build);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<TypeBuilding> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, TypeBuilding.values());
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                typeBuilding = TypeBuilding.values()[position];
                name.setText(typeBuilding.toString());
                String txt =typeBuilding.toCost()+" руб";
                cost.setText(txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    private void onButtonChangeClick() {
        Map.tempPoint = point;
        MyFM.OpenFragment(new MapFragment(true), "MyMap");
    }


    private void onButtonBuildClick() {
        if (Server.addBuild(name.getText().toString(), point, typeBuilding)) {
            MyFM.OpenFragment(new MapFragment(false), "MyMap");
        } else {
            Toast toast = Toast.makeText(getContext(),
                    "Недостаточно средств", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}