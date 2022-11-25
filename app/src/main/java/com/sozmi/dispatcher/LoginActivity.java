package com.sozmi.dispatcher;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

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
    private static String email = "", password = "";
    private static boolean isPasswordValid = false, isLoginValid = false;
    Button auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ServerData.loadSettings(this);
            try {
                if (ServerData.isLoginSaved() && ServerData.AuthorizationSave()) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    return;
                }
            } catch (IOException e) {
                Toast toast = Toast.makeText(this, "Не удалось прочитать полученные данные. Пожалуйста попробуйте позже.", Toast.LENGTH_SHORT);
                toast.show();
            } catch (InterruptedException e) {
                Toast toast = Toast.makeText(this, "Произошла ошибка потока. Пожалуйста попробуйте позже.", Toast.LENGTH_SHORT);
                toast.show();
            }catch (NetworkException e){
                Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
       auth = findViewById(R.id.login_btn);
        emailInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.editText_password);


        emailInput.addTextChangedListener(tw_email);
        passwordInput.addTextChangedListener(tw_password);
        auth.setOnClickListener(v -> OnAuthClick());
    }

    private void OnAuthClick() {
        auth.setEnabled(false);
        if (isLoginValid && isPasswordValid) {
            email = emailInput.getText().toString();
            password = passwordInput.getText().toString();
            try {
                if (ServerData.Authorization(email, password)) {
                    ServerData.addEmail(email);
                    ServerData.addPasswd(password);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                }
            } catch (IOException e) {
                Toast toast = Toast.makeText(this, "Не удалось прочитать полученные данные. Пожалуйста попробуйте позже.", Toast.LENGTH_SHORT);
                toast.show();
            } catch (InterruptedException e) {
                Toast toast = Toast.makeText(this, "Произошла ошибка потока. Пожалуйста, попробуйте позже.", Toast.LENGTH_SHORT);
                toast.show();
            }catch (NetworkException e){
                Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }catch (NullPointerException e){
                Toast toast = Toast.makeText(this,"Не установлено соединение с сервером. Пожалуйста, попробуйте позже", Toast.LENGTH_SHORT);
                toast.show();
            }

        }else if(emailInput.getText().toString().isEmpty()){
            Toast toast = Toast.makeText(this, "Введите e-mail", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(passwordInput.getText().toString().isEmpty()){
            Toast toast = Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT);
            toast.show();
        }
            else {
            Toast toast = Toast.makeText(this, "Неверное имя пользователя или пароль", Toast.LENGTH_SHORT);
            toast.show();

        }
        auth.setEnabled(true);
    }
}