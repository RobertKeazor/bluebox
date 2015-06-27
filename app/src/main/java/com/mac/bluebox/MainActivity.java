package com.mac.bluebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import com.google.inject.Inject;
import com.mac.bluebox.bluetooth.BboxBroadcastReceiver;
import com.mac.bluebox.bluetooth.BluetoothArrayAdapter;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    @InjectView(R.id.listViewDevices)
    ListView listViewDevices;

    @Inject
    BboxBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter); // Don't forget to unregister during onDestroy

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.startDiscovery();
    }

    @Override
    protected void onResume() {
        super.onResume();

        listViewDevices.setAdapter(broadcastReceiver.getAdapter());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }


    public BluetoothArrayAdapter getBluetoothArrayAdapter() {
        return broadcastReceiver.getAdapter();
    }


    private void startDiscoveringDevices() {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }
}
