package com.android.gjprojection.roveedoll;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.gjprojection.roveedoll.features.free_line.FreeLineFragment;
import com.android.gjprojection.roveedoll.services.bluetooth.BluetoothManager;
import com.android.gjprojection.roveedoll.utils.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothManager.startDiscovery(this);


        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_layout, FreeLineFragment.newInstance());
        ft.commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                BluetoothManager.startDiscovery(this);
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;

        }
    }
}
