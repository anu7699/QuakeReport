package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Created by WELLCOME on 26-07-2017.
 */

public final class QueryUtils {
    /** Sample JSON response for a USGS query */
    private static String JSON_RESPONSE ="";
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     *
     */
    public QueryUtils()
    {

    }
  /*  public QueryUtils(String JSON_response) {
        this.JSON_RESPONSE=JSON_response;
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();


        // Try to parse the JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject root=new JSONObject(JSON_RESPONSE);
            JSONArray features=root.getJSONArray("features");


            for(int i=0;i<features.length();i++)
            {
                JSONObject feature=features.getJSONObject(i);
                JSONObject properties=feature.getJSONObject("properties");
                double mag=properties.getDouble("mag");
                String location=properties.getString("place");
                long timems=properties.getLong("time");
                String url=properties.getString("url");

                Earthquake temp=new Earthquake(mag,location,timems,url);
                earthquakes.add(temp);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static ArrayList<Earthquake> fetchEarthquakes(String url1)
    { try {
        Thread.sleep(1000);
    }catch (Exception e)
    {
        e.printStackTrace();
    }
        URL url=createUrl(url1);
        if(url.equals(null))
            return null;
        String JSONresp="";
        try{
            JSONresp=makeHttpConn(url);
        }catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        JSON_RESPONSE=JSONresp;
        Log.d(LOG_TAG,"fetching earthquake data");
        return extractEarthquakes();
    }
    private static URL createUrl(String usgsRequestUrl) {

        URL url=null;
        try{
            url=new URL(usgsRequestUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"error creating url ",e);
            return null;
        }
        return url;
    }

    private static String makeHttpConn(URL url)throws IOException {
        String JSON_resp="";
        if(url==null)
            return JSON_resp;

        HttpURLConnection urlconnetion=null;
        InputStream is=null;
        try{
            urlconnetion=(HttpURLConnection)url.openConnection();
            urlconnetion.setRequestMethod("GET");
            urlconnetion.setReadTimeout(10000);
            urlconnetion.setConnectTimeout(15000);
            urlconnetion.connect();
            if(urlconnetion.getResponseCode()!=200)
            {
                Log.e(LOG_TAG,"http request code not 200");
                return ("");
            }

            is=urlconnetion.getInputStream();
            JSON_resp=readFromStream(is);
        }catch (IOException e){
            Log.e(LOG_TAG,"error occured while connecting ",e);
        }
        finally {
            if(urlconnetion!=null)
                urlconnetion.disconnect();
            if(is!=null)
                is.close();
        }
        return JSON_resp;
    }

    private static String readFromStream(InputStream is) throws IOException{
        StringBuilder output=new StringBuilder();
        if(is!=null) {
            InputStreamReader reader = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader buffer_reader = new BufferedReader(reader);


            String line = buffer_reader.readLine();
            while (line != null) {
                output.append(line);
                line = buffer_reader.readLine();
            }
        }
        return output.toString();


    }


}
