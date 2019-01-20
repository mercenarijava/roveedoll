package com.android.gjprojection.roveedoll.services.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

class ConnectedThread extends Thread {
    static final String TAG = ConnectedThread.class.getName();

    private final BufferedReader mmInStream;
    private final PrintStream mmOutStream;
    private boolean readOn = true;
    private byte[] mmBuffer; // mmBuffer store for the stream

    ConnectedThread(
            @NonNull BluetoothSocket mmSocket) {

        BufferedReader tmpIn;
        PrintStream tmpOut;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()), 2);
        } catch (IOException e) {
            tmpIn = null;
        }
        try {
            tmpOut = new PrintStream(mmSocket.getOutputStream(), true);
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
                final String read = mmInStream.readLine();

                if (!read.isEmpty()) {
                    @NonNull final String[] parsedMsg = read.split(",");
                    if (parsedMsg.length == 3) {
                        BluetoothManager.messageReceiver(
                                new BleReceiveMessage(
                                        Integer.parseInt(parsedMsg[1]),
                                        Long.parseLong(parsedMsg[2])
                                )
                        );
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                readOn = false;
            }


        }
    }

    <T extends BleWrittable> boolean write(
            @NonNull final T message) {
        if (this.mmOutStream == null) return false;
        this.mmOutStream.println(message.getJSON() + '\n');
        this.mmOutStream.flush();
        return true;
    }

}
