package com.mac.bluebox.bluetooth;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.inject.Inject;

import java.util.List;

/**
 * Created by anyer on 6/26/15.
 */
public class BluetoothArrayAdapter extends ArrayAdapter {
    public BluetoothArrayAdapter(Context context, int resource, List<String> devices) {
        super(context, resource, devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
