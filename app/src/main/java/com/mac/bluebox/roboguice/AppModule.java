package com.mac.bluebox.roboguice;

import android.bluetooth.BluetoothAdapter;

import com.google.inject.AbstractModule;
import com.mac.bluebox.bluetooth.BboxBroadcastReceiver;
import com.mac.bluebox.bluetooth.BboxRecyclerViewAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BluetoothAdapter.class).toProvider(BluetoothAdapterProvider.class);
        bind(BboxRecyclerViewAdapter.class).toProvider(BboxRecyclerViewAdapterProvider.class);
        bind(BboxBroadcastReceiver.class).toProvider(BboxBroadcastReceiverProvider.class);
    }
}