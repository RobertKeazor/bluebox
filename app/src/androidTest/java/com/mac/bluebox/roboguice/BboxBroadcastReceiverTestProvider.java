package com.mac.bluebox.roboguice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.bluetooth.BboxDevicesBroadcastReceiver;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBroadcastReceiverTestProvider implements Provider<BboxDevicesBroadcastReceiver> {
    @Inject
    BboxDevicesRecyclerViewAdapter bboxDevicesRecyclerViewAdapter;

    @Override
    public BboxDevicesBroadcastReceiver get() {
        BboxDevicesBroadcastReceiver bboxBroadcastReceiver = mock(BboxDevicesBroadcastReceiver.class);
        when(bboxBroadcastReceiver.getAdapter()).thenReturn(bboxDevicesRecyclerViewAdapter);
        return bboxBroadcastReceiver;
    }
}
