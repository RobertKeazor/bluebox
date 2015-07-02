package com.mac.bluebox.roboguice;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.view.DevicesRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by anyer on 6/26/15.
 */
public class DevicesRecyclerViewAdapterProvider implements Provider<DevicesRecyclerViewAdapter> {
    @Inject
    Context context;

    @Override
    public DevicesRecyclerViewAdapter get() {
        return new DevicesRecyclerViewAdapter(new ArrayList<BluetoothDevice>(){}, context);
    }
}
