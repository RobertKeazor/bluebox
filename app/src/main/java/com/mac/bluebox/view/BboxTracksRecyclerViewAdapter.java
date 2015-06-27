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
public class BboxTracksRecyclerViewAdapter extends
        RecyclerView.Adapter<BboxTracksRecyclerViewAdapter.BboxRecyclerViewHolder> {

    private final Context context;
    private final List<String> tracks;

    public BboxTracksRecyclerViewAdapter(List<String> tracks, Context context) {
        this.tracks = tracks;
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
        final String track = tracks.get(i);
        bboxRecyclerViewHolder.textViewDevice.setText(track);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public List<String> getTracks() {
        return tracks;
    }

    public static class BboxRecyclerViewHolder extends RecyclerView.ViewHolder {
        protected TextView textViewDevice;

        public BboxRecyclerViewHolder(View v) {
            super(v);

            textViewDevice =  (TextView) v.findViewById(R.id.card_view_textview_device);
        }
    }
}
