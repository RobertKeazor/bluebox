package com.mac.bluebox.roboguice;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.bluetooth.BboxRecyclerViewAdapter;
import com.mac.bluebox.model.DeviceModel;

import java.util.ArrayList;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxRecyclerViewAdapterProvider implements Provider<BboxRecyclerViewAdapter> {
    @Inject
    Context context;

    @Override
    public BboxRecyclerViewAdapter get() {
        return new BboxRecyclerViewAdapter(new ArrayList<DeviceModel>(){}, context);
    }
}
