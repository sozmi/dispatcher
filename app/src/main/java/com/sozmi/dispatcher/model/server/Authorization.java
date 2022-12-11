package com.sozmi.dispatcher.model.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Authorization {
    public static final String APP_PREFERENCES = "settings";
    private static final String APP_PREFERENCES_EMAIL = "User_email";
    private static final String APP_PREFERENCES_PASSWD = "User_password";

    private static SharedPreferences mSettings; //файл настроек

    public static String getEmail() {
        return mSettings.getString(APP_PREFERENCES_EMAIL, "");
    }

    public static String getPassword() {
        return mSettings.getString(APP_PREFERENCES_PASSWD, "");
    }

    public static void init(Activity activity) {
        mSettings = activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Проверка сохранял ли пользователь данные
     *
     * @return true -если данные сохранены, иначе - false
     */
    public static boolean isLoginSaved() {
        boolean res = mSettings.contains(APP_PREFERENCES_EMAIL) && mSettings.contains(APP_PREFERENCES_PASSWD);
        Log.d("Config", "Login is save in config:" + res);
        return res;
    }

    /**
     * Сохранение данных пользователя в фацле конфигурации в файле конфигурации
     *
     * @param email    почта пользователя
     * @param password пароль пользователя
     */
    public static void addAuthorizationData(String email, String password) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_EMAIL, email);
        editor.putString(APP_PREFERENCES_PASSWD, password);
        editor.apply();
        Log.d("Config", "Add email and password in config setting");
    }
}
