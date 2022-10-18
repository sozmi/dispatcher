package com.sozmi.dispatcher.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sozmi.dispatcher.R;

/**Класс для получения разрешений*/
public class Permission {

    /** Массив разрешений, необходимых для работы приложения*/
    private final static String[] PERMISSION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    /**
     * Получает разрешения у пользователя, если они ещё не получены
     * @param context контекст
     */
    public static void get(Context context) {
        if (!checkAll(context)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSION, 0);
            if (checkAll(context)) return;
            showDialog(context);
        }
    }

    /**
     * Проверка даны ли все разрешения
     * @param context контекст приложения
     * @return true, если даны; false, не даны
     */
    private static boolean checkAll(Context context) {
        for (String permission : PERMISSION)
            //если разрешения нет возвращаем false
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }


    /**
     * Показывает диалог получения разрешений
     * @param context контекст приложения
     */
    private static void showDialog(Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Получение разрешений");
        alertBuilder.setMessage("Для корректной работы программы и повышения комфорта вашей игры, " +
                "прошу предоставить приложению разрешение на определение местоположения.");
        alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions((Activity) context, PERMISSION, 0));
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}
