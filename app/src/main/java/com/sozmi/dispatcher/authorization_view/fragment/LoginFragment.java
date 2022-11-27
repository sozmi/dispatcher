package com.sozmi.dispatcher.authorization_view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.main_view.MainActivity;
import com.sozmi.dispatcher.main_view.ui.MyTextWatcher;
import com.sozmi.dispatcher.model.server.DataException;
import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;


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
        isSaveCB= view.findViewById(R.id.isSave);

        tw_email = new MyTextWatcher(emailInput,
                "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
                "Пожалуйста, введите правильную почту");
        tw_password = new MyTextWatcher(passwordInput,
                "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#%^&+=])(?=\\S+$)(?=[^\\;\\|\\']+$).{8,}",
                "Некорректный пароль. Допустимые символы(0-9,a-z,A-Z,!@#%^&+=)");
        auth.setOnClickListener(v -> OnAuthClick());
        return view;
    }

    private void OnAuthClick() {
        auth.setEnabled(false);
        if (tw_email.isValid() && tw_password.isValid()) {

            try {
                if (ServerData.Authorization(tw_email.getText(), tw_password.getText(),isSaveCB.isChecked())) {
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    requireActivity().finish();

                }
            } catch (NetworkException e) {
                Toast toast = Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            } catch (NullPointerException e) {
                Toast toast = Toast.makeText(requireActivity(), "Не установлено соединение с сервером. Пожалуйста, попробуйте позже", Toast.LENGTH_SHORT);
                toast.show();
            } catch (DataException e) {
                Toast toast = Toast.makeText(requireActivity(), e.getMessage() + "", Toast.LENGTH_SHORT);
                toast.show();
            }

        } else if (tw_email.isEmpty()) {
            Toast toast = Toast.makeText(requireActivity(), "Введите e-mail", Toast.LENGTH_SHORT);
            toast.show();
        } else if (tw_password.isEmpty()) {
            Toast toast = Toast.makeText(requireActivity(), "Введите пароль", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(requireActivity(), "Неверное имя пользователя или пароль", Toast.LENGTH_SHORT);
            toast.show();
        }
        auth.setEnabled(true);
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName();
    }
}