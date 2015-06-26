package com.mac.bluebox.bluetooth;

import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by anyer on 6/26/15.
 */
public class BluetoothHelper extends Service {

    private BluetoothDevice bluetoothDevice;

    public List<String> getOnlineDevices() {
        return null;
    }

    public void connect(String friend) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        connectToDevice(bluetoothDevice);
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
