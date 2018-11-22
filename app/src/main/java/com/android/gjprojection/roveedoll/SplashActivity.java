package com.android.gjprojection.roveedoll;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class SplashActivity extends AppCompatActivity implements UIComponent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        connectViews();
        connectListeners();
        setActionBar();
    }

    private void configureLoader(){
    }

    @Override
    public void connectViews(@Nullable View... views) {
    }

    @Override
    public void connectListeners() {
        configureLoader();
    }

    @Override
    public void setActionBar() {
        if(getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.default_actionbar_layout);
    }
}
