package com.mac.bluebox.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.mac.bluebox.R;

/**
 * Created by anyer on 6/27/15.
 */
public class SwipeRefreshLayoutWrapper {
    private final SwipeRefreshLayout swipeRefreshLayout;

    public SwipeRefreshLayoutWrapper(Activity activity, int resourceId,
                                     final BboxDevicesRecyclerViewAdapter recyclerViewAdapter,
                                     final BluetoothAdapter bluetoothAdapter){

        swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(resourceId);

        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue,
                R.color.orange);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerViewAdapter.getDevices().clear();
                        recyclerViewAdapter.notifyDataSetChanged();
                        bluetoothAdapter.cancelDiscovery();
                        bluetoothAdapter.startDiscovery();
                    }
                });
            }
        });
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }
}
