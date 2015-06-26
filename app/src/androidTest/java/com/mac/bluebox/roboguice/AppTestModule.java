package com.mac.bluebox.roboguice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.mac.bluebox.bluetooth.BlueboxBluetoothAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class AppTestModule implements Module {
    private BlueboxBluetoothAdapter mockBlueboxBluetoothAdapter;

    public AppTestModule(BlueboxBluetoothAdapter mockBlueboxBluetoothAdapter) {
        this.mockBlueboxBluetoothAdapter = mockBlueboxBluetoothAdapter;
    }

    @Override
    public void configure(Binder binder) {
       binder.bind(BlueboxBluetoothAdapter.class)
               .toInstance(mockBlueboxBluetoothAdapter);
    }
}
