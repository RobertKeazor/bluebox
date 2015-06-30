package com.mac.bluebox;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.mac.bluebox.bluetooth.BboxBluetoothService;
import com.mac.bluebox.bluetooth.BboxDevicesBroadcastReceiver;
import com.mac.bluebox.bluetooth.BboxTracksBroadcastReceiver;
import com.mac.bluebox.bluetooth.MainActivityServiceConnection;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;
import com.mac.bluebox.view.BboxRecyclerViewWrapper;
import com.mac.bluebox.view.SwipeRefreshLayoutWrapper;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    public static final int UI_CHANGE_TO_SERVING = 1;
    public static final int UI_CHANGE_TO_LIST_DEVICES = 2;

    @InjectView(R.id.activity_main_textView_message)
    TextView textViewMessage;

    @Inject
    BboxDevicesBroadcastReceiver broadcastReceiver;

    @Inject
    BboxDevicesRecyclerViewAdapter mBboxDevicesRecyclerViewAdapter;

    @Inject
    BluetoothAdapter mBluetoothAdapter;

    @Inject
    MainActivityServiceConnection mServiceConnection;

    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_ENABLE_BT = 1;
    private SwipeRefreshLayoutWrapper swipeRefreshLayoutWrapper;
    private MainActivityHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new MainActivityHandler();
        broadcastReceiver.setHandler(mHandler);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BboxDevicesBroadcastReceiver.SERVING);
        registerReceiver(broadcastReceiver, filter); // Don't forget to unregister during onDestroy

        filter = new IntentFilter(BboxDevicesBroadcastReceiver.STOP_SERVING);
        registerReceiver(broadcastReceiver, filter); // Don't forget to unregister during onDestroy

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        new BboxRecyclerViewWrapper(this, R.id.activity_main_recyclerview,
                mBboxDevicesRecyclerViewAdapter);

        swipeRefreshLayoutWrapper = new SwipeRefreshLayoutWrapper(this, R.id.activity_main_swipe_refresh_layout,
                mBboxDevicesRecyclerViewAdapter, mBluetoothAdapter);

        textViewMessage.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }


    class MainActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MainActivity.UI_CHANGE_TO_SERVING:
                    swipeRefreshLayoutWrapper.getSwipeRefreshLayout().setVisibility(View.GONE);

                    textViewMessage.setVisibility(View.VISIBLE);
                    textViewMessage.setText("SERVING to " + (String)msg.obj);

                    break;

                case MainActivity.UI_CHANGE_TO_LIST_DEVICES:
                    textViewMessage.setVisibility(View.GONE);
                    swipeRefreshLayoutWrapper.getSwipeRefreshLayout().setVisibility(View.VISIBLE);

                    swipeRefreshLayoutWrapper.refreshDevices();
                    break;
            }
        }
    }
}
