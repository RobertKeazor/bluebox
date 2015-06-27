package com.mac.bluebox.roboguice;

import android.content.Context;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.mac.bluebox.bluetooth.BboxDevicesBroadcastReceiver;
import com.mac.bluebox.view.BboxDevicesRecyclerViewAdapter;

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
        binder.bind(BboxDevicesRecyclerViewAdapter.class).toProvider(BboxDevicesRecyclerViewAdapterProvider.class);
        binder.bind(BboxDevicesBroadcastReceiver.class)
                .toProvider(BboxBroadcastReceiverTestProvider.class);
    }
}
