package com.android.gjprojection.roveedoll.services;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.gjprojection.roveedoll.R;

public class BluetoothManager {
    private static BluetoothManager bluetoothManager;
    private MutableLiveData<Boolean> deviceConnectionLiveData;
    private MutableLiveData<Boolean> bluetoothConnectionLiveData;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private MutableLiveData<BluetoothHeadset> mBluetoothHeadset;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset.postValue((BluetoothHeadset) proxy);
            }
        }

        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset.postValue(null);
            }
        }
    };

    private MediaPlayer bluetoothConnectedPlayer;

    private BluetoothManager(@NonNull Context context) {
        this.mBluetoothHeadset = new MutableLiveData<>();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.bluetoothConnectedPlayer = MediaPlayer.create(context, R.raw.sound_bluetooth_connected);
    }

    private boolean isBluetoothSupported() {
        return this.mBluetoothAdapter != null;
    }

    private MutableLiveData<BluetoothHeadset> connectProxy(@NonNull Context context) {
        this.mBluetoothAdapter.getProfileProxy(context, mProfileListener, BluetoothProfile.HEADSET);
        return mBluetoothHeadset;
    }

    private void disconnectProxy() {
        this.mBluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, mBluetoothHeadset.getValue());
    }

    private void observeDeviceConnection(){
        if(deviceConnectionLiveData == null) return;
        deviceConnectionLiveData.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean connected) {
                if (connected == null) return;
                if (connected) bluetoothConnectedPlayer.start();
            }
        });
    }

    public LiveData<Boolean> getDeviceConnection() {
        if (deviceConnectionLiveData == null) {
            deviceConnectionLiveData = new MutableLiveData<>();
            observeDeviceConnection();
            // SOLO PER TEST
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    deviceConnectionLiveData.postValue(true);
                }
            }, 6000);
            // FIXME
        }
        return deviceConnectionLiveData;
    }

    public LiveData<Boolean> getBluettothConnection() {
        if (bluetoothConnectionLiveData == null) {
            bluetoothConnectionLiveData = new MutableLiveData<>();
            // FIXME
        }
        return bluetoothConnectionLiveData;
    }

    public static BluetoothManager get() {
        return bluetoothManager;
    }

    static BluetoothManager init(@NonNull final Context context) {
        if (bluetoothManager == null) {
            bluetoothManager = new BluetoothManager(context);
        }
        return bluetoothManager;
    }


}
