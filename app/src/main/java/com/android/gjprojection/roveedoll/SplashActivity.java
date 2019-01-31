package com.android.gjprojection.roveedoll;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity implements UIComponent {
    private static final int ANIMATION_DELAY_MILLIS = 1000;
    private static final int ANIMATION_ROTATION_MILLIS = 700;
    private static final int VERTICAL_TRANSLATION_MISURE = 180;
    private static final int TIMER_DELAY_MILLIS = 1000;
    private static final int APP_INIT_DELAY_MILLIS = 4000;
    private static final int COROUTINE_DELAY_SECONDS = 3;


    @NonNull
    ImageView logoImageView;
    @NonNull
    LinearLayout titleLayout;
    @NonNull
    FrameLayout contentLayout;
    @NonNull
    Handler mainHandler = new Handler(Looper.getMainLooper());
    @NonNull
    Timer timer = new Timer();

    int timeFromStartSeconds = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        connectViews();
        connectListeners();
        setActionBar();
    }

    @Override
    public void connectViews(@Nullable View... views) {
        this.logoImageView = findViewById(R.id.logo);
        this.titleLayout = findViewById(R.id.title_layout);
        this.contentLayout = findViewById(R.id.content_layout);
    }

    @Override
    public void connectListeners() {
        this.mainHandler.postDelayed(() -> {
            configureTimer();
            configureLoader();
            mainHandler.postDelayed(this::startMainActivity, APP_INIT_DELAY_MILLIS);
        }, ANIMATION_DELAY_MILLIS);

    }

    private void startMainActivity() {
        @NonNull final Intent a = new Intent(this, MainActivity.class);
        startActivity(a);
        finish();
    }

    public void setActionBar() {
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.default_actionbar_layout);
    }

    private void configureTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mainHandler.post(() -> coroutine());
            }
        }, TIMER_DELAY_MILLIS, TIMER_DELAY_MILLIS);
    }

    private void configureLoader() {
        this.titleLayout.animate().translationY(-1 * VERTICAL_TRANSLATION_MISURE);
        this.contentLayout.animate().translationY(VERTICAL_TRANSLATION_MISURE);
        this.contentLayout.animate().alpha(1);
        this.contentLayout.animate().translationZ(20);
        this.logoImageView.animate().rotationY(-360).setDuration(ANIMATION_ROTATION_MILLIS);
    }

    private void coroutine() {
        if (timeFromStartSeconds % COROUTINE_DELAY_SECONDS == 0) {
            if ((timeFromStartSeconds / COROUTINE_DELAY_SECONDS) % 2 == 0) {
                logoImageView.animate().rotationYBy(-360).setDuration(ANIMATION_ROTATION_MILLIS);
            } else {
                logoImageView.animate().rotationXBy(360).setDuration(ANIMATION_ROTATION_MILLIS);
            }
        }
        timeFromStartSeconds++;
    }

    @Override
    protected void onDestroy() {
        this.mainHandler.removeCallbacksAndMessages(null);
        this.timer.cancel();
        super.onDestroy();
    }
}
