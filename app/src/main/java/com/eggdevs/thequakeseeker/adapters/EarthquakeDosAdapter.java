package com.eggdevs.thequakeseeker.adapters;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eggdevs.thequakeseeker.R;
import com.eggdevs.thequakeseeker.data.EarthquakeDos;

import java.util.List;

public class EarthquakeDosAdapter extends RecyclerView.Adapter<EarthquakeDosAdapter.ViewHolder> {

    private List<EarthquakeDos> earthquakeDos;

    public EarthquakeDosAdapter(List<EarthquakeDos> earthquakeDos) {
        this.earthquakeDos = earthquakeDos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.what_fragment_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // Current earthquakeDos point from the list.
        final EarthquakeDos currentEarthquakeDo = earthquakeDos.get(position);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // Setting the image for the Dos.
                holder.ivDosImage.setImageResource(currentEarthquakeDo.getImageId());
            }
        });

        // Setting the corresponding the Dos for the image.
        holder.tvDosDescription.setText(currentEarthquakeDo.getImageDescription());
    }

    @Override
    public int getItemCount() {
        return earthquakeDos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDosDescription;
        ImageView ivDosImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDosDescription = itemView.findViewById(R.id.tvDosDescription);
            ivDosImage = itemView.findViewById(R.id.ivDosDescription);
        }
    }
}
