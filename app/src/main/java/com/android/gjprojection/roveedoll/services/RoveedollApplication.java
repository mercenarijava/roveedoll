package com.android.gjprojection.roveedoll.services;

import android.app.Application;

public class RoveedollApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothManager.init(getApplicationContext());
    }

}
