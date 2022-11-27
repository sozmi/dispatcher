package com.sozmi.dispatcher.authorization_view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.authorization_view.LoginActivity;


public class AuthorizationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);
        Button login = view.findViewById(R.id.buttonOpenLoginFragment);
        Button register = view.findViewById(R.id.buttonOpenRegisterFragment);
        ImageButton vk = view.findViewById(R.id.vk);
        ImageButton google = view.findViewById(R.id.google);
        ImageButton telegram = view.findViewById(R.id.telegram);
        vk.setOnClickListener(v -> noWork());
        google.setOnClickListener(v -> noWork());
        telegram.setOnClickListener(v -> noWork());
        login.setOnClickListener(v -> onLoginClick());
        register.setOnClickListener(v -> onRegisterClick());
        return view;
    }

    private void noWork() {
        Toast toast = Toast.makeText(getContext(), "Данный функционал в разработке", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void onLoginClick() {
        var a = requireActivity();
        FragmentTransaction ft = a.getSupportFragmentManager().beginTransaction();
        Fragment f = new LoginFragment();

        ft.replace(R.id.fragment_view_auth, f, f.toString());
        ft.addToBackStack(f.getTag());
        ft.commit();
        LoginActivity.lastName = f.getTag();
    }

    private void onRegisterClick() {
        var a = requireActivity();
        FragmentTransaction ft = a.getSupportFragmentManager().beginTransaction();
        Fragment f = new RegistrationFragment();
        ft.replace(R.id.fragment_view_auth, f, f.toString());
        ft.addToBackStack(f.getTag());
        ft.commit();
        LoginActivity.lastName = f.getTag();
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName();
    }
}