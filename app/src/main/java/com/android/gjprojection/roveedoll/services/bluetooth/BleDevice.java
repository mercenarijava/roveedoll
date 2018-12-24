package com.android.gjprojection.roveedoll.services.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.support.annotation.NonNull;
import android.util.Log;

public class BleDevice extends BluetoothGattCallback {
    static final String TAG = BleDevice.class.getName();
    static final String MY_UUID = "00001101-0000-1000-8000-00805f9b34fb";


    enum State {CONNECTED, CONNECTING, DISCONNECTED}

    private final BluetoothDevice bluetoothDevice;
    private ConnectedThread connectedThread;
    private State currentState = State.DISCONNECTED;

    public BleDevice(
            @NonNull BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        new ConnectionThread(this).start();
    }

    public boolean isConnected() {
        return currentState == State.CONNECTED;
    }

    public boolean isConnecting() {
        return currentState == State.CONNECTING;
    }

    public boolean isDisconnected() {
        return currentState == State.DISCONNECTED;
    }

    void setState(
            @NonNull final State state) {
        this.currentState = state;
        Log.d(TAG, "Ev3 " + state.name());

        if (isDisconnected()) {
            BluetoothManager.startDiscovery();
        }

        BluetoothManager.connectionStateChanged(isConnected());
        BluetoothManager.notifyBleDevice();
    }

    public boolean writeData(
            @NonNull final BleSendMessage message) {
        if (connectedThread == null) return false;
        return connectedThread.write(message);
    }

    BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    void setConnectedThread(
            @NonNull ConnectedThread connectedThread) {
        this.connectedThread = connectedThread;
        this.connectedThread.start();
    }
}
