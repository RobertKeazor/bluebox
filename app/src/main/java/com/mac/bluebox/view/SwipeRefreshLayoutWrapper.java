package com.mac.bluebox.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.mac.bluebox.R;

import java.util.Set;

/**
 * Created by anyer on 6/27/15.
 */
public class SwipeRefreshLayoutWrapper {
    private static final String TAG = SwipeRefreshLayoutWrapper.class.getName();
    private final SwipeRefreshLayout mSwipeRefreshLayout;
    private final BboxDevicesRecyclerViewAdapter mRecyclerViewAdapter;
    private final BluetoothAdapter mBluetoothAdapter;

    public SwipeRefreshLayoutWrapper(Activity activity, int resourceId,
                                     final BboxDevicesRecyclerViewAdapter recyclerViewAdapter,
                                     final BluetoothAdapter bluetoothAdapter){
        this.mRecyclerViewAdapter = recyclerViewAdapter;
        this.mBluetoothAdapter = bluetoothAdapter;

        mSwipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(resourceId);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue,
                R.color.orange);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        refreshDevices();
                    }
                });
            }
        });
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void refreshDevices() {
        mRecyclerViewAdapter.getDevices().clear();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mRecyclerViewAdapter.getDevices().add(device);

                Log.e(TAG, "PAIRED DEVICE NAME: " + device.getName());
            }
        }

        mRecyclerViewAdapter.notifyDataSetChanged();
    }
}
