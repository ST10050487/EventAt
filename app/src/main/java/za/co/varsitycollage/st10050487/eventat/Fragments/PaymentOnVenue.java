package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import za.co.varsitycollage.st10050487.eventat.R;

public class PaymentOnVenue extends Fragment {

    private DatabaseReference databaseReference;
    private TextView eventHeading, eventDate, eventTime, eventPrice;
    private ImageView eventImage;
    private String eventName;
    private String stagePrice;

    public PaymentOnVenue() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_on_venue, container, false);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        // Initialize views
        eventHeading = view.findViewById(R.id.EventHeading);
        eventDate = view.findViewById(R.id.EventDate);
        eventTime = view.findViewById(R.id.EventTime);
        eventImage = view.findViewById(R.id.EventImage);
        eventPrice = view.findViewById(R.id.stage_price);

        // Fetch event data from Firebase
        fetchEventData();

        // Set up the download button to show a Toast message
        setupDownloadButton(view);

        return view;
    }

    private void fetchEventData() {
        stagePrice = SummaryEvent.FinalPrice;
        eventName = SummaryEvent.FINAL_EVENT;
        if (eventName == null) {
            Log.e("PaymentOnVenue", "Event name is null");
            return;
        }
        Log.d("PaymentOnVenue", "Fetching data for event: " + eventName);
        databaseReference.orderByChild("name").equalTo(eventName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        String heading = eventSnapshot.child("name").getValue(String.class);
                        String date = eventSnapshot.child("date").getValue(String.class);
                        String time = eventSnapshot.child("startTime").getValue(String.class);
                        String imageUrl = eventSnapshot.child("imageUrl").getValue(String.class);
                        Boolean paidEvent = eventSnapshot.child("paidEvent").getValue(Boolean.class);
                        String ticketPriceAtVenue = eventSnapshot.child("ticketPriceAtVenue").getValue(String.class);

                        // Determine the price to display
                        if (paidEvent == null || !paidEvent) {
                            eventPrice.setText("Price: Free");
                        } else if (ticketPriceAtVenue != null && !ticketPriceAtVenue.isEmpty()) {
                            eventPrice.setText("Price at Venue: ZAR " + ticketPriceAtVenue);
                        } else {
                            eventPrice.setText("Price: ZAR " + stagePrice);
                        }

                        // Update UI with the retrieved data
                        eventHeading.setText(heading);
                        eventDate.setText(date);
                        eventTime.setText(time);

                        // Set the image using Glide
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(getContext()).load(imageUrl).into(eventImage);
                        } else {
                            // Set default image if imageUrl is null or empty
                            eventImage.setImageResource(R.drawable.springbox);
                        }

                        // Set the image dimensions programmatically using dp
                        ViewGroup.LayoutParams layoutParams = eventImage.getLayoutParams();
                        layoutParams.width = 0;
                        layoutParams.height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._181sdp);
                        eventImage.setLayoutParams(layoutParams);
                    }
                } else {
                    Log.e("PaymentOnVenue", "No data found for event: " + eventName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    private void setupDownloadButton(View view) {
        // Find the download button and set a click listener
        Button downloadButton = view.findViewById(R.id.MakeTicketButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a Toast message
                Toast.makeText(getActivity(), "Your ticket has been downloaded to the gallery", Toast.LENGTH_LONG).show();
            }
        });
    }
}