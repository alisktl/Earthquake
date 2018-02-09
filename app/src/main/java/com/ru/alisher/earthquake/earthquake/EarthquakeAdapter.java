package com.ru.alisher.earthquake.earthquake;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ru.alisher.earthquake.earthquake.data.Earthquake;

import java.util.List;

/**
 * Created by alisher on 2/7/18.
 */

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeAdapterViewHolder> {

    private static final int VIEW_TYPE_EARTHQUAKE = 0;

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    private List<Earthquake> earthquakes;

    /**
     * Creates a EarthquakeAdapter.
     *
     * @param context Used to talk to the UI and app resources
     */
    public EarthquakeAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public EarthquakeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;

        switch (viewType) {

            case VIEW_TYPE_EARTHQUAKE: {
                layoutId = R.layout.earthquake_list_item;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);

        return new EarthquakeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EarthquakeAdapterViewHolder earthquakeAdapterViewHolder, int position) {
        Earthquake earthquakeToShow = earthquakes.get(position);

        earthquakeAdapterViewHolder.magnitude.setText("7.5");

        earthquakeAdapterViewHolder.time.setText("3PM (2 hrs ago)");

        if (position == 0)
            earthquakeAdapterViewHolder.location.setText("Kazakhstan Kazakhstan Kazakhstan Kazakhstan Kazakhstan Kazakhstan Kazakhstan Kazakhstan, Almaty");
        else
            earthquakeAdapterViewHolder.location.setText("Kazakhstan, Almaty");
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our earthquakes
     */
    @Override
    public int getItemCount() {
        if (earthquakes == null) return 0;
        else return earthquakes.size();
    }

    /**
     * This method is called by MainActivity after a load has finished, as well as when the Loader
     * responsible for loading the earthquake data is reset. When this method is called, we assume we
     * have a completely new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param earthquakes the new list of earthquakes to use as EarthquakeAdapter's data source
     */
    void swapEarthquakes(List<Earthquake> earthquakes) {
        this.earthquakes = earthquakes;
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a earthquake item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class EarthquakeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView location;
        final TextView time;
        final TextView magnitude;

        EarthquakeAdapterViewHolder(View view) {
            super(view);

            location = view.findViewById(R.id.location);
            time = view.findViewById(R.id.time);
            magnitude = view.findViewById(R.id.magnitude);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
