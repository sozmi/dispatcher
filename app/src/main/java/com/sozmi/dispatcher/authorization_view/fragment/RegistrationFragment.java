package com.sozmi.dispatcher.authorization_view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.Objects;


public class RegistrationFragment extends Fragment {

    private Button register;
    private MyTextWatcher tw_email, tw_password, tw_name;
    private EditText emailInput, passwordInput, nameInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        register = view.findViewById(R.id.buttonRegister);
        nameInput = view.findViewById(R.id.nameRegister);
        emailInput = view.findViewById(R.id.emailRegister);
        passwordInput = view.findViewById(R.id.passwordRegister);


        tw_email = new MyTextWatcher(emailInput, "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", "Пожалуйста, введите правильную почту");
        tw_password = new MyTextWatcher(passwordInput,
                "(?=[^\\;\\|\\']+$).{10,}",
                "Длина пароля меньше 10 или использованы запрещённые символы: ;'|");
        tw_name = new MyTextWatcher(nameInput, "^[а-яА-ЯёЁa-zA-Z0-9]+$", "Введите логин без специальных символов");
        register.setOnClickListener(v -> onRegisterButtonClick());
        return view;
    }

    private void onRegisterButtonClick() {
        register.setEnabled(false);
        if (tw_email.isValid() && tw_password.isValid() && tw_name.isValid()) {
            new Thread(() -> {
                try {
                    if (ServerData.Registration(tw_email.getText(), tw_password.getText(), tw_name.getText())) {
                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                } catch (NetworkException e) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

                } catch (DataException e) {
                    if (Objects.equals(e.getType(), "passwd")) {
                        nameInput.setError(e.getMessage());
                    } else if (Objects.equals(e.getType(), "email")) {
                        emailInput.setError(e.getMessage());
                    }
                }
            }).start();

        } else if (tw_name.isEmpty()) {
            Toast.makeText(requireActivity(), "Введите логин", Toast.LENGTH_SHORT).show();
        } else if (tw_email.isEmpty()) {
            Toast.makeText(requireActivity(), "Введите e-mail", Toast.LENGTH_SHORT).show();
        } else if (tw_password.isEmpty()) {
            Toast.makeText(requireActivity(), "Введите пароль", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Введите корректные данные", Toast.LENGTH_SHORT).show();
        }
        register.setEnabled(true);
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName();
    }

}