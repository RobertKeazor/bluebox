package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.mac.bluebox.MainActivity;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;


/**
 * Created by anyer on 6/26/15.
 */
public class BboxDevicesBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = BboxDevicesBroadcastReceiver.class.getName();
    public static final String SERVING = "SERVING";
    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String STOP_SERVING = "STOP_SERVING";

    private BboxDevicesRecyclerViewAdapter adapter;
    private Handler mHandler;

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

        if (BboxDevicesBroadcastReceiver.SERVING.equals(action) && mHandler != null) {
            adapter.getDevices().clear();
            adapter.notifyDataSetChanged();

            String deviceName = intent.getStringExtra(BboxDevicesBroadcastReceiver.DEVICE_NAME);

            mHandler.obtainMessage(MainActivity.UI_CHANGE_TO_SERVING, deviceName).sendToTarget();
        }

        Log.e(TAG, "PUTA ACCION: " + action);
        if (BboxDevicesBroadcastReceiver.STOP_SERVING.equals(action) && mHandler != null) {
            mHandler.obtainMessage(MainActivity.UI_CHANGE_TO_LIST_DEVICES).sendToTarget();
        }
    }

    public BboxDevicesRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }
}
