package com.mac.bluebox.roboguice;

import android.app.Application;

import com.google.inject.AbstractModule;
import com.mac.bluebox.bluetooth.BboxBroadcastReceiver;
import com.mac.bluebox.bluetooth.BluetoothArrayAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BluetoothArrayAdapter.class).toProvider(BluetoothArrayAdapterProvider.class);
        bind(BboxBroadcastReceiver.class).toProvider(BboxBroadcastReceiverProvider.class);
    }
}