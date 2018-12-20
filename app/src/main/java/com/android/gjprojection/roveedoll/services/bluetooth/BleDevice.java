package com.android.gjprojection.roveedoll.services.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BleDevice {
    static final String TAG = BleDevice.class.getName();

    enum State {CONNECTED, CONNECTING, DISCONNECTED}

    private final BluetoothDevice bluetoothDevice;
    private State currentState = State.DISCONNECTED;

    public BleDevice(
            @NonNull BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;

        new ConnectThread(this).start();
    }

    public boolean isConnected() {
        return currentState == State.CONNECTED;
    }

    public boolean isConnecting() {
        return currentState == State.CONNECTING;
    }

    public boolean isDisconneted() {
        return currentState == State.DISCONNECTED;
    }

    void setState(
            @NonNull final State state) {
        this.currentState = state;
        Log.d(TAG, "Ev3 " + state.name());

        BluetoothManager.notifyBleDevice();
    }

    private static class ConnectThread extends Thread {
        static final String TAG = ConnectThread.class.getName();
        static final String MY_UUID = "00001101-0000-1000-8000-00805f9b34fb";

        private final BleDevice bleDevice;
        private BluetoothSocket mmSocket;

        ConnectThread(
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
                tmp = bleDevice.bluetoothDevice.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Socket's create() method failed", e);
            }
            this.mmSocket = tmp;
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
                Log.e(TAG, "Connection failed", connectException);
                cancel();
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            // manageMyConnectedSocket(mmSocket); TODO
            int x = 0;
        }

        // Closes the client socket and causes the thread to finish.
        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
}
