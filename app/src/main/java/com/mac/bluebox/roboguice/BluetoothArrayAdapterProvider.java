package com.mac.bluebox.roboguice;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.bluetooth.BluetoothArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by anyer on 6/26/15.
 */
public class BluetoothArrayAdapterProvider implements Provider<BluetoothArrayAdapter> {
    @Inject
    Context context;

    @Override
    public BluetoothArrayAdapter get() {
        return new BluetoothArrayAdapter(context, android.R.layout.simple_list_item_1,
                new ArrayList<String>());
    }
}
