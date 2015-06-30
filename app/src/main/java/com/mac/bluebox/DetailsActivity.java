package com.mac.bluebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.inject.Inject;
import com.mac.bluebox.bluetooth.BboxBluetoothService;
import com.mac.bluebox.bluetooth.BboxTracksBroadcastReceiver;
import com.mac.bluebox.bluetooth.DetailActivityServiceConnection;
import com.mac.bluebox.view.BboxRecyclerViewWrapper;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_details)
public class DetailsActivity extends RoboActivity {
    private static final String TAG = DetailsActivity.class.getName();
    @Inject
    BluetoothAdapter mBluetoothAdapter;

    @Inject
    BboxTracksBroadcastReceiver broadcastReceiver;

    @Inject
    DetailActivityServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BboxTracksBroadcastReceiver.TRACKS_LIST_DISCOVERED);
        registerReceiver(broadcastReceiver, filter); // Don't forget to unregister during onDestroy

        new BboxRecyclerViewWrapper(this, R.id.activity_details_recyclerview, getRecyclerViewAdapter());

        Object device = getIntent().getExtras().get(BluetoothDevice.EXTRA_DEVICE);
        serviceConnection.setDevice(device);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent= new Intent(this, BboxBluetoothService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        serviceConnection.disconnectFromServer();
        unbindService(serviceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }

    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return broadcastReceiver.getAdapter();
    }
}
