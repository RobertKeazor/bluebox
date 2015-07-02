package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;
import android.util.Size;

import com.mac.bluebox.helper.PlayAudioHelper;
import com.mac.bluebox.helper.StreamAudioHelper;
import com.mac.bluebox.service.BboxBluetoothService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by anyer on 6/27/15.
 */
public class ConnectedThread extends Thread {
    private static final String TAG = ConnectedThread.class.getName();

    public static final byte SERVER_SEND_LIST_OF_TRACKS = 10;
    public static final byte SERVER_SEND_STREAM_TRACK = 50;
    public static final byte CLIENT_SEND_PLAY_TRACK = 100;

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
        int SIZE = StreamAudioHelper.BUF_SIZE + 1;
        byte[] buffer = new byte[SIZE];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (!isInterrupted()) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);

                if (bytes > 1) {
                    byte operation = buffer[0];

                    byte[] data = new byte[bytes - 1];

                    for (int i = 0; i < bytes - 1; i++) {
                        data[i] = buffer[i + 1];
                    }

                    Log.e(TAG, "Operation: " + operation + ", Bytes: " + bytes);
                    switch (operation) {
                        case ConnectedThread.SERVER_SEND_LIST_OF_TRACKS:
                            mHandler.obtainMessage(BboxBluetoothService.CLIENT_RECEIVE_LIST_OF_TRACKS,
                                    data).sendToTarget();
                            break;

                        case ConnectedThread.SERVER_SEND_STREAM_TRACK:
                            mHandler.obtainMessage(BboxBluetoothService.CLIENT_RECEIVE_STREAM_TRACK,
                                    bytes - 1, -1, data).sendToTarget();

                            break;

                        case ConnectedThread.CLIENT_SEND_PLAY_TRACK:
                            mHandler.obtainMessage(BboxBluetoothService.SERVER_RECEIVE_PLAY_TRACK,
                                    data).sendToTarget();

                            break;
                    }
                }
            } catch (Throwable e) {
                Log.e(TAG, e.getMessage());
                break;
            }
        }

        cancel();
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
            mmOutStream.flush();
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

        mHandler.obtainMessage(BboxBluetoothService.SERVER_DETECTED_A_DISCONNECTION).sendToTarget();

        Log.e(TAG, "Socket closed.");
    }

    public String getDeviceName() {
        return mmSocket.getRemoteDevice().getName();
    }
}