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

import za.co.varsitycollage.st10050487.eventat.R;

public class PaymentMethod extends Fragment {

    public PaymentMethod() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_method, container, false);

        // Find the cardarrowButton and set an OnClickListener
        SendingToAddCardFragment(view);

        // Set up navigation for the cash arrow button
        setupCashNavigation(view);

        // Set up "Coming Soon" messages for Google Pay and Apple Pay buttons
        setupComingSoonMessages(view);

        // Find the arrowButton and set an OnClickListener
        SendingBackToSummaryEvent(view);

        return view;
    }


    private @NonNull View SendingBackToSummaryEvent(View view) {
        Button arrowButton = view.findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ChoosingTicketStage fragment
                Fragment SummaryEventFragment = new SummaryEvent();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, SummaryEventFragment);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });

        return view;
    }
    private @NonNull View SendingToAddCardFragment(View view) {
        // Find the cardarrowButton and set an OnClickListener
        Button cardarrowButton = view.findViewById(R.id.CardarrowButton);
        cardarrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the Addcard fragment
                Fragment addCardFragment = new Addcard();

                // Replace the current fragment with the Addcard fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, addCardFragment);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });

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