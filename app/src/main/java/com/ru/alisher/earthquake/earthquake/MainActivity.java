package com.ru.alisher.earthquake.earthquake;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ru.alisher.earthquake.earthquake.data.Earthquake;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final int ID_EARTHQUAKE_LOADER = 1;

    private EarthquakeAdapter mEarthquakeAdapter;

    private RecyclerView mEarthquakesRecyclerView;

    private ProgressBar mLoadingIndicator;

    private int mPosition = RecyclerView.NO_POSITION;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public Loader<List<Earthquake>> onCreateLoader(int loaderId, Bundle bundle) {

        switch (loaderId) {
            case ID_EARTHQUAKE_LOADER:
                return new EarthquakeLoader(this);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        mEarthquakeAdapter.swapEarthquakes(earthquakes);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        mEarthquakesRecyclerView.smoothScrollToPosition(mPosition);
        if (earthquakes.size() != 0)
            showEarthquakeDataView();
        else
            showEmptyStateView(getString(R.string.no_earthquakes));

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
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
}
