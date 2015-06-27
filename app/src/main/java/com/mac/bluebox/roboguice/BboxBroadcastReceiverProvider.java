package com.mac.bluebox.roboguice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.bluetooth.BboxBroadcastReceiver;
import com.mac.bluebox.bluetooth.BboxRecyclerViewAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxBroadcastReceiverProvider implements Provider<BboxBroadcastReceiver> {
    @Inject
    BboxRecyclerViewAdapter bboxRecyclerViewAdapter;

    @Override
    public BboxBroadcastReceiver get() {
        return new BboxBroadcastReceiver(bboxRecyclerViewAdapter);
    }
}
