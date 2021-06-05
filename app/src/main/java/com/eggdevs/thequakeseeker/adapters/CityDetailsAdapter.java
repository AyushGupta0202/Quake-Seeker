package com.eggdevs.thequakeseeker.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eggdevs.thequakeseeker.R;
import com.eggdevs.thequakeseeker.data.CityDetails;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CityDetailsAdapter extends RecyclerView.Adapter<CityDetailsAdapter.ViewHolder> {

    private static final String LOCATION_SEPARATOR = " of ";

    private List<CityDetails> earthquake;
    private Context context;
    private Listener listener;

    public CityDetailsAdapter(Context context, List<CityDetails> list) {
        this.context = context;
        this.earthquake = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.earthquake_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        CityDetails currentEarthquake = earthquake.get(position);
        holder.itemView.setTag(earthquake.get(position));

        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());

        // Display the magnitude of the current earthquake in that TextView
        holder.magnitudeView.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) holder.magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(context, currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        String originalLocation, primaryLocation, locationOffset;
        originalLocation = currentEarthquake.getLocation();

        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = context.getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        // Setting primary location.
        holder.primaryLocationView.setText(primaryLocation);

        // Setting location offset.
        holder.locationOffsetView.setText(locationOffset);

        // Create a new Date object for time in milliseconds for earthquake.
        Date dateObject = new Date(currentEarthquake.getTimeInMilliSeconds());

        // Display date.
        String formattedDate = formatDate(dateObject);
        holder.dateView.setText(formattedDate);

        // Display Time.
        String formattedTime = formatTime(dateObject);
        holder.timeView.setText(formattedTime);

        // Felt By.
        String feltBy = currentEarthquake.getFeltBy();
        feltBy = context.getString(R.string.felt_by_people) + " " + getFeltByCount(feltBy);
        holder.feltByView.setText(feltBy);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return earthquake.size();
    }

    private int getMagnitudeColor(Context context, double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;

            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;

            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;

            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;

            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;

            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;

            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;

            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;

            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;

            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {

        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    /**
     * Return the formatted date string ie. Mar 3, 1994.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string ie. 4:30 PM.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String getFeltByCount(String s) {
        if (s.equals("null")) {
            return "0";
        } else {
            return s;
        }
    }

    public interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView locationOffsetView, primaryLocationView, magnitudeView, dateView, timeView, feltByView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Assign variables.
            locationOffsetView = itemView.findViewById(R.id.location_offset);
            primaryLocationView = itemView.findViewById(R.id.primary_location);
            magnitudeView = itemView.findViewById(R.id.magnitude);
            dateView = itemView.findViewById(R.id.date);
            timeView = itemView.findViewById(R.id.time);
            feltByView = itemView.findViewById(R.id.felt_by);
        }
    }
}
