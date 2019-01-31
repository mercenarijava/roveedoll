package com.android.gjprojection.roveedoll;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import com.android.gjprojection.roveedoll.features.free_line.FreeLineActivity;
import com.android.gjprojection.roveedoll.features.manual.ManualActivity;
import com.android.gjprojection.roveedoll.services.bluetooth.BluetoothManager;
import com.android.gjprojection.roveedoll.utils.Constants;

public class MainActivity extends AppCompatActivity implements UIBase {

    @NonNull
    CardView pMode;
    @NonNull
    CardView mMode;
    @NonNull
    CardView info;
    @NonNull
    CardView tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectViews();
        connectListeners();
    }

    @Override
    public void connectViews() {
        BluetoothManager.startDiscovery(this);
        this.pMode = findViewById(R.id.programmedMode);
        this.mMode = findViewById(R.id.manualMode);
        this.info = findViewById(R.id.information);
        this.tutorial = findViewById(R.id.tutorial);
    }

    @Override
    public void connectListeners() {
        this.setTitle(R.string.menu_title);

        this.pMode.setOnClickListener(v -> {
            startMode(true);
        });

        this.mMode.setOnClickListener(v -> {
            startMode(false);
        });
    }

    private void startMode(
            final boolean vMode) {
        @NonNull final Intent a = new Intent(
                this,
                vMode ? FreeLineActivity.class : ManualActivity.class
        );
        startActivity(a);
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