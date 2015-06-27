package com.mac.bluebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.inject.Inject;
import com.mac.bluebox.bluetooth.BboxDevicesBroadcastReceiver;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;
import com.mac.bluebox.view.BboxRecyclerViewWrapper;
import com.mac.bluebox.view.SwipeRefreshLayoutWrapper;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    @Inject
    BboxDevicesBroadcastReceiver broadcastReceiver;

    @Inject
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothAdapter.cancelDiscovery();

//      Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter); // Don't forget to unregister during onDestroy

        new BboxRecyclerViewWrapper(this, R.id.activity_main_recyclerview, getRecyclerViewAdapter());

        new SwipeRefreshLayoutWrapper(this, R.id.activity_main_swipe_refresh_layout,
                getRecyclerViewAdapter(), bluetoothAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onPause() {
        super.onPause();

        bluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }


    public BboxDevicesRecyclerViewAdapter getRecyclerViewAdapter() {
        return broadcastReceiver.getAdapter();
    }


    private void startDiscoveringDevices() {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }
}
