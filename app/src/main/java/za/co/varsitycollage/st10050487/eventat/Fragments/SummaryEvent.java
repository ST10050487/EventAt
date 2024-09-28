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
    public static int FinalPrice;

    public SummaryEvent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_event, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        blockInfoViewModel = new ViewModelProvider(requireActivity()).get(BlockInfoViewModel.class);

        view = UpdatingInfoInTextView(view);
        view = MovingToCoupon(view);
        view = MovingToPayment(view);
        view = MovingToStageMethod(view);

        retrieveEventData(view);

        return view;
    }

    private void retrieveEventData(View view) {
        eventImage = view.findViewById(R.id.EventImage);
        databaseReference.child("-O7tO1M1ex8AumOmI3sU").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String eventDate = dataSnapshot.child("date").getValue(String.class);
                    String eventTime = dataSnapshot.child("startTime").getValue(String.class);
                    String eventPrice = dataSnapshot.child("ticketPrice").getValue(String.class);
                    String eventTitle = dataSnapshot.child("name").getValue(String.class);
                    String eventTitlteinfo = dataSnapshot.child("name").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private @NonNull View UpdatingInfoInTextView(View view) {
        blockInfoViewModel.getBlockInfo().observe(getViewLifecycleOwner(), blockInfo -> {
            if (blockInfo != null) {
                TextView ticketStageInfo = view.findViewById(R.id.ticketstageinfo);
                TextView stagePrice = view.findViewById(R.id.stage_price);
                TextView totalPrice = view.findViewById(R.id.totalAmount);
                Button couponArrowButton = view.findViewById(R.id.Couponarrow);

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
                    } else {
                        // If the event is paid, set the actual prices
                        stagePrice.setText("R" + price);
                        totalPrice.setText("R" + price);
                        couponArrowButton.setEnabled(true);
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
            Fragment paymentMethodFragment = new PaymentMethod();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_EventInfo_container, paymentMethodFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private @NonNull View MovingToStageMethod(View view) {
        Button arrowButton = view.findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(v -> {
            Fragment choosingTicketStageFragment = new ChoosingTicketStage();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_EventInfo_container, choosingTicketStageFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}