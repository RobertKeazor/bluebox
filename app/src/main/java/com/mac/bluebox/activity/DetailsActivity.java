package com.mac.bluebox.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.inject.Inject;
import com.mac.bluebox.R;
import com.mac.bluebox.receivers.TracksBroadcastReceiver;
import com.mac.bluebox.service.BboxBluetoothService;
import com.mac.bluebox.service.DetailActivityServiceConnection;
import com.mac.bluebox.view.TracksRecyclerViewAdapter;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_details)
public class DetailsActivity extends RoboActivity {
    private static final String TAG = DetailsActivity.class.getName();

    @Inject
    TracksRecyclerViewAdapter mRecyclerViewAdapter;

    @Inject
    TracksBroadcastReceiver mBroadcastReceiver;

    @Inject
    DetailActivityServiceConnection mServiceConnection;

    @InjectView(R.id.activity_details_recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver();
        setupServiceConnection();

        setupRecyclerViewAdapter();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent= new Intent(this, BboxBluetoothService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mServiceConnection.disconnectFromServer();
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);
    }

    public void playTrack(int trackId) {
        mServiceConnection.playTrack(trackId);
    }

    private void setupRecyclerViewAdapter() {
        mRecyclerViewAdapter.setActivity(this);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    private void setupServiceConnection() {
        Object device = getIntent().getExtras().get(BluetoothDevice.EXTRA_DEVICE);
        mServiceConnection.setDevice(device);
    }

    private void registerReceiver() {
        mBroadcastReceiver.setup(mRecyclerViewAdapter);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(TracksBroadcastReceiver.TRACKS_LIST_DISCOVERED);
        registerReceiver(mBroadcastReceiver, filter); // Don't forget to unregister during onDestroy
    }
}
