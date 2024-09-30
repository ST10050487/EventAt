// SummaryEvent.java
package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String ARG_EVENT_NAME = "eventName";
    private String eventName;
    private BlockInfoViewModel blockInfoViewModel;
    private DatabaseReference databaseReference;
    private ImageView eventImage;
    public static String FinalPrice;
    public static String FINAL_EVENT;

    public SummaryEvent() {
        // Required empty public constructor
    }

    public static SummaryEvent newInstance(String eventName) {
        SummaryEvent fragment = new SummaryEvent();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_NAME, eventName);
        fragment.setArguments(args);
        FINAL_EVENT = eventName;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventName = getArguments().getString(ARG_EVENT_NAME);
        }

        // Initialize the ViewModel
        blockInfoViewModel = new ViewModelProvider(requireActivity()).get(BlockInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_event, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        blockInfoViewModel = new ViewModelProvider(requireActivity()).get(BlockInfoViewModel.class);

        view = UpdatingInfoInTextView(view);
        view = MovingToCoupon(view);
        view = MovingToPayment(view);
        retrieveEventData(view);
        return view;
    }

    private void retrieveEventData(View view) {
        if (eventName == null) {
            Log.e("SummaryEvent", "Event name is null");
            return;
        }
        eventImage = view.findViewById(R.id.EventImage);
        databaseReference.orderByChild("name").equalTo(eventName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        String eventDate = eventSnapshot.child("date").getValue(String.class);
                        String eventTime = eventSnapshot.child("startTime").getValue(String.class);
                        String eventPrice = eventSnapshot.child("ticketPrice").getValue(String.class);
                        String eventTitle = eventSnapshot.child("name").getValue(String.class);
                        String eventTitlteinfo = eventSnapshot.child("name").getValue(String.class);
                        String imageUrl = eventSnapshot.child("imageUrl").getValue(String.class);

                        TextView eventDateTextView = view.findViewById(R.id.EventDate);
                        TextView eventTimeTextView = view.findViewById(R.id.EventTime);
                        TextView eventPriceTextView = view.findViewById(R.id.EventPrice);
                        TextView eventTitleTextView = view.findViewById(R.id.EventTitle);
                        TextView eventTitleInfo = view.findViewById(R.id.EventitleInfo);

                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(getContext()).load(imageUrl).into(eventImage);
                        } else {
                            eventImage.setImageResource(R.drawable.springbox);
                        }

                        eventDateTextView.setText(eventDate);
                        eventTimeTextView.setText(eventTime);
                        eventPriceTextView.setText("R" + eventPrice);
                        eventTitleTextView.setText(eventTitle);
                        eventTitleInfo.setText(eventTitlteinfo);

                        ViewGroup.LayoutParams layoutParams = eventImage.getLayoutParams();
                        layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 355, getResources().getDisplayMetrics());
                        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 181, getResources().getDisplayMetrics());
                        eventImage.setLayoutParams(layoutParams);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private @NonNull View UpdatingInfoInTextView(View view) {
        blockInfoViewModel.getBlockInfo().observe(getViewLifecycleOwner(), blockInfo ->
        {
            if (blockInfo != null) {
                TextView ticketStageInfo = view.findViewById(R.id.ticketstageinfo);
                TextView stagePrice = view.findViewById(R.id.stage_price);
                TextView totalPrice = view.findViewById(R.id.totalAmount);
                Button couponArrowButton = view.findViewById(R.id.Couponarrow);
                Button EventSubmitButton = view.findViewById(R.id.EventSubmitButton);

                // Extract the block name and price from the blockInfo string
                String[] parts = blockInfo.split(" R");
                if (parts.length == 2) {
                    String blockName = parts[0].trim();
                    String price = parts[1].trim();

                    // Check if the event is paid or free
                    Boolean paidEvent = InfoEvent.PAID_EVENT; // Assuming PAID_EVENT is a static variable in InfoEvent
                    if (paidEvent == null || !paidEvent) {
                        // If the event is free, set prices to "Free" and disable the coupon button
                        stagePrice.setText("Free");
                        totalPrice.setText("Free");
                        couponArrowButton.setEnabled(false);
                        FinalPrice = "Free";
                    } else {
                        // If the event is paid, set the actual prices
                        stagePrice.setText("R" + price);
                        totalPrice.setText("R" + price);
                        couponArrowButton.setEnabled(true);
                        FinalPrice = price;
                    }

                    // Update the TextViews
                    ticketStageInfo.setText(blockName);

                } else {
                    // Log an error if the blockInfo string is not in the expected format
                    Log.e("SummaryEvent", "Unexpected blockInfo format: " + blockInfo);
                }
            }
        });

        return view;
    }

    private @NonNull View MovingToCoupon(View view) {
        Button couponArrowButton = view.findViewById(R.id.Couponarrow);
        couponArrowButton.setOnClickListener(v -> {
            Fragment applyCouponFragment = new ApplyCoupon();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_EventInfo_container, applyCouponFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private @NonNull View MovingToPayment(View view) {
        Button nextButton = view.findViewById(R.id.EventSubmitButton);
        nextButton.setOnClickListener(v -> {
            if (!nextButton.isEnabled()) {
                Toast.makeText(getContext(), "Please select a block price before continuing.", Toast.LENGTH_SHORT).show();
            } else {
                // Create the PaymentMethod instance and pass the eventName
                Fragment paymentMethodFragment = PaymentMethod.newInstance(eventName);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, paymentMethodFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}