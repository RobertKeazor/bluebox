package com.mac.bluebox.roboguice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.bluetooth.BboxBroadcastReceiver;
import com.mac.bluebox.bluetooth.BluetoothArrayAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBroadcastReceiverProvider implements Provider<BboxBroadcastReceiver> {
    @Inject
    BluetoothArrayAdapter bluetoothArrayAdapter;

    @Override
    public BboxBroadcastReceiver get() {
        return new BboxBroadcastReceiver(bluetoothArrayAdapter);
    }
}
