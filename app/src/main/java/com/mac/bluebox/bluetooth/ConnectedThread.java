package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by anyer on 6/27/15.
 */
public class ConnectedThread extends Thread {
    private static final String TAG = ConnectedThread.class.getName();
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Handler mHandler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        setName(ConnectedThread.class.getName());

        mmSocket = socket;
        mHandler = handler;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (!isInterrupted()) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                mHandler.obtainMessage(BboxBluetoothService.SOCKET_MESSAGE_READ, bytes, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                break;
            }
        }

        cancel();
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }

    /**
     * Will cancel an in-progress connection, and close the socket
     */
    public void cancel() {
        interrupt();
        try {
            mmSocket.close();
        } catch (IOException e) {
        }

        mHandler.obtainMessage(BboxBluetoothService.SOCKET_DISCONNECTED).sendToTarget();

        Log.e(TAG, "Socket closed.");
    }
}