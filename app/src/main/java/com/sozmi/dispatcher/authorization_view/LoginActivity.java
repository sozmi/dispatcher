package com.sozmi.dispatcher.authorization_view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.authorization_view.fragment.AuthorizationFragment;
import com.sozmi.dispatcher.main_view.MainActivity;
import com.sozmi.dispatcher.model.server.Authorization;
import com.sozmi.dispatcher.model.server.DataException;
import com.sozmi.dispatcher.model.server.NetworkException;
import com.sozmi.dispatcher.model.server.ServerData;

public class LoginActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_login);
        Authorization.init(this);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            checkVersion();
            swipeRefreshLayout.setRefreshing(false);
        });
        checkVersion();
    }


    private void checkVersion() {
        new Thread(() -> {
            try {
                ServerData.loadLastVersion(this);
            } catch (NetworkException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    ProgressBar pb = findViewById(R.id.progressLoadLogin);
                    pb.setVisibility(View.GONE);
                });
                return;
            }
            runOnUiThread(() -> {
                SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
                swipeRefreshLayout.setOnRefreshListener(() -> {
                    authorization();
                    swipeRefreshLayout.setRefreshing(false);
                });
            });
            authorization();
        }).start();
    }

    private void authorization() {
        try {
            if (Authorization.authorization()) {
                openMainActivity();
            } else {
                runOnUiThread(() -> {
                    ProgressBar pb = findViewById(R.id.progressLoadLogin);
                    pb.setVisibility(View.GONE);
                    openAuthFragment();
                });
            }
        } catch (NetworkException | DataException e) {
            runOnUiThread(() -> {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                ProgressBar pb = findViewById(R.id.progressLoadLogin);
                pb.setVisibility(View.GONE);
            });
        }

    }

    private void openAuthFragment() {
        runOnUiThread(() -> {
            SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                swipeRefreshLayout.setRefreshing(false);
            });
        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = new AuthorizationFragment();
        ft.replace(R.id.fragment_view_auth, f, f.toString());
        ft.commit();
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}