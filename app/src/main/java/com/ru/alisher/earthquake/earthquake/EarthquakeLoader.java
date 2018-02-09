package com.ru.alisher.earthquake.earthquake;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.ru.alisher.earthquake.earthquake.data.Earthquake;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alisher on 2/7/18.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public EarthquakeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Earthquake> loadInBackground() {
        List<Earthquake> earthquakes = new ArrayList<>();
        Earthquake earthquake1 = new Earthquake("1", 7.5, 5.5, "green", "1.5",
                "5.6", "Kazakhstan, Almaty", "", 243434, "");
        Earthquake earthquake2 = new Earthquake("1", 7.5, 5.5, "green", "1.5",
                "5.6", "Kazakhstan, Almaty", "", 243434, "");
        Earthquake earthquake3 = new Earthquake("1", 7.5, 5.5, "green", "1.5",
                "5.6", "Kazakhstan, Almaty", "", 243434, "");
        Earthquake earthquake4 = new Earthquake("1", 7.5, 5.5, "green", "1.5",
                "5.6", "Kazakhstan, Almaty", "", 243434, "");
        Earthquake earthquake5 = new Earthquake("1", 7.5, 5.5, "green", "1.5",
                "5.6", "Kazakhstan, Almaty", "", 243434, "");

        earthquakes.add(earthquake1);
        earthquakes.add(earthquake2);
        earthquakes.add(earthquake3);
        earthquakes.add(earthquake4);
        earthquakes.add(earthquake5);
        return earthquakes;
    }
}