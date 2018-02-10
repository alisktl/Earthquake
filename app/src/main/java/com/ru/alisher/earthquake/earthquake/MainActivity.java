package com.ru.alisher.earthquake.earthquake;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ru.alisher.earthquake.earthquake.data.Earthquake;
import com.ru.alisher.earthquake.earthquake.helper.Helper;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_URL_API_RESOURCE_KEY = "usgs_api_url";

    private static final int ID_EARTHQUAKE_LOADER = 1;

    private EarthquakeAdapter mEarthquakeAdapter;

    private RecyclerView mEarthquakesRecyclerView;

    private ProgressBar mLoadingIndicator;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(LOG_TAG, "onCreate");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        mEarthquakesRecyclerView = findViewById(R.id.recyclerview_earthquakes);

        mEmptyStateTextView = findViewById(R.id.empty_view);

        mLoadingIndicator = findViewById(R.id.loading_indicator);

        /**
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list.
         */
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        // Adding divider between earthquake items.
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mEarthquakesRecyclerView.getContext(),
                layoutManager.getOrientation()
        );
        mEarthquakesRecyclerView.addItemDecoration(mDividerItemDecoration);


        // setLayoutManager associates the LayoutManager we created above with our RecyclerView.
        mEarthquakesRecyclerView.setLayoutManager(layoutManager);

        /**
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView.
         */
        mEarthquakesRecyclerView.setHasFixedSize(true);

        mEarthquakeAdapter = new EarthquakeAdapter(this);
        mEarthquakesRecyclerView.setAdapter(mEarthquakeAdapter);

        showLoading();

        if (isDeviceConnectedToNetwork()) {
            getLoaderManager().initLoader(ID_EARTHQUAKE_LOADER, null, this);
        } else {
            showEmptyStateView(getString(R.string.no_internet_connection));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reload) {
            refreshEarthquakeData();

            return true;
        } else if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        refreshEarthquakeData();
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int loaderId, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader");
        switch (loaderId) {
            case ID_EARTHQUAKE_LOADER:
                String usgsApiUrl = Helper.getConfigValue(this, USGS_URL_API_RESOURCE_KEY);

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                String minMagnitude = sharedPrefs.getString(
                        getString(R.string.settings_min_magnitude_key),
                        getString(R.string.settings_min_magnitude_default));

                String orderBy = sharedPrefs.getString(
                        getString(R.string.settings_order_by_key),
                        getString(R.string.settings_order_by_default)
                );

                String limitOfEarthquakes = sharedPrefs.getString(
                        getString(R.string.settings_limit_of_earthquakes_key),
                        getString(R.string.settings_limit_of_earthquakes_default)
                );

                Uri baseUri = Uri.parse(usgsApiUrl);
                Uri.Builder uriBuilder = baseUri.buildUpon();

                uriBuilder.appendQueryParameter("format", "geojson");
                uriBuilder.appendQueryParameter("limit", limitOfEarthquakes);
                uriBuilder.appendQueryParameter("minmag", minMagnitude);
                uriBuilder.appendQueryParameter("orderby", orderBy);

                return new EarthquakeLoader(this, uriBuilder.toString());
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.v(LOG_TAG, "onLoadFinished");
        mEarthquakeAdapter.swapEarthquakes(earthquakes);

        if (earthquakes != null && earthquakes.size() != 0)
            showEarthquakeDataView();
        else
            showEmptyStateView(getString(R.string.no_earthquakes));
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.v(LOG_TAG, "onLoaderReset");
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */
        mEarthquakeAdapter.swapEarthquakes(null);
    }

    private boolean isDeviceConnectedToNetwork() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * This method will make the loading indicator visible and hide the earthquakes View
     * and error message.
     */
    private void showLoading() {
        // First, hide the earthquake data
        mEarthquakesRecyclerView.setVisibility(View.INVISIBLE);

        // Hide the emptyStateTextView
        mEmptyStateTextView.setVisibility(View.INVISIBLE);

        // Then, show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the View for the earthquakes data visible and hide the error message
     * and loading indicator.
     */
    private void showEarthquakeDataView() {
        // First, hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        // Hide the emptyStateTextView
        mEmptyStateTextView.setVisibility(View.INVISIBLE);

        // Then, make sure the earthquake data is visible
        mEarthquakesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showEmptyStateView(String emptyText) {
        // First, hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        // Hide the earthquake data
        mEarthquakesRecyclerView.setVisibility(View.INVISIBLE);

        // Show the emptyStateTextView
        mEmptyStateTextView.setText(emptyText);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
    }

    private void refreshEarthquakeData() {
        showLoading();

        // Scrolling up
        mEarthquakesRecyclerView.scrollToPosition(0);

        if (isDeviceConnectedToNetwork()) {
            getLoaderManager().restartLoader(ID_EARTHQUAKE_LOADER, null, this);
        } else {
            showEmptyStateView(getString(R.string.no_internet_connection));
        }
    }
}
