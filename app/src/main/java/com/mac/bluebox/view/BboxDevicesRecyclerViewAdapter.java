package com.mac.bluebox.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.bluebox.DetailsActivity;
import com.mac.bluebox.R;

import java.util.List;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxDevicesRecyclerViewAdapter extends
        RecyclerView.Adapter<BboxDevicesRecyclerViewAdapter.BboxRecyclerViewHolder> {

    private static final String DEVICE_MAC = "DEVICE_MAC";
    private static final String DEVICE_NAME = "DEVICE_NAME";
    private List<BluetoothDevice> devices;
    private final Context context;

    public BboxDevicesRecyclerViewAdapter(List<BluetoothDevice> devices, Context context) {
        this.devices = devices;
        this.context = context;
    }

    @Override
    public BboxRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);

        return new BboxRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BboxRecyclerViewHolder bboxRecyclerViewHolder, int i) {
        final BluetoothDevice device = devices.get(i);
        bboxRecyclerViewHolder.textViewDevice.setText(device.getName());

       bboxRecyclerViewHolder.imageViewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, device.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), DetailsActivity.class);

                intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public List<BluetoothDevice> getDevices() {
        return devices;
    }

    public static class BboxRecyclerViewHolder extends RecyclerView.ViewHolder {
        protected TextView textViewDevice;
        protected ImageView imageViewDevice;

        public BboxRecyclerViewHolder(View v) {
            super(v);

            textViewDevice =  (TextView) v.findViewById(R.id.card_view_textview_device);
            imageViewDevice = (ImageView) v.findViewById(R.id.card_view_image_device);
        }
    }
}
