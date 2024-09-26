package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import za.co.varsitycollage.st10050487.eventat.R;


public class PaymentOnVenue extends Fragment {


    public PaymentOnVenue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_on_venue, container, false);

        // Set up navigation for the arrow button to Payment Success
        setupNavigation(view);


        // Set up the download button to show a Toast message
        setupDownloadButton(view);


        return view;
    }

    private void setupNavigation(View view) {
        // Find the arrow button and set a click listener
        Button arrowButton = view.findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to PaymentSuccessful fragment
                Fragment paymentMethodFragment = new PaymentMethod();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, paymentMethodFragment);
                transaction.addToBackStack(null);
                transaction.commit();
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