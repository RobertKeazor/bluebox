package com.mac.bluebox.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mac.bluebox.view.TracksRecyclerViewAdapter;

import java.util.Arrays;


/**
 * Created by anyer on 6/26/15.
 */
public class TracksBroadcastReceiver extends BroadcastReceiver {
    private TracksRecyclerViewAdapter adapter;

    private static final String TAG = TracksBroadcastReceiver.class.getName();
    public static final String TRACKS_LIST_DISCOVERED = "TRACKS_LIST_DISCOVERED";
    public static final String EXTRA_TRACKS = "EXTRA_TRACKS";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.e(TAG, "Broadcast Action Received: " + action);
        if(TracksBroadcastReceiver.TRACKS_LIST_DISCOVERED.equals(action)){
            String data = (String) intent.getExtras().get(TracksBroadcastReceiver.EXTRA_TRACKS);
            String[] tracks = data.split(",");

            adapter.getTracks().addAll(Arrays.asList(tracks));
            adapter.notifyDataSetChanged();
            Log.e(TAG, "DataSetChanged Items: " + tracks.length);
        }
    }

    public void setup(TracksRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
}
