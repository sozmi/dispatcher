package com.sozmi.dispetcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private final TextWatcher tw_email = new TextWatcher() {
        public void afterTextChanged(Editable s){
            if(email.isEmpty() && isEmailValid(email)) {
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
    private final TextWatcher tw_password = new TextWatcher() {
        public void afterTextChanged(Editable s){
            if(password.isEmpty() && isPasswordValid(password)){
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

    private  EditText emailInput,passwordInput;
    private static String email,password;
    private static boolean isPasswordValid=false,isLoginValid=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // This will be the top level handling of theme
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button auth = findViewById(R.id.login_btn);
        emailInput =findViewById(R.id.username);
        passwordInput =findViewById(R.id.editText_password);
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        emailInput.addTextChangedListener(tw_email);
        passwordInput.addTextChangedListener(tw_password);

        auth.setOnClickListener(view -> OnAuthClick());
    }
   private void OnAuthClick() {
        if(isLoginValid && isPasswordValid){
            Login(email,password);
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Неверное имя пользователя или пароль", Toast.LENGTH_SHORT);
            toast.show();
            //TODO: убрать Login()
            Login("xx","xxx");
        }
    }


    private void Login(String email, String password){
        //TODO: Написать обработчик сверки с бд и регистрацией, входом
        if(email!=null &&password!=null){
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main);
        }
    }
}