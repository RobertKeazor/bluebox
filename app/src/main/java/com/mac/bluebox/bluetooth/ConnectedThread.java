package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.mac.bluebox.helper.ArrayHelper;
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
        int SIZE = 1024;
        byte[] buffer = new byte[SIZE];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (!isInterrupted()) {
            try {
                // buffer has the follow structure: code(byte),sizeOfDataPacket(4bytes),data(byte[])
                // Read from the InputStream
                bytes = mmInStream.read(buffer);

                if (bytes > 1) {
//                    int unparsedBytes = bytes;
//                    while (unparsedBytes > 0) {
                        byte operation = ArrayHelper.decodeCommandPacket(buffer);
                        int packetSize = ArrayHelper.decodeDataPacketSize(buffer);
                        byte[] data = ArrayHelper.decodeDataPacket(buffer);

                        handlePacket(operation, data, packetSize);

                        // 1 byte of command, 4 bytes of dataSize
//                        unparsedBytes = bytes - packetSize;
//                        bytes = unparsedBytes;
//                    }

                    Log.e(TAG, "Operation: " + buffer[0] + ", Bytes: " + bytes + ", UNPARSED: " + (bytes - 5 - packetSize));
                }
            } catch (Throwable e) {
                Log.e(TAG, e.getMessage());
                break;
            }
        }

        cancel();
    }

    private void handlePacket(byte operation, byte[] data, int packetSize) {
        switch (operation) {
            case ConnectedThread.SERVER_SEND_LIST_OF_TRACKS:
                mHandler.obtainMessage(BboxBluetoothService.CLIENT_RECEIVE_LIST_OF_TRACKS,
                        packetSize, -1, data).sendToTarget();
                break;

            case ConnectedThread.SERVER_SEND_STREAM_TRACK:
                mHandler.obtainMessage(BboxBluetoothService.CLIENT_RECEIVE_STREAM_TRACK,
                        packetSize, -1, data).sendToTarget();

                break;

            case ConnectedThread.CLIENT_SEND_PLAY_TRACK:
                mHandler.obtainMessage(BboxBluetoothService.SERVER_RECEIVE_PLAY_TRACK,
                        packetSize, -1, data).sendToTarget();

                break;
        }

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