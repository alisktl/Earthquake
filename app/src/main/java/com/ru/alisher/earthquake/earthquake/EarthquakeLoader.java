package com.ru.alisher.earthquake.earthquake;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.ru.alisher.earthquake.earthquake.data.Earthquake;
import com.ru.alisher.earthquake.earthquake.network.NetworkUtils;

import java.util.List;

/**
 * Created by alisher on 2/7/18.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String mUrl;
    // Needed from preventing fetching data from internet when device is rotated
    private boolean toReload;

    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
        toReload = true;
    }

    @Override
    protected void onStartLoading() {
        if (toReload) {
            forceLoad();
            toReload = false;
        }
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Earthquake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Earthquake> earthquakes = NetworkUtils.fetchEarthquakeData(mUrl, this.getContext());
        return earthquakes;
    }
}