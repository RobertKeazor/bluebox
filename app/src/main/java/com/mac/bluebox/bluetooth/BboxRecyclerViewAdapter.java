package com.mac.bluebox.bluetooth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.bluebox.R;
import com.mac.bluebox.model.DeviceModel;

import java.util.List;

/**
 * Created by anyer on 6/26/15.
 */
public class BboxRecyclerViewAdapter extends
        RecyclerView.Adapter<BboxRecyclerViewAdapter.BboxRecyclerViewHolder> {

    private List<DeviceModel> devices;
    private final Context context;

    public BboxRecyclerViewAdapter(List<DeviceModel> devices, Context context) {
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
        final DeviceModel device = devices.get(i);
        bboxRecyclerViewHolder.textViewDevice.setText(device.getName());

        bboxRecyclerViewHolder.imageViewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, device.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public List<DeviceModel> getDevices() {
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
