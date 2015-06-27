package com.mac.bluebox.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBluetoothService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private BluetoothDevice bluetoothDevice;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        BboxBluetoothService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BboxBluetoothService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if(bluetoothDevice != null) {
            connectToDevice(bluetoothDevice);
            return mBinder;
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

    public List<String> getTracks(){
        return new ArrayList<String>();
    }

}
