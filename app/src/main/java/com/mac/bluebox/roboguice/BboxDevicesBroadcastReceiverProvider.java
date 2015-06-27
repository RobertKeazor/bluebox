package com.mac.bluebox.roboguice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.bluetooth.BboxDevicesBroadcastReceiver;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxDevicesBroadcastReceiverProvider implements Provider<BboxDevicesBroadcastReceiver> {
    @Inject
    BboxDevicesRecyclerViewAdapter bboxDevicesRecyclerViewAdapter;

    @Override
    public BboxDevicesBroadcastReceiver get() {
        return new BboxDevicesBroadcastReceiver(bboxDevicesRecyclerViewAdapter);
    }
}
