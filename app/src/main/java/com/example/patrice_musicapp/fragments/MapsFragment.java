package com.example.patrice_musicapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Event;
import com.example.patrice_musicapp.models.User;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {
    private static final int DISPLAY_LIMIT =20;
    public static final String TAG = MapsFragment.class.getSimpleName();
    private List<Event> allEvents = new ArrayList<>();
    private User user;
    private Event event;
    private LinearLayout bottomSheetEvent;
    private BottomSheetBehavior bottomSheetEventBehavior;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            queryEvents(0, googleMap);

        }

    };

    private void addMarkers(GoogleMap googleMap) {
        //pan camera to the location of the user. make this marker green
        user = new User(ParseUser.getCurrentUser());
        LatLng userLatLng = new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(userLatLng)
                .title("Me")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        //change the view to the user location with a view of 15
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));

        //for each event location, add a specific color for events
        for (Event event: allEvents){
            LatLng latLng = new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(event.getName()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        //Get the bundle to determine if bottom navigation sheet is pulled up or not
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int position = bundle.getInt("position");
            event = allEvents.get(position);
            bottomSheetEvent = view.findViewById(R.id.bottom_sheet_event);
            bottomSheetEventBehavior = BottomSheetBehavior.from(bottomSheetEvent);
            bottomSheetEventBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }


    }

    private void queryEvents(final int page, final GoogleMap googleMap) {
        Event.query(page, DISPLAY_LIMIT, new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Event event : events) {
                    Log.i(TAG, "Post: " + event.getDescription() + " Username: " + event.getHost().getUsername());
                }
                allEvents.addAll(events);
                addMarkers(googleMap);
            }
        });
    }


}