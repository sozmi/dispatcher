package com.sozmi.dispatcher.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.model.MyFM;


public class LoginFragment extends Fragment {

    private final TextWatcher tw_email = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (email.isEmpty() && isEmailValid(email)) {
                emailInput.setError("Неправильная почта.");
                isLoginValid = false;
            } else {
                isLoginValid = true;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        private boolean isEmailValid(String email) {
            final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
            return email.matches(EMAIL_REGEX);
        }
    };
    private final TextWatcher tw_password = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (password.isEmpty() && isPasswordValid(password)) {
                passwordInput.setError("Некорректный пароль. В пароле должны быть:\n" +
                        "1)цифры [0-9];\n" +
                        "2)строчные буквы [a-z];\n" +
                        "3)заглавные буквы [A-Z];\n" +
                        "4)спецсимволы [@#$%^&+=].\n");
                isPasswordValid = false;
            } else {
                isPasswordValid = true;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        private boolean isPasswordValid(String password) {
            final String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
            return password.matches(pattern);
        }
    };

    private EditText emailInput, passwordInput;
    private static String email, password;
    private static boolean isPasswordValid = false, isLoginValid = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment
        // This will be the top level handling of theme
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        Button auth = view.findViewById(R.id.login_btn);
        emailInput = view.findViewById(R.id.username);
        passwordInput = view.findViewById(R.id.editText_password);
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        emailInput.addTextChangedListener(tw_email);
        passwordInput.addTextChangedListener(tw_password);

        auth.setOnClickListener(v -> OnAuthClick());
        return view;
    }

    private void OnAuthClick() {
        if (isLoginValid && isPasswordValid) {
            Login(email, password);
        } else {
            Toast toast = Toast.makeText(getContext(),
                    "Неверное имя пользователя или пароль", Toast.LENGTH_SHORT);
            toast.show();
            //TODO: убрать Login()
            Login("xx", "xxx");
        }
    }


    private void Login(String email, String password) {
        //TODO: Написать обработчик сверки с бд и регистрацией, входом
        if (email != null && password != null) {
            MyFM.OpenFragment(new MapFragment());
            FrameLayout topMenu = requireActivity().findViewById(R.id.top_menu);
            LinearLayout bottomMenu = requireActivity().findViewById(R.id.bottom_menu);
            topMenu.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);

        }
    }
    @NonNull
    @Override
    public String toString() {
        return "LoginFragment";
    }
}