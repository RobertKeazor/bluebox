package com.mac.bluebox.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mac.bluebox.R;
import com.mac.bluebox.bluetooth.BboxRecyclerViewAdapter;

/**
 * Created by anyer on 6/27/15.
 */
public class BboxRecyclerViewWrapper {
    private RecyclerView view;

    public BboxRecyclerViewWrapper(Activity activity, int resourceId, BboxRecyclerViewAdapter bboxRecyclerViewAdapter,
                                   BluetoothAdapter bluetoothAdapter) {

        view = (RecyclerView) activity.findViewById(R.id.activity_main_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        view.setLayoutManager(linearLayoutManager);
        view.setAdapter(bboxRecyclerViewAdapter);
    }

    public RecyclerView getView() {
        return view;
    }
}
