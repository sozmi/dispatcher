package com.sozmi.dispetcher;

import static com.yandex.runtime.Runtime.getApplicationContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private TextWatcher tw_email = new TextWatcher() {
        public void afterTextChanged(Editable s){
            String email = emailInput.getText().toString();
            if(email.isEmpty()==false && isEmailValid(email)==false) {
                emailInput.setError("Неправильная почта.");
                isLoginValid=false;
            }
            else {
                isLoginValid=true;
            }

        }
        public void  beforeTextChanged(CharSequence s, int start, int count, int after){}
        public void  onTextChanged (CharSequence s, int start, int before,int count) {}
        private boolean isEmailValid(String email) {
            final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
            return email.matches(EMAIL_REGEX);
        }
    };
    private TextWatcher tw_password = new TextWatcher() {
        public void afterTextChanged(Editable s){
            String password = passwordInput.getText().toString();

            if(password.isEmpty()==false && isPasswordValid(password)==false){
                passwordInput.setError("Некорректный пароль. В пароле должны быть:\n" +
                        "1)цифры [0-9];\n" +
                        "2)строчные буквы [a-z];\n" +
                        "3)заглавные буквы [A-Z];\n" +
                        "4)спецсимволы [@#$%^&+=].\n");
                isPasswordValid=false;
            }
            else {
                isPasswordValid=true;
            }
        }

        public void  beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void  onTextChanged (CharSequence s, int start, int before,int count) {}

        private boolean isPasswordValid(String password) {
            final String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
            return password.matches(pattern);
        }
    };

    private Button loginButton;
    private  EditText emailInput,passwordInput;
    private static boolean isPasswordValid=false,isLoginValid=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.login_btn);
        emailInput =(EditText)findViewById(R.id.username);
        passwordInput =(EditText)findViewById(R.id.editText_password);
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        loginButton.setOnClickListener(view -> {
            if(isLoginValid && isPasswordValid){
                Login(email,password);
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Неверное имя пользователя или пароль", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        emailInput.addTextChangedListener(tw_email);
        passwordInput.addTextChangedListener(tw_password);
// Java
    }


    private void Login(String email, String password){
        //TODO: Написать обработчик сверки с бд и регистрацией, входом
        Intent main = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(main);
    }
}