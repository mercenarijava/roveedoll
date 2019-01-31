package com.android.gjprojection.roveedoll.services;

import android.app.Application;
import android.support.annotation.NonNull;

import com.android.gjprojection.roveedoll.services.bluetooth.BluetoothManager;

public class RoveedollApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothManager.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        BluetoothManager.end(getBaseContext());
        super.onTerminate();
    }
}
