package com.sozmi.dispatcher.authorization_view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.authorization_view.fragment.AuthorizationFragment;
import com.sozmi.dispatcher.main_view.MainActivity;
import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;

public class LoginActivity extends AppCompatActivity {
    public static String lastName = "";
    @Override
    public void onBackPressed() {
        FragmentManager sm = getSupportFragmentManager();
        if (sm.getBackStackEntryCount() == 0) {
            finishAndRemoveTask();
        } else {
            sm.popBackStack();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (ServerData.authorization(this)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        } catch (NetworkException e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = new AuthorizationFragment();
        ft.replace(R.id.fragment_view_auth, f, f.toString());
        ft.commit();
        lastName = f.getTag();

        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }



}