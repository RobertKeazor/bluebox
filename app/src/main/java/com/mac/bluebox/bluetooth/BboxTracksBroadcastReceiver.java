package com.mac.bluebox.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;
import com.mac.bluebox.view.BboxTracksRecyclerViewAdapter;

import java.io.Serializable;
import java.util.List;


/**
 * Created by anyer on 6/26/15.
 */
public class BboxTracksBroadcastReceiver extends BroadcastReceiver {
    private BboxTracksRecyclerViewAdapter adapter;

    private static final String TAG = BboxTracksBroadcastReceiver.class.getName();
    public static final String TRACKS_LIST_DISCOVERED = "TRACKS_LIST_DISCOVERED";
    public static final String EXTRA_TRACKS = "EXTRA_TRACKS";

    public BboxTracksBroadcastReceiver(BboxTracksRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.e(TAG, "Broadcast Action Received: " + action);
        if(BboxTracksBroadcastReceiver.TRACKS_LIST_DISCOVERED.equals(action)){
            List<String> tracks = (List<String>) intent.getSerializableExtra(BboxTracksBroadcastReceiver.EXTRA_TRACKS);
            adapter.getTracks().addAll(tracks);
            adapter.notifyDataSetChanged();
            Log.e(TAG, "DataSetChanged Items: " + tracks.size());
        }
    }

    public BboxTracksRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
