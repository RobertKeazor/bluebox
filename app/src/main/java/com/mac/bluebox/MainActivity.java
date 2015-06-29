package com.mac.bluebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.inject.Inject;
import com.mac.bluebox.bluetooth.BboxBluetoothService;
import com.mac.bluebox.bluetooth.MainActivityServiceConnection;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;
import com.mac.bluebox.view.BboxRecyclerViewWrapper;
import com.mac.bluebox.view.SwipeRefreshLayoutWrapper;

import java.util.Set;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    @Inject
    BboxDevicesRecyclerViewAdapter mBboxDevicesRecyclerViewAdapter;

    @Inject
    BluetoothAdapter mBluetoothAdapter;

    @Inject
    MainActivityServiceConnection mServiceConnection;

    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_ENABLE_BT = 1;
    private SwipeRefreshLayoutWrapper swipeRefreshLayoutWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        new BboxRecyclerViewWrapper(this, R.id.activity_main_recyclerview,
                mBboxDevicesRecyclerViewAdapter);

        swipeRefreshLayoutWrapper = new SwipeRefreshLayoutWrapper(this, R.id.activity_main_swipe_refresh_layout,
                mBboxDevicesRecyclerViewAdapter, mBluetoothAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent= new Intent(this, BboxBluetoothService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        swipeRefreshLayoutWrapper.refreshDevices();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mServiceConnection.turnOffBluetoothServer();
        unbindService(mServiceConnection);
    }
}
