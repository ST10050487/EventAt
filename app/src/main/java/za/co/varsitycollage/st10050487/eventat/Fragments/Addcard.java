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

public class Addcard extends Fragment {

    public Addcard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_addcard, container, false);

        // Set up the button to navigate to PaymentMethod fragment
        SendingBacktoPaymentMethod(view);

        // Set up the button to navigate to PaymentSuccessful fragment
        setupMakePaymentButton(view);

        return view;
    }

    private void SendingBacktoPaymentMethod(View view) {
        // Find the arrowButton and set an OnClickListener
        Button arrowButton = view.findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the PaymentMethod fragment
                Fragment paymentMethodFragment = new PaymentMethod();

                // Replace the current fragment with the PaymentMethod fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, paymentMethodFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void setupMakePaymentButton(View view) {
        Button makePaymentButton = view.findViewById(R.id.MakePaymentButton);
        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the PaymentSuccessful fragment
                Fragment paymentSuccessfulFragment = new PaymentSuccessful();

                // Replace the current fragment with the PaymentSuccessful fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, paymentSuccessfulFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}