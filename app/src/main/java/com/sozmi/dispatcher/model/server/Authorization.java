package com.sozmi.dispatcher.model.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Класс, описывающий процесс авторизации пользователя
 */
public class Authorization {
    private static final String APP_PREFERENCES = "settings";
    private static final String APP_PREFERENCES_EMAIL = "User_email";
    private static final String APP_PREFERENCES_PASSWD = "User_password";
    private static final String host = "82.179.140.18";
    private static final int port = 45555;
    private static SharedPreferences mSettings; //файл настроек


    /**
     * Получение адреса пользователя из сохраненных данных
     * @return адрес пользователя
     */
    public static String getEmail() {
        return mSettings.getString(APP_PREFERENCES_EMAIL, "");
    }

    /**
     * Получение пароля пользователя из сохраненных данных
     * @return пароль пользователя
     */
    public static String getPassword() {
        return mSettings.getString(APP_PREFERENCES_PASSWD, "");
    }


    /**
     * Инициализация файла настроек
     * @param activity текущая активность
     */
    public static void init(Activity activity){
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
    private static void addUserData(String email, String password) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_EMAIL, email);
        editor.putString(APP_PREFERENCES_PASSWD, password);
        editor.apply();
        Log.d("Authorization", "Add email and password in config setting");
    }


    /**
     * Авторизация пользователя
     * @param email почта пользователя
     * @param passwd пароль пользователя
     * @param isSave нужно ли сохранить пароль
     * @return true - если авторизация успешна, иначе false
     * @throws NetworkException ошибка сети
     * @throws DataException может быть типом no_find, если пользователь с такими данными не найден
     */
    public static boolean authorization(String email, String passwd, boolean isSave) throws NetworkException, DataException {
        Connection connection = new Connection(host, port);
        connection.sendData("get_user;" + email + "|" + passwd);
        String s = connection.getData();
        connection.disconnect();
        if (s.equals("no_find")) {
            throw new DataException("Неверный логин или пароль", "no_find");
        }
        if (ServerData.loadUser(s)) {
            if (isSave)
                addUserData(email, passwd);

            return true;
        }
        return false;
    }

    /**
     * Регистрация пользователя
     * @param email почта пользователя
     * @param passwd пароль пользователя
     * @param isSave нужно ли сохранить пароль
     * @return true - если регистрация успешна, иначе false
     * @throws NetworkException ошибка сети
     * @throws DataException может быть типом email, если найден пользователь с такой почтой;
     * может быть типом name, если найден пользователь с таким именем
     */
    public static boolean registration(String email, String passwd, String name, boolean isSave) throws NetworkException, DataException {
        Connection connection = new Connection(host, port);
        connection.sendData("add_user;" + email + "|" + name + "|" + passwd);
        String s = connection.getData();
        connection.disconnect();
        if (s.equals("email_exist"))
            throw new DataException("Email уже зарегистрирован", "email");
        else if (s.equals("name_exist"))
            throw new DataException("Username уже существует", "name");

        if (ServerData.loadUser(s)) {
            if (isSave)
                addUserData(email, passwd);
            return true;
        }
        connection.disconnect();
        return false;
    }


    /**
     * Авторизация пользователя по сохраненным данным
     * @return true - если авторизация успешна, иначе false
     * @throws NetworkException ошибка сети
     * @throws DataException может быть типом no_find, если пользователь с такими данными не найден
     */
    public static boolean authorization() throws NetworkException, DataException {
        if (Authorization.isLoginSaved()) {
            return authorization(Authorization.getEmail(), Authorization.getPassword(), false);
        }
        return false;
    }
}
