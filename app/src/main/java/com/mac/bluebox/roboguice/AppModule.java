package com.mac.bluebox.roboguice;

import android.bluetooth.BluetoothAdapter;

import com.google.inject.AbstractModule;
import com.mac.bluebox.bluetooth.BboxBluetoothService;
import com.mac.bluebox.bluetooth.BboxTracksBroadcastReceiver;
import com.mac.bluebox.bluetooth.IncommingMessagesHandler;
import com.mac.bluebox.bluetooth.MainActivityServiceConnection;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;
import com.mac.bluebox.view.BboxTracksRecyclerViewAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BluetoothAdapter.class).toProvider(BluetoothAdapterProvider.class);

        bind(BboxDevicesRecyclerViewAdapter.class).toProvider(BboxDevicesRecyclerViewAdapterProvider.class);
//        bind(BboxDevicesBroadcastReceiver.class).toProvider(BboxDevicesBroadcastReceiverProvider.class);

        bind(BboxTracksRecyclerViewAdapter.class).toProvider(BboxTracksRecyclerViewAdapterProvider.class);
        bind(BboxTracksBroadcastReceiver.class).toProvider(BboxTracksBroadcastReceiverProvider.class);

        bind(MainActivityServiceConnection.class);

        bind(IncommingMessagesHandler.class);
    }
}
