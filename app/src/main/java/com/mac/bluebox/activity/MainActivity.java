package com.mac.bluebox.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.mac.bluebox.R;
import com.mac.bluebox.receivers.DevicesBroadcastReceiver;
import com.mac.bluebox.service.BboxBluetoothService;
import com.mac.bluebox.service.MainActivityServiceConnection;
import com.mac.bluebox.view.DevicesRecyclerViewAdapter;

import java.util.Set;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    @InjectView(R.id.activity_main_textView_message)
    TextView mTextViewMessage;

    @Inject
    DevicesBroadcastReceiver mBroadcastReceiver;

    @Inject
    DevicesRecyclerViewAdapter mDevicesRecyclerViewAdapter;

    @Inject
    BluetoothAdapter mBluetoothAdapter;

    @Inject
    MainActivityServiceConnection mServiceConnection;

    @Inject
    MainActivityHandler mHandler;

    @InjectView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.activity_main_recyclerview)
    RecyclerView mRecyclerView;

    public static final int UI_CHANGE_TO_SERVING = 1;
    public static final int UI_CHANGE_TO_LIST_DEVICES = 2;
    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTextViewMessage.setVisibility(View.GONE);

        setupRecyclerView();

        setupSwipeRefreshLayout();

        setupHandler();

        registerReceivers();

        Intent intent = new Intent(this, BboxBluetoothService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        refreshPairedDevices();
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPairedDevices();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        mServiceConnection.turnOffBluetoothServer();
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);
    }

    private void enableBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void registerReceivers() {
        mBroadcastReceiver.setup(mHandler);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(DevicesBroadcastReceiver.SERVING);
        registerReceiver(mBroadcastReceiver, filter); // Don't forget to unregister during onDestroy

        filter = new IntentFilter(DevicesBroadcastReceiver.STOP_SERVING);
        registerReceiver(mBroadcastReceiver, filter); // Don't forget to unregister during onDestroy
    }

    public void refreshPairedDevices() {
        mDevicesRecyclerViewAdapter.getDevices().clear();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mDevicesRecyclerViewAdapter.getDevices().add(device);
            }
        }

        mDevicesRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setupHandler() {
        mHandler.setup(this, mSwipeRefreshLayout, mTextViewMessage);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mDevicesRecyclerViewAdapter);
    }
}
