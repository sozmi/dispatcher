package com.sozmi.dispatcher.main_view.fragments;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.objects.TypeBuilding;
import com.sozmi.dispatcher.model.server.DataException;
import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Tag;
import com.sozmi.dispatcher.ui.MyTextWatcher;

import org.osmdroid.util.GeoPoint;

public class BuildFragment extends Fragment {
    private TypeBuilding typeBuilding;
    private EditText nameInput;
    private EditText cost;
    private GeoPoint point;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build, container, false);
        ImageButton changeCoordinate = view.findViewById(R.id.buttonChange);
        Button build = view.findViewById(R.id.buttonBuildOnFragment);
        EditText pointBuild = view.findViewById(R.id.coordinateOut);
        nameInput = view.findViewById(R.id.nameBuildingOnFragment);
        MyTextWatcher tw_name = new MyTextWatcher(nameInput, "^[\\sа-яА-ЯёЁa-zA-Z0-9]+$", "Введите название здания без специальных символов");
        cost = view.findViewById(R.id.costBuilding);

        Bundle bundle = getArguments();
        if (bundle != null) {
            point = bundle.getParcelable(Tag.point.toString());
            pointBuild.setText(point.toString().replace(",", ",\n"));
        }

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
                nameInput.setText(typeBuilding.toString());
                String txt = typeBuilding.toCost() + " руб";
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
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tag.point.toString(), point);
        bundle.putBoolean(Tag.viewPanel.toString(), true);
        MyFM.OpenFragment(new MapFragment(), bundle);
    }


    private void onButtonBuildClick() {
        try {
            ServerData.addBuild(nameInput.getText().toString(), point, typeBuilding);

        } catch (NetworkException | DataException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tag.point.toString(), point);
        MyFM.OpenFragment(new MapFragment(), bundle);
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName();
    }
}