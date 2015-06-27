package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;
import com.mac.bluebox.view.BboxTracksRecyclerViewAdapter;

import java.io.Serializable;
import java.util.List;


/**
 * Created by anyer on 6/26/15.
 */
public class BboxTracksBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = BboxTracksBroadcastReceiver.class.getName();
    public static final String TRACKS_LIST_DISCOVERED = "TRACKS_LIST_DISCOVERED";
    public static final String EXTRA_TRACKS = "EXTRA_TRACKS";
    private BboxTracksRecyclerViewAdapter adapter;

    public BboxTracksBroadcastReceiver(BboxTracksRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // When discovery finds a device

        if(BboxTracksBroadcastReceiver.TRACKS_LIST_DISCOVERED.equals(action)){
            List<String> tracks = (List<String>) intent.getSerializableExtra(BboxTracksBroadcastReceiver.EXTRA_TRACKS);
            adapter.getTracks().addAll(tracks);
            adapter.notifyDataSetChanged();
        }
    }

    public BboxTracksRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
