package com.mac.bluebox.roboguice;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;
import com.mac.bluebox.view.BboxTracksRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxTracksRecyclerViewAdapterProvider implements Provider<BboxTracksRecyclerViewAdapter> {
    @Inject
    Context context;

    @Override
    public BboxTracksRecyclerViewAdapter get() {
        return new BboxTracksRecyclerViewAdapter(new ArrayList<String>(){}, context);
    }
}
