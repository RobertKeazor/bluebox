package com.mac.bluebox;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;

import com.google.inject.Inject;
import com.mac.bluebox.bluetooth.BboxBluetoothService;
import com.mac.bluebox.bluetooth.BboxDevicesBroadcastReceiver;
import com.mac.bluebox.bluetooth.BboxTracksBroadcastReceiver;
import com.mac.bluebox.view.BboxRecyclerViewWrapper;
import com.mac.bluebox.view.BboxTracksRecyclerViewAdapter;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_details)
public class DetailsActivity extends RoboActivity {
    @Inject
    BboxTracksBroadcastReceiver broadcastReceiver;

    private ServiceConnection connection;
    private BboxTracksRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //      Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter); // Don't forget to unregister during onDestroy

        new BboxTracksRecyclerViewAdapter(new ArrayList<String>(), this);
        new BboxRecyclerViewWrapper(this, R.id.activity_details_recyclerview, getRecyclerViewAdapter());

        Intent i= new Intent(this, BboxBluetoothService.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                requestRetrieveTracks(new Messenger(service));
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        this.bindService(i, connection, Context.BIND_AUTO_CREATE);
    }

    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }


    private void requestRetrieveTracks(Messenger service) {
        Message msg =  Message.obtain(null, BboxBluetoothService.MSG_SAY_HELLO);
        try {
            service.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
