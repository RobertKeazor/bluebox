package com.mac.bluebox.roboguice;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxDevicesRecyclerViewAdapterProvider implements Provider<BboxDevicesRecyclerViewAdapter> {
    @Inject
    Context context;

    @Override
    public BboxDevicesRecyclerViewAdapter get() {
        return new BboxDevicesRecyclerViewAdapter(new ArrayList<BluetoothDevice>(){}, context);
    }
}
