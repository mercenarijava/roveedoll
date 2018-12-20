package com.android.gjprojection.roveedoll.services.bluetooth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.android.gjprojection.roveedoll.utils.BluetoothUtils;

public class BluetoothManager {
    private static BluetoothManager bluetoothManager;
    private MutableLiveData<BleDevice> connectedDeviceLiveData;

    private BluetoothAdapter mBluetoothAdapter;
    // private MediaPlayer bluetoothConnectedPlayer;

    private BluetoothManager(
            @NonNull Context context) {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // this.bluetoothConnectedPlayer = MediaPlayer.create(context, R.raw.sound_bluetooth_connected);
    }

    /**
     * Used to monitor the current connected device
     *
     * @return LiveData<BleDevice>
     */
    public LiveData<BleDevice> getDeviceConnection() {
        if (this.connectedDeviceLiveData == null) {
            this.connectedDeviceLiveData = new MutableLiveData<>();
        }
        return connectedDeviceLiveData;
    }

    private void manageDeviceFound(
            @NonNull BluetoothDevice bluetoothDevice){
        final BleDevice ev3Device = BluetoothUtils.getEv3BleDevice(bluetoothDevice);



    }

    /**
     * Used to notify new device found
     *
     * @param bluetoothDevice found
     */
    static void deviceFound(
            @NonNull BluetoothDevice bluetoothDevice) {
        if(bluetoothManager == null) return;
        bluetoothManager.manageDeviceFound(bluetoothDevice);
    }

    /**
     * Used to enable the ble manager
     * Call this method in onCreate of Application file
     * If the phone doesn't support the bluetooth, it returns null
     *
     * @param context Application context
     * @return null if the device doesn't support bluetooth, BluetoothManager otherwise
     */
    static BluetoothManager init(
            @NonNull final Context context) {
        if (bluetoothManager == null) {
            bluetoothManager = new BluetoothManager(context);
            if (bluetoothManager.mBluetoothAdapter == null) bluetoothManager = null;
        }
        return bluetoothManager;
    }


}
