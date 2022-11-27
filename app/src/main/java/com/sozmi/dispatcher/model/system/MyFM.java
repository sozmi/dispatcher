package com.sozmi.dispatcher.model.system;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sozmi.dispatcher.R;

public class MyFM {
    private static String nameCurrent;
    private static FragmentManager fm;

    /**
     * Метод открывающий заменяющий текущий фрагмент нужным
     *
     * @param fragment - фрагмент который будет отображаться
     */
    public static void OpenFragment(Fragment fragment, Bundle bundle) {
        String name = fragment.toString();
        if (name.equals(nameCurrent)) return;
        setCurrentName(name);

        FragmentTransaction ft = getFM().beginTransaction();
        fragment.setArguments(bundle);
        ft.replace(R.id.fragment_view, fragment, name);
        ft.commit();
    }

    public static String getCurrentName() {
        return nameCurrent;
    }

    public static void setCurrentName(String currentFragment) {
        MyFM.nameCurrent = currentFragment;
    }

    public static FragmentManager getFM() {
        return fm;
    }

    public static void setFM(FragmentManager fm) {
        MyFM.fm = fm;
    }
    public static void clear(){
        fm=null;
        nameCurrent=null;
    }
}
