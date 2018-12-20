package com.android.gjprojection.roveedoll.services.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

public class BleDevice {
    enum State {CONNECTED, CONNECTING, DISCONNECTING, DISCONNECTED}

    private final BluetoothDevice bluetoothDevice;
    private State currentState = State.DISCONNECTED;

    public BleDevice(
            @NonNull BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public boolean isConnected(){
        return currentState == State.CONNECTED;
    }

    public boolean isConnecting(){
        return currentState == State.CONNECTING;
    }

    public boolean isDisconnecting(){
        return currentState == State.DISCONNECTING;
    }

    public boolean isDisconneted(){
        return currentState == State.DISCONNECTED;
    }
}
