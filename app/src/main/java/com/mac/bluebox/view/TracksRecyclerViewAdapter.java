package com.mac.bluebox.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mac.bluebox.activity.DetailsActivity;
import com.mac.bluebox.R;

import java.util.List;

/**
 * Created by anyer on 6/26/15.
 */
public class TracksRecyclerViewAdapter extends
        RecyclerView.Adapter<TracksRecyclerViewAdapter.BboxRecyclerViewHolder> {

    private static final String TAG = TracksRecyclerViewAdapter.class.getName();
    private final Context context;
    private final List<String> tracks;
    private DetailsActivity activity = null;

    public TracksRecyclerViewAdapter(List<String> tracks, Context context) {
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
    public void onBindViewHolder(BboxRecyclerViewHolder bboxRecyclerViewHolder, final int i) {
        final String track = tracks.get(i);

        bboxRecyclerViewHolder.textViewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity != null) {
                    activity.playTrack(track);
                    Log.e(TAG, "TRACK ITEM CLICKED: " + track);
                }
            }
        });

        bboxRecyclerViewHolder.textViewDevice.setText(track + ":" + track.length());
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public List<String> getTracks() {
        return tracks;
    }

    public void setActivity(DetailsActivity activity) {
        this.activity = activity;
    }

    public static class BboxRecyclerViewHolder extends RecyclerView.ViewHolder {
        protected TextView textViewDevice;

        public BboxRecyclerViewHolder(View v) {
            super(v);

            textViewDevice =  (TextView) v.findViewById(R.id.card_view_textview_device);
            textViewDevice.setClickable(true);
        }
    }
}
