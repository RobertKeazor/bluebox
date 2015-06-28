package com.mac.bluebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.google.inject.Inject;
import com.mac.bluebox.bluetooth.BboxBluetoothService;
import com.mac.bluebox.bluetooth.BboxDevicesBroadcastReceiver;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;
import com.mac.bluebox.view.BboxRecyclerViewWrapper;
import com.mac.bluebox.view.SwipeRefreshLayoutWrapper;

import java.util.Set;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final String TAG = MainActivity.class.getName();

    @Inject
    BboxDevicesBroadcastReceiver broadcastReceiver;

    @Inject
    BluetoothAdapter bluetoothAdapter;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Intent intent= new Intent(this, BboxBluetoothService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                turnBluetoothAsServer(new Messenger(service));
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {}
        };

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                getRecyclerViewAdapter().getDevices().add(device);
            }
        }

        new BboxRecyclerViewWrapper(this, R.id.activity_main_recyclerview, getRecyclerViewAdapter());

        new SwipeRefreshLayoutWrapper(this, R.id.activity_main_swipe_refresh_layout,
                getRecyclerViewAdapter(), bluetoothAdapter);
    }

    private void turnBluetoothAsServer(Messenger service) {
        Message msg =  Message.obtain(null, BboxBluetoothService.CONNECT_AS_SERVER);
        try {
            service.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unbindService(serviceConnection);
    }

    public BboxDevicesRecyclerViewAdapter getRecyclerViewAdapter() {
        return broadcastReceiver.getAdapter();
    }
}
