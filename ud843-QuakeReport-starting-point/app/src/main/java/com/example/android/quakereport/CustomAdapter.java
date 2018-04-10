package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by WELLCOME on 25-07-2017.
 */

public class CustomAdapter extends ArrayAdapter<Earthquake> {
    private static final String LOCATION_SEPARATOR = " of ";
    public CustomAdapter(Activity context,ArrayList<Earthquake> earthquakes) {
        super(context,0,earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView =convertView;
        if(listItemView==null)
        {
            listItemView=LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Earthquake currentRecord=getItem(position);
        TextView text_magnitude=(TextView)listItemView.findViewById(R.id.tv_magnitude);
        text_magnitude.setText(formatMag(currentRecord.getMagnitude()));
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle=(GradientDrawable)text_magnitude.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentRecord.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        TextView text_offset_location=(TextView)listItemView.findViewById(R.id.tv_offset_loc);
        text_offset_location.setText(getOffset(currentRecord.getLocation()));
        TextView text_prime_location=(TextView)listItemView.findViewById(R.id.tv_prime_loc);
        text_prime_location.setText(getPrime(currentRecord.getLocation()));
        Date dateObj=new Date(currentRecord.getTimems());

        TextView text_time=(TextView)listItemView.findViewById(R.id.tv_time);
        text_time.setText(formattime(dateObj));
        TextView text_date=(TextView)listItemView.findViewById(R.id.tv_date);
        text_date.setText(formatdate(dateObj));
        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int colorID= (int)Math.floor(magnitude);
        switch (colorID)
        {
            case 0:
            case 1: return ContextCompat.getColor(getContext(), R.color.magnitude1);
            case 2: return ContextCompat.getColor(getContext(), R.color.magnitude2);
            case 3: return ContextCompat.getColor(getContext(), R.color.magnitude3);
            case 4: return ContextCompat.getColor(getContext(), R.color.magnitude4);
            case 5: return ContextCompat.getColor(getContext(), R.color.magnitude5);
            case 6: return ContextCompat.getColor(getContext(), R.color.magnitude6);
            case 7: return ContextCompat.getColor(getContext(), R.color.magnitude7);
            case 8: return ContextCompat.getColor(getContext(), R.color.magnitude8);
            case 9: return ContextCompat.getColor(getContext(), R.color.magnitude9);
            case 10: return ContextCompat.getColor(getContext(), R.color.magnitude10plus);
            default: return ContextCompat.getColor(getContext(), R.color.magnitude10plus);
        }

    }
    //to have one digit after decimal
    private String formatMag(double magnitude) {
        DecimalFormat dm=new DecimalFormat("0.0");
        return dm.format(magnitude);
    }

    private String getPrime(String location) {
        if(!location.contains(LOCATION_SEPARATOR))
        {
            return(location);
        }
        return (location.split(LOCATION_SEPARATOR)[1]);
    }

    private String getOffset(String location) {
        if(!location.contains(LOCATION_SEPARATOR))
        {
            return("Near the");
        }
        return (location.split(LOCATION_SEPARATOR)[0]+LOCATION_SEPARATOR);
    }

    private String formattime(Date dateObj) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObj);
    }
    private  String formatdate(Date dateObj)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObj);
    }
    public   String getLink(int position)
    {
        return (getItem(position).getUrl());
    }
}
