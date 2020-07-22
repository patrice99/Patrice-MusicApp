package com.example.patrice_musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Event;
import com.parse.ParseFile;

import java.io.IOException;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    public Context context;
    public List<Event> events;
    public onClickListener clickListener;
    public static final String TAG = EventAdapter.class.getSimpleName();

    public interface onClickListener {
        void onEventClick(int position);
    }

    public EventAdapter(Context context, List<Event> events, onClickListener clickListener){
        this.context = context;
        this.events = events;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivEventImage;
        private ImageView ivHostProfilePic;
        private TextView tvEventName;
        private TextView tvEventLocation;
        private TextView tvEventDate;
        private TextView tvHostUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEventImage = itemView.findViewById(R.id.ivEventImage);
            ivHostProfilePic = itemView.findViewById(R.id.ivHostProfilePic);
            tvEventName = itemView.findViewById(R.id.tvName);
            tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvHostUsername = itemView.findViewById(R.id.tvHostUsername);
            itemView.setOnClickListener(this);
        }

        public void bind(Event event) {
            //bind views for events
            tvEventName.setText(event.getName());
            try {
                tvEventLocation.setText(Event.getStringFromLocation(event.getLocation(), context, TAG));
            } catch (IOException e) {
                e.printStackTrace();
            }
            tvEventDate.setText(event.getDate().toString());
            tvHostUsername.setText(event.getHost().getUsername());

            ParseFile image = event.getImage();
            if (image != null) {
                Glide.with(context).load(event.getImage().getUrl()).into(ivEventImage);
            }

            //check if the user has a valid profilePic
            ParseFile image2 = event.getHost().getParseFile("profileImage");
            if (image2 != null) {
                Glide.with(context)
                        .load(event.getHost().getParseFile("profileImage").getUrl())
                        .circleCrop()
                        .into(ivHostProfilePic);
            } else {
                Glide.with(context)
                        .load(context.getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                        .circleCrop()
                        .into(ivHostProfilePic);
            }


        }

        @Override
        public void onClick(View view) {
            clickListener.onEventClick(getAdapterPosition());
        }
    }

    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }
}
