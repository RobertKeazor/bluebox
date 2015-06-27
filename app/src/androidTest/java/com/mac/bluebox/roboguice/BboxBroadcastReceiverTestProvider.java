package com.mac.bluebox.roboguice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.bluetooth.BboxBroadcastReceiver;
import com.mac.bluebox.bluetooth.BluetoothArrayAdapter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBroadcastReceiverTestProvider implements Provider<BboxBroadcastReceiver> {
    @Inject
    BluetoothArrayAdapter bluetoothArrayAdapter;

    @Override
    public BboxBroadcastReceiver get() {
        BboxBroadcastReceiver bboxBroadcastReceiver = mock(BboxBroadcastReceiver.class);
        when(bboxBroadcastReceiver.getAdapter()).thenReturn(bluetoothArrayAdapter);
        return bboxBroadcastReceiver;
    }
}
