package com.sozmi.dispatcher.authorization_view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.main_view.MainActivity;
import com.sozmi.dispatcher.model.server.DataException;
import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;
import com.sozmi.dispatcher.ui.MyTextWatcher;


public class LoginFragment extends Fragment {
    private MyTextWatcher tw_email, tw_password;

    private Button auth;
    private CheckBox isSaveCB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        auth = view.findViewById(R.id.buttonLogin);
        EditText emailInput = view.findViewById(R.id.emailLogin);
        EditText passwordInput = view.findViewById(R.id.passwordLogin);
        isSaveCB = view.findViewById(R.id.isSave);

        tw_email = new MyTextWatcher(emailInput,
                "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
                "Пожалуйста, введите правильную почту");

        tw_password = new MyTextWatcher(passwordInput,
                "(?=[^\\;\\|\\']+$).{10,}",
                "Длина пароля меньше 10 или использованы запрещённые символы: ;'|");
        auth.setOnClickListener(v -> OnAuthClick());
        return view;
    }

    private void OnAuthClick() {
        auth.setEnabled(false);
        if (tw_email.isValid() && tw_password.isValid()) {
            new Thread(() -> {
                try {
                    if (ServerData.Authorization(tw_email.getText(), tw_password.getText(), isSaveCB.isChecked())) {
                        requireActivity().runOnUiThread(() -> {
                            Intent intent = new Intent(requireActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            requireActivity().finish();
                        });

                    }
                } catch (NetworkException | DataException e) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();


        } else if (tw_email.isEmpty()) {
            Toast.makeText(requireActivity(), "Введите email", Toast.LENGTH_SHORT).show();
        } else if (tw_password.isEmpty()) {
            Toast.makeText(requireActivity(), "Введите пароль", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Введите корректные данные", Toast.LENGTH_SHORT).show();
        }
        auth.setEnabled(true);
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName();
    }
}