// PaymentMethod.java
package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;

import za.co.varsitycollage.st10050487.eventat.R;

public class PaymentMethod extends Fragment {
    private static final String ARG_EVENT_NAME = "eventName";
    private String eventName;

    public static PaymentMethod newInstance(String eventName)
    {
        PaymentMethod fragment = new PaymentMethod();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_NAME, eventName);
        fragment.setArguments(args);
        return fragment;
    }

    public PaymentMethod() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventName = getArguments().getString(ARG_EVENT_NAME);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_method, container, false);

        // Retrieve the passed data from the arguments
        if (getArguments() != null) {
            eventName = getArguments().getString(ARG_EVENT_NAME);
        }

        // Find the cardarrowButton and set an OnClickListener
        SendingToAddCardFragment(view);

        // Set up navigation for the cash arrow button
        setupCashNavigation(view);

        // Set up "Coming Soon" messages for Google Pay and Apple Pay buttons
        setupComingSoonMessages(view);

        return view;
    }

    private @NonNull View SendingToAddCardFragment(View view) {
        // Find the cardarrowButton and set an OnClickListener
        Button cardarrowButton = view.findViewById(R.id.CardarrowButton);

        // Check if the event is paid or free
        Boolean paidEvent = InfoEvent.PAID_EVENT;
        if (paidEvent == null || !paidEvent) {
            // If the event is free, disable the card payment button and show a Toast message
            cardarrowButton.setEnabled(false);
            Toast.makeText(getContext(), "No card needed", Toast.LENGTH_SHORT).show();
        } else {
            // If the event is paid, enable the card payment button and set the click listener
            cardarrowButton.setEnabled(true);
            cardarrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an instance of the Addcard fragment
                    Fragment addCardFragment = Addcard.newInstance(eventName);

                    // Replace the current fragment with the Addcard fragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_EventInfo_container, addCardFragment);
                    transaction.addToBackStack(null); // Add to back stack to allow back navigation
                    transaction.commit();
                }
            });
        }

        return view;
    }

    private void setupCashNavigation(View view) {
        // Find the cash arrow button and set a click listener
        Button cashArrowButton = view.findViewById(R.id.CasharrowButton);
        cashArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to PaymentOnVenue fragment
                Fragment paymentOnVenueFragment = new PaymentOnVenue();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, paymentOnVenueFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void setupComingSoonMessages(View view) {
        // Find the Google Pay arrow button and set a click listener
        Button googleArrowButton = view.findViewById(R.id.GoogleRightarrowButton);
        googleArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show "Coming Soon" message
                Toast.makeText(getContext(), "Google Pay feature coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        // Find the Apple Pay arrow button and set a click listener
        Button appleArrowButton = view.findViewById(R.id.ApplearrowButton);
        appleArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show "Coming Soon" message
                Toast.makeText(getContext(), "Apple Pay feature coming soon", Toast.LENGTH_SHORT).show();
            }
        });
    }
}