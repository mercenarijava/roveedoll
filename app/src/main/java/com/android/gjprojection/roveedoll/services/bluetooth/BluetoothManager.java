package com.android.gjprojection.roveedoll.services.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.android.gjprojection.roveedoll.utils.BluetoothUtils;

import static com.android.gjprojection.roveedoll.utils.Constants.PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION;

public class BluetoothManager {
    private static BluetoothManager bluetoothManager;

    private MutableLiveData<BleDevice> activeDeviceLiveData;
    private MutableLiveData<BleReceiveMessage> messageReceiverLiveData;
    private MutableLiveData<Boolean> activeDeviceConnectionLiveData;
    private BluetoothAdapter mBluetoothAdapter;

    // Create a BroadcastReceiver for ACTION_FOUND | ACTION CONNECTED | ACTION DISCONNECTED | ACTION DISCONNECTING
    private final BroadcastReceiver bleEventsReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothManager.deviceFound(device);
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                final BleDevice bleDevice = activeDeviceLiveData.getValue();
                if (bleDevice == null) return;
                bleDevice.setState(BleDevice.State.CONNECTED);
                notifyBleDevice();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                final BleDevice bleDevice = activeDeviceLiveData.getValue();
                if (bleDevice == null) return;
                bleDevice.setState(BleDevice.State.DISCONNECTED);
                notifyBleDevice();
            }
        }
    };

    private BluetoothManager(
            @NonNull Context context) {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        getDeviceConnection();
        getActiveDeviceConnection();
        getMessageReceiver();
        registerBluetoothReceiver(context);

    }

    private void registerBluetoothReceiver(
            @NonNull Context context) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        context.registerReceiver(bleEventsReceiver, filter);
    }

    private void unregisterBluetoothReceiver(
            @NonNull Context context) {
        context.unregisterReceiver(bleEventsReceiver);
    }

    private void manageDeviceFound(
            @NonNull BluetoothDevice bluetoothDevice) {
        @Nullable final BleDevice ev3Device = BluetoothUtils.getEv3BleDevice(bluetoothDevice);
        if (ev3Device != null) activeDeviceLiveData.setValue(ev3Device);
    }

    /**
     * Used to monitor the current connected device
     *
     * @return LiveData<BleDevice>
     */
    public LiveData<BleDevice> getDeviceConnection() {
        if (this.activeDeviceLiveData == null) {
            this.activeDeviceLiveData = new MutableLiveData<>();
        }
        return activeDeviceLiveData;
    }

    /**
     * Used to monitor the device connection
     *
     * @return LiveData<Boolean> true if is connected false otherwise
     */
    public LiveData<Boolean> getActiveDeviceConnection() {
        if (this.activeDeviceConnectionLiveData == null) {
            this.activeDeviceConnectionLiveData = new MutableLiveData<>();
            this.activeDeviceConnectionLiveData.setValue(
                    this.activeDeviceLiveData != null &&
                            this.activeDeviceLiveData.getValue() != null &&
                            this.activeDeviceLiveData.getValue().isConnected()
            );
        }
        return activeDeviceConnectionLiveData;
    }

    /**
     * Used to receive messages from Ev3 robot
     *
     * @return LiveData<BleReceiveMessage>
     */
    @UiThread
    public LiveData<BleReceiveMessage> getMessageReceiver() {
        if (this.messageReceiverLiveData == null) {
            this.messageReceiverLiveData = new MutableLiveData<>();
        }
        this.messageReceiverLiveData.setValue(null);
        return messageReceiverLiveData;
    }

    /**
     * Use to write something on bluetooth connection
     *
     * @param message to write
     * @return true if successfully write on stream, false otherwise
     */
    public synchronized static <T extends BleWrittable> boolean writeData(
            @NonNull final T message) {
        if (bluetoothManager.activeDeviceLiveData == null ||
                bluetoothManager.activeDeviceLiveData.getValue() == null ||
                !bluetoothManager.activeDeviceLiveData.getValue().isConnected()) return false;

        return bluetoothManager.activeDeviceLiveData.getValue().writeData(message);
    }

    /**
     * Use to start the device discovery
     */
    public static void startDiscovery(
            @NonNull Activity activity) {
        if (bluetoothManager == null) return;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
            );
            return;
        }

        startDiscovery();
    }

    /**
     * Use to start the device discovery
     */
    public static void startDiscovery() {
        if (bluetoothManager == null) return;
        if (bluetoothManager.mBluetoothAdapter.isDiscovering()) stopDiscovery();
        bluetoothManager
                .mBluetoothAdapter
                .startDiscovery();
    }

    /**
     * Use to stop current device discovery
     */
    static void stopDiscovery() {
        if (bluetoothManager == null) return;
        bluetoothManager
                .mBluetoothAdapter
                .cancelDiscovery();
    }

    /**
     * Used to notify ble device changes to all active observers
     */
    static void notifyBleDevice() {
        bluetoothManager
                .activeDeviceLiveData
                .postValue(bluetoothManager.activeDeviceLiveData.getValue());
    }

    /**
     * Used to notify new device found
     *
     * @param bluetoothDevice found
     */
    static void deviceFound(
            @NonNull BluetoothDevice bluetoothDevice) {
        if (bluetoothManager == null) return;
        bluetoothManager.manageDeviceFound(bluetoothDevice);
    }

    /**
     * Used to notify new received message
     *
     * @param bleReceiveMessage received
     */
    static void messageReceiver(
            @NonNull final BleReceiveMessage bleReceiveMessage) {
        if (bluetoothManager == null) return;
        bluetoothManager.messageReceiverLiveData.postValue(bleReceiveMessage);
    }

    static void connectionStateChanged(
            final boolean connected) {
        if (bluetoothManager == null) return;
        bluetoothManager.activeDeviceConnectionLiveData.postValue(connected);
    }

    /**
     * Used to enable the ble manager
     * Call this method in onCreate of Application file
     * If the phone doesn't support the bluetooth, it returns null
     *
     * @param context Application context
     * @return null if the device doesn't support bluetooth, BluetoothManager otherwise
     */
    public static BluetoothManager init(
            @NonNull final Context context) {
        if (bluetoothManager == null) {
            bluetoothManager = new BluetoothManager(context);
            if (bluetoothManager.mBluetoothAdapter == null) bluetoothManager = null;
        }
        return bluetoothManager;
    }

    public static void end(
            @NonNull final Context context) {
        if (bluetoothManager == null) return;
        bluetoothManager.unregisterBluetoothReceiver(context);
        bluetoothManager = null;
    }


}
