package com.mac.bluebox.roboguice;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mac.bluebox.view.TracksRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by anyer on 6/26/15.
 */
public class TracksRecyclerViewAdapterProvider implements Provider<TracksRecyclerViewAdapter> {
    @Inject
    Context context;

    @Override
    public TracksRecyclerViewAdapter get() {
        return new TracksRecyclerViewAdapter(new ArrayList<String>(){}, context);
    }
}
