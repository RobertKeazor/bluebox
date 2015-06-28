package com.mac.bluebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.inject.Inject;
import com.mac.bluebox.bluetooth.BboxBluetoothService;
import com.mac.bluebox.bluetooth.BboxTracksBroadcastReceiver;
import com.mac.bluebox.view.BboxRecyclerViewWrapper;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_details)
public class DetailsActivity extends RoboActivity {
    @Inject
    BboxTracksBroadcastReceiver broadcastReceiver;

    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BboxTracksBroadcastReceiver.TRACKS_LIST_DISCOVERED);
        registerReceiver(broadcastReceiver, filter); // Don't forget to unregister during onDestroy

        new BboxRecyclerViewWrapper(this, R.id.activity_details_recyclerview, getRecyclerViewAdapter());

        Intent i= new Intent(this, BboxBluetoothService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                requestRetrieveTracks(new Messenger(service));
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        this.bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return broadcastReceiver.getAdapter();
    }


    private void requestRetrieveTracks(Messenger service) {
        Message msg =  Message.obtain(null, BboxBluetoothService.REQUEST_TRACK);
        msg.obj = getIntent().getExtras().get(BluetoothDevice.EXTRA_DEVICE);
        try {
            service.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        unbindService(serviceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }
}
