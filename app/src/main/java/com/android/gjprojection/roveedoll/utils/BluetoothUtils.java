package com.android.gjprojection.roveedoll.utils;


import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.gjprojection.roveedoll.services.bluetooth.BleDevice;

public class BluetoothUtils {

    @Nullable
    public static BleDevice getEv3BleDevice(
            @NonNull BluetoothDevice bluetoothDevice) {
        if (isEv3BleDevice(bluetoothDevice)) return new BleDevice(bluetoothDevice);
        return null;
    }

    public static boolean isEv3BleDevice(
            @NonNull BluetoothDevice bluetoothDevice) {
        return bluetoothDevice.getName() != null && bluetoothDevice.getName().endsWith(Constants.EV3_ROBOT_NAME);
    }

}
