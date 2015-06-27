package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.inject.Inject;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = BboxBroadcastReceiver.class.getName();
    private BluetoothArrayAdapter adapter;

    public BboxBroadcastReceiver(BluetoothArrayAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (device != null && device.getName() != null) {
                // Add the name and address to an array adapter to show in a ListView
                String deviceString = device.getName(); // + " @ " + device.getAddress();

                Log.e(TAG, deviceString);

                adapter.add(deviceString);
            }
        }
    }

    public BluetoothArrayAdapter getAdapter() {
        return adapter;
    }
}
