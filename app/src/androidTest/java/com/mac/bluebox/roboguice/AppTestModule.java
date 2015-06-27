package com.mac.bluebox.roboguice;

import android.content.Context;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.mac.bluebox.bluetooth.BboxBroadcastReceiver;
import com.mac.bluebox.bluetooth.BluetoothArrayAdapter;

/**
 * Created by anyer on 6/26/15.
 */
public class AppTestModule implements Module {
    private Context context;

    public AppTestModule(Context context){
        this.context = context;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Context.class).toInstance(context);
        binder.bind(BluetoothArrayAdapter.class).toProvider(BluetoothArrayAdapterProvider.class);
        binder.bind(BboxBroadcastReceiver.class)
                .toProvider(BboxBroadcastReceiverTestProvider.class);
    }
}
