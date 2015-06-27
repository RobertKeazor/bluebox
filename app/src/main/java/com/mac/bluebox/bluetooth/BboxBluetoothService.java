package com.mac.bluebox.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBluetoothService extends Service {
    private static final int MSG_SAY_HELLO = 1;

    private BluetoothDevice bluetoothDevice;

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());


    @Override
    public IBinder onBind(Intent intent) {
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if(bluetoothDevice != null) {
            connectToDevice(bluetoothDevice);
            return mMessenger.getBinder();
        }

        return null;
    }

    private void connectToDevice(BluetoothDevice bluetoothDevice) {
        BluetoothSocket bluetoothSocket = null;
        UUID blueBoxUuid = new UUID(1000001, 100000001);
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(blueBoxUuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        handleDeviceConnected(bluetoothSocket);
    }

    private void handleDeviceConnected(BluetoothSocket bluetoothSocket) {
        //do stuff
    }


}
