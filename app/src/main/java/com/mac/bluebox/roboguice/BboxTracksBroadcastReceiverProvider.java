package com.mac.bluebox.roboguice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.bluetooth.BboxDevicesBroadcastReceiver;
import com.mac.bluebox.bluetooth.BboxTracksBroadcastReceiver;
import com.mac.bluebox.view.BboxTracksRecyclerViewAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxTracksBroadcastReceiverProvider implements Provider<BboxTracksBroadcastReceiver> {
    @Inject
    BboxTracksRecyclerViewAdapter bboxTracksRecyclerViewAdapter;

    @Override
    public BboxTracksBroadcastReceiver get() {
        return new BboxTracksBroadcastReceiver(bboxTracksRecyclerViewAdapter);
    }
}
