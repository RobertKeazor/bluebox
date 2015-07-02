package com.mac.bluebox.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.google.inject.Inject;
import com.mac.bluebox.activity.MainActivity;
import com.mac.bluebox.view.DevicesRecyclerViewAdapter;


/**
 * Created by anyer on 6/26/15.
 */
public class DevicesBroadcastReceiver extends BroadcastReceiver {
    @Inject
    DevicesRecyclerViewAdapter adapter;

    private static final String TAG = DevicesBroadcastReceiver.class.getName();
    public static final String SERVING = "SERVING";
    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String STOP_SERVING = "STOP_SERVING";

//    private DevicesRecyclerViewAdapter adapter;
    private Handler mHandler;

//    public DevicesBroadcastReceiver(DevicesRecyclerViewAdapter adapter) {
//        this.adapter = adapter;
//    }

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

        if (DevicesBroadcastReceiver.SERVING.equals(action) && mHandler != null) {
            adapter.getDevices().clear();
            adapter.notifyDataSetChanged();

            String deviceName = intent.getStringExtra(DevicesBroadcastReceiver.DEVICE_NAME);

            mHandler.obtainMessage(MainActivity.UI_CHANGE_TO_SERVING, deviceName).sendToTarget();
        }

        if (DevicesBroadcastReceiver.STOP_SERVING.equals(action) && mHandler != null) {
            mHandler.obtainMessage(MainActivity.UI_CHANGE_TO_LIST_DEVICES).sendToTarget();
        }
    }

    public DevicesRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setup(Handler handler) {
        this.mHandler = handler;
    }
}
