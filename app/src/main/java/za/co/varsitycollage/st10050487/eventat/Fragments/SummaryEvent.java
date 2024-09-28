package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import za.co.varsitycollage.st10050487.eventat.R;

public class SummaryEvent extends Fragment {
    private BlockInfoViewModel blockInfoViewModel;
    private DatabaseReference databaseReference;
    private ImageView eventImage;

    public SummaryEvent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_event, container, false);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        // Retrieve the block info from the ViewModel
        blockInfoViewModel = new ViewModelProvider(requireActivity()).get(BlockInfoViewModel.class);

        view = UpdatingInfoInTextView(view);
        view = MovingToCoupon(view);
        view = MovingToPayment(view);
        view = MovingToStageMethod(view);

        // Retrieve event data from Firebase and update UI
        retrieveEventData(view);

        return view;
    }

    private void retrieveEventData(View view) {
        eventImage = view.findViewById(R.id.EventImage);
        databaseReference.child("-O7keyPNnAoXE4R6d5kj").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String eventDate = dataSnapshot.child("date").getValue(String.class);
                    String eventTime = dataSnapshot.child("startTime").getValue(String.class);
                    String eventPrice = dataSnapshot.child("ticketPrice").getValue(String.class);
                    String eventTitle = dataSnapshot.child("name").getValue(String.class);
                    String eventTitlteinfo = dataSnapshot.child("name").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                    // Update the TextViews with the retrieved data
                    TextView eventDateTextView = view.findViewById(R.id.EventDate);
                    TextView eventTimeTextView = view.findViewById(R.id.EventTime);
                    TextView eventPriceTextView = view.findViewById(R.id.EventPrice);
                    TextView eventTitleTextView = view.findViewById(R.id.EventTitle);
                    TextView eventTitleInfo = view.findViewById(R.id.EventitleInfo);

                    // Set the image using Glide
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(getContext()).load(imageUrl).into(eventImage);
                    } else {
                        // Set default image if imageUrl is null or empty
                        eventImage.setImageResource(R.drawable.springbox);
                    }

                    eventDateTextView.setText(eventDate);
                    eventTimeTextView.setText(eventTime);
                    eventPriceTextView.setText("R" + eventPrice);
                    eventTitleTextView.setText(eventTitle);
                    eventTitleInfo.setText(eventTitlteinfo);

                    // Set the image dimensions programmatically using dp
                    ViewGroup.LayoutParams layoutParams = eventImage.getLayoutParams();
                    layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 355, getResources().getDisplayMetrics());
                    layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 181, getResources().getDisplayMetrics());
                    eventImage.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private @NonNull View UpdatingInfoInTextView(View view) {
        blockInfoViewModel.getBlockInfo().observe(getViewLifecycleOwner(), blockInfo -> {
            if (blockInfo != null) {
                TextView ticketStageInfo = view.findViewById(R.id.ticketstageinfo);
                TextView stagePrice = view.findViewById(R.id.stage_price);

                // Extract the text part of the blockInfo string
                String text = blockInfo.replaceAll("[0-9]", "").trim();
                ticketStageInfo.setText(text);

                // Extract the price from the blockInfo string
                String price = blockInfo.replaceAll("[^0-9]", "");
                stagePrice.setText("R" + price);
            }
        });

        return view;
    }

    private @NonNull View MovingToCoupon(View view) {
        Button couponArrowButton = view.findViewById(R.id.Couponarrow);
        couponArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ApplyCoupon fragment
                Fragment applyCouponFragment = new ApplyCoupon();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, applyCouponFragment);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });

        return view;
    }

    private @NonNull View MovingToPayment(View view) {
        // Find the next button and set an OnClickListener
        Button nextButton = view.findViewById(R.id.EventSubmitButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to PaymentMethod fragment
                Fragment paymentMethodFragment = new PaymentMethod();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, paymentMethodFragment);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });

        return view;
    }

    private @NonNull View MovingToStageMethod(View view) {
        Button arrowButton = view.findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ChoosingTicketStage fragment
                Fragment choosingTicketStageFragment = new ChoosingTicketStage();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, choosingTicketStageFragment);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });

        return view;
    }
}