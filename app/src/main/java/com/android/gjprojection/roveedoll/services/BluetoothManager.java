package com.android.gjprojection.roveedoll.services;

import android.arch.lifecycle.MutableLiveData;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by recosta32 on 20/11/2018.
 */

public class BluetoothManager {
    private MutableLiveData<BluetoothHeadset> mBluetoothHeadset;
    // Get the default adapter
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

    private BluetoothManager(@NonNull Context context) {
        this.mBluetoothHeadset = new MutableLiveData<>();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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


}
