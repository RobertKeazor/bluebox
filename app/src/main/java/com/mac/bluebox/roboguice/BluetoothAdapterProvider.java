package com.mac.bluebox.roboguice;

import android.bluetooth.BluetoothAdapter;

import com.google.inject.Provider;

/**
 * Created by anyer on 6/27/15.
 */
public class BluetoothAdapterProvider implements Provider<BluetoothAdapter> {
    @Override
    public BluetoothAdapter get() {
        return BluetoothAdapter.getDefaultAdapter();
    }
}
