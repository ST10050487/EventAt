package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
}