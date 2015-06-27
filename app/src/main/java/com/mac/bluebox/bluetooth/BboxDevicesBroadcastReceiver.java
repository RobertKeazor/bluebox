package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;


/**
 * Created by anyer on 6/26/15.
 */
public class BboxDevicesBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = BboxDevicesBroadcastReceiver.class.getName();
    public static final String TRACKS_LIST_DISCOVERED = "TRACKS_LIST_DISCOVERED";
    private BboxDevicesRecyclerViewAdapter adapter;

    public BboxDevicesBroadcastReceiver(BboxDevicesRecyclerViewAdapter adapter) {
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

                adapter.getDevices().add(device);
                adapter.notifyDataSetChanged();
            }
        }
        if(BboxDevicesBroadcastReceiver.TRACKS_LIST_DISCOVERED.equals(action)){

        }
    }

    public BboxDevicesRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
