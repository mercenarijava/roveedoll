package com.android.gjprojection.roveedoll;

import android.arch.lifecycle.Observer;
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
import android.widget.TextView;

import com.android.gjprojection.roveedoll.services.bluetooth.BluetoothManager;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity implements UIComponent {
    private static final int ANIMATION_DELAY_MILLIS = 1000;
    private static final int ANIMATION_ROTATION_MILLIS = 700;
    private static final int VERTICAL_TRANSLATION_MISURE = 220;
    private static final int TIMER_DELAY_MILLIS = 1000;

    @NonNull
    ImageView logoImageView;
    @NonNull
    LinearLayout titleLayout;
    @NonNull
    FrameLayout contentLayout;
    @NonNull
    TextView counterTextView;
    @NonNull
    LinearLayout notConnectedLayout;
    @NonNull
    LinearLayout connectedLayout;
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
        this.counterTextView = findViewById(R.id.counter_textview);
        this.notConnectedLayout = findViewById(R.id.not_connected_layout);
        this.connectedLayout = findViewById(R.id.connected_layout);
    }

    @Override
    public void connectListeners() {
        this.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                configureTimer();
                configureLoader();
                configureBluetoothConnection();
            }
        }, ANIMATION_DELAY_MILLIS);
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
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        coroutine();
                    }
                });
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

    private void configureBluetoothConnection() {
        BluetoothManager.get()
                .getDeviceConnection()
                .observe(
                        SplashActivity.this,
                        new Observer<Boolean>() {
                            @Override
                            public void onChanged(@Nullable Boolean isConnected) {
                                if (isConnected == null || !isConnected) return;
                                completeConnection();
                            }
                        }
                );
    }

    private void completeConnection() {
        this.notConnectedLayout.animate().alpha(0);
        this.connectedLayout.animate().alpha(1);
        this.connectedLayout.animate().scaleX(1.2f);
        this.connectedLayout.animate().scaleY(1.2f);
        this.connectedLayout.animate().withEndAction(new Runnable() {
            @Override
            public void run() {
                connectedLayout.animate().scaleX(1f);
                connectedLayout.animate().scaleY(1f);
            }
        });
    }

    private void coroutine() {
        counterTextView.setText(String.valueOf(timeFromStartSeconds));
        if (timeFromStartSeconds % 5 == 0) {
            if ((timeFromStartSeconds / 5) % 2 == 0) {
                logoImageView.animate().rotationYBy(-360).setDuration(ANIMATION_ROTATION_MILLIS);
            } else {
                logoImageView.animate().rotationXBy(360).setDuration(ANIMATION_ROTATION_MILLIS);
            }
        }
        timeFromStartSeconds++;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mainHandler.removeCallbacksAndMessages(null);
        this.timer.cancel();
    }
}
