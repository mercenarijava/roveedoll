package com.android.gjprojection.roveedoll.services.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectionThread extends Thread {
    static final String TAG = ConnectionThread.class.getName();
    static final String MY_UUID = "00001101-0000-1000-8000-00805f9b34fb";

    private final BleDevice bleDevice;
    private BluetoothSocket mmSocket;

    ConnectionThread(
            @NonNull BleDevice bleDevice) {
        this.bleDevice = bleDevice;
        createRfcommSocket();
    }

    private void createRfcommSocket() {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            final UUID SPP_UUID = UUID.fromString(MY_UUID);
            tmp = bleDevice.getBluetoothDevice().createInsecureRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            bleDevice.setState(BleDevice.State.DISCONNECTED);
        }
        this.mmSocket = tmp;
    }

    // Closes the client socket and causes the thread to finish.
    void cancel() {
        try {
            this.mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void run() {

        if (this.mmSocket == null) return;

        // Cancel discovery because it otherwise slows down the connection.
        BluetoothManager.stopDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception
            this.bleDevice.setState(BleDevice.State.CONNECTING);
            this.mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            Log.e(TAG, connectException.getMessage());
            this.bleDevice.setState(BleDevice.State.DISCONNECTED);
            cancel();
            return;
        }

        manageSocketConnection(this.mmSocket);
    }

    private void manageSocketConnection(
            @NonNull BluetoothSocket mmSocket) {
        this.bleDevice.setConnectedThread(new ConnectedThread(mmSocket));
    }

}
