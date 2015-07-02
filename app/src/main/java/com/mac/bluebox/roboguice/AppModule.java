package com.mac.bluebox.roboguice;

import android.bluetooth.BluetoothAdapter;

import com.google.inject.AbstractModule;
import com.mac.bluebox.service.MainActivityServiceConnection;
import com.mac.bluebox.view.DevicesRecyclerViewAdapter;
import com.mac.bluebox.view.TracksRecyclerViewAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BluetoothAdapter.class).toProvider(BluetoothAdapterProvider.class);

        bind(DevicesRecyclerViewAdapter.class).toProvider(DevicesRecyclerViewAdapterProvider.class);
        bind(TracksRecyclerViewAdapter.class).toProvider(TracksRecyclerViewAdapterProvider.class);
    }
}
