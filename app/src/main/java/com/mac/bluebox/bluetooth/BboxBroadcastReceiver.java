package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mac.bluebox.model.DeviceModel;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = BboxBroadcastReceiver.class.getName();
    private BboxRecyclerViewAdapter adapter;

    public BboxBroadcastReceiver(BboxRecyclerViewAdapter adapter) {
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
                DeviceModel deviceModel = new DeviceModel(device.getName(), device.getAddress());

                Log.e(TAG, deviceModel.getName() + " @ " + deviceModel.getMac());
                adapter.getDevices().add(deviceModel);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public BboxRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
