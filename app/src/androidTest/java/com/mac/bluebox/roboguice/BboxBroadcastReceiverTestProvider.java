package com.mac.bluebox.roboguice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.receivers.DevicesBroadcastReceiver;
import com.mac.bluebox.view.DevicesRecyclerViewAdapter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBroadcastReceiverTestProvider implements Provider<DevicesBroadcastReceiver> {
    @Inject
    DevicesRecyclerViewAdapter devicesRecyclerViewAdapter;

    @Override
    public DevicesBroadcastReceiver get() {
        DevicesBroadcastReceiver bboxBroadcastReceiver = mock(DevicesBroadcastReceiver.class);
        when(bboxBroadcastReceiver.getAdapter()).thenReturn(devicesRecyclerViewAdapter);
        return bboxBroadcastReceiver;
    }
}
