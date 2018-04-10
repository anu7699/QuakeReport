package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by WELLCOME on 30-07-2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>{
    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /** Query URL */
    private String Url;

    public EarthquakeLoader(Context context,String url) {
        super(context);
        Url=url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG,"on start loading");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.d(LOG_TAG,"loading in background");
        if (Url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.

        return QueryUtils.fetchEarthquakes(Url);
    }
}
