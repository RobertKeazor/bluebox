package com.mac.bluebox.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mac.bluebox.R;

/**
 * Created by anyer on 6/27/15.
 */
public class BboxRecyclerViewWrapper {
    private RecyclerView view;

    public BboxRecyclerViewWrapper(Activity activity, int resourceId,  RecyclerView.Adapter bboxDevicesRecyclerViewAdapter) {

        view = (RecyclerView) activity.findViewById(resourceId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        view.setLayoutManager(linearLayoutManager);
        view.setAdapter(bboxDevicesRecyclerViewAdapter);
    }
}
