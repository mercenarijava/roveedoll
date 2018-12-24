package com.android.gjprojection.roveedoll.services.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.gjprojection.roveedoll.utils.JacksonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ConnectedThread extends Thread {
    static final String TAG = ConnectedThread.class.getName();

    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private boolean readOn = true;
    private byte[] mmBuffer; // mmBuffer store for the stream

    ConnectedThread(
            @NonNull BluetoothSocket mmSocket) {

        InputStream tmpIn;
        OutputStream tmpOut;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = mmSocket.getInputStream();
        } catch (IOException e) {
            tmpIn = null;
        }
        try {
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            tmpOut = null;
        }

        this.mmInStream = tmpIn;
        this.mmOutStream = tmpOut;
    }


    public void run() {
        this.mmBuffer = new byte[1024];

        // Keep listening to the InputStream until an exception occurs.
        while (readOn) {
            // Read from the InputStream.
            try {
                final int numBytes = mmInStream.read(mmBuffer);
                final String read = new String(mmBuffer);
                final BleReceiveMessage receiveMessage =
                        JacksonUtils.read(BleReceiveMessage.class, read);

                if (receiveMessage != null) {
                    BluetoothManager.messageReceiver(receiveMessage);
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                readOn = false;
            }


        }
    }

    // Call this from the main activity to send data to the remote device.
    boolean write(
            @NonNull final BleSendMessage message) {
        if (this.mmOutStream == null) return false;
        try {
            this.mmOutStream.write(message.getJSON().getBytes());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

}
