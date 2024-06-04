package com.example.nerede;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private List<Etkinlik> activityList;
    private Context context;

    public ActivityAdapter(Context context, List<Etkinlik> activityList) {
        this.context = context;
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Etkinlik activity = activityList.get(position);
        holder.titleTextView.setText(activity.getIsim());
        holder.capacityTextView.setText("Kapasite: " + activity.getEtkinlikKapasitesi());
        holder.reserveTextView.setText("Rezerve edilen: " + activity.getRezerveSayisi());
        holder.contentTextView.setText("Açıklama: " + activity.getAciklama());
        holder.locationTextView.setText("Konum: " + activity.getKonum());
        holder.dateTextView.setText("Tarih: " + activity.getZaman());
        holder.timeTextView.setText("Saat: " + activity.getSaat());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityDetail.class);
            intent.putExtra("activity", activity);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, capacityTextView, contentTextView, locationTextView, dateTextView, timeTextView, reserveTextView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.activity_title);
            capacityTextView = itemView.findViewById(R.id.activity_capacity);
            reserveTextView = itemView.findViewById(R.id.activity_rezerve);
            contentTextView = itemView.findViewById(R.id.activity_content);
            locationTextView = itemView.findViewById(R.id.activity_location);
            dateTextView = itemView.findViewById(R.id.activity_date);
            timeTextView = itemView.findViewById(R.id.activity_time);
        }
    }
}
