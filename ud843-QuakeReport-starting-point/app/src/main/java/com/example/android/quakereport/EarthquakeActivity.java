/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
   // private ArrayList<Earthquake> earthquakes;
    private ListView earthquakeListView;
    private CustomAdapter adapter;
    private TextView empty_view;
    private ProgressBar spinner;
    private boolean isConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);
        spinner=(ProgressBar)findViewById(R.id.loading_spinner);

        //for empty view
        empty_view=(TextView)findViewById(R.id.tv_empty_text);
        earthquakeListView.setEmptyView(empty_view);


        // Create a fake list of earthquake locations.
          //earthquakes = QueryUtils.extractEarthquakes();
        /*earthquakes.add(new Earthquake("1:30","San Francisco","21-03-2016"));
        */


        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new CustomAdapter(this,new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(adapter);
        //QuakeAsyncTask task=new QuakeAsyncTask();
        //task.execute(USGS_REQUEST_URL);


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
       // earthquakeListView.setAdapter(adapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri=Uri.parse(adapter.getLink(position));
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(uri);
                startActivity(i);
            }
        });
        //for checking network connection
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        isConnected=(info!=null)&&(info.isConnectedOrConnecting());
        if(isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        }
        else {
            spinner.setVisibility(View.GONE);
            empty_view.setText("No internet connection");
        }



    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
       Log.d(LOG_TAG,"creating loader");
        SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude=preference.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String orderBy=preference.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));
        Uri baseUri=Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder=baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);

        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        Log.d(LOG_TAG,"finished loading");
        spinner.setVisibility(View.GONE);

        adapter.clear();
        if(!data.isEmpty()&&data!=null)
        {
            adapter.addAll(data);
        }
        empty_view.setText("Nothing to display");

    }


    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.d(LOG_TAG,"reseting data");
        adapter.clear();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
            Intent i=new Intent(this,SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /* private class QuakeAsyncTask extends AsyncTask<String,Void,List<Earthquake>>{
        @Override
        protected List<Earthquake> doInBackground(String... params) {

            return QueryUtils.fetchEarthquakes(params[0]);
        }

        @Override
        protected void onPostExecute(List<Earthquake> list ) {
            adapter.clear();
            if(!list.isEmpty()&&list!=null)
            {   adapter.addAll(list);
            }
           }
    }*/
    }
