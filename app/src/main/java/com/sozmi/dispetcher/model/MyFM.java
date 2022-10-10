package com.sozmi.dispetcher.model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.sozmi.dispetcher.R;

public class MyFM {
    private static String nameCurrent;
    private static FragmentManager fm;
    /**
     * Метод открывающий заменяющий текущий фрагмент нужным
     * @param fragment - фрагмент который будет отображаться
     */
    public static void OpenFragment(Fragment fragment,String name){
        setCurrentName(name);
        FragmentTransaction ft = getFM().beginTransaction();
        ft.replace(R.id.fragment_view, fragment,name);
        ft.commit();
    }

    public static String getCurrentName() {
        return nameCurrent;
    }

    public static void setCurrentName(String _currentFragment) {
        MyFM.nameCurrent = _currentFragment;
    }

    public static FragmentManager getFM() {
        return fm;
    }

    public static void setFM(FragmentManager fm) {
        MyFM.fm = fm;
    }
}
