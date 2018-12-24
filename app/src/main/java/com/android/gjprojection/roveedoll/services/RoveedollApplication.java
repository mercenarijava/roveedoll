package com.android.gjprojection.roveedoll.services;

import android.app.Application;
import android.support.annotation.NonNull;

import com.android.gjprojection.roveedoll.services.bluetooth.BluetoothManager;

public class RoveedollApplication extends Application {

    public static Application application;

    static void init(
            @NonNull RoveedollApplication app) {
        application = app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        RoveedollApplication.init(this);
        BluetoothManager.init(getBaseContext());
    }

    @Override
    public void onTerminate() {
        BluetoothManager.end(getBaseContext());
        super.onTerminate();
    }
}
