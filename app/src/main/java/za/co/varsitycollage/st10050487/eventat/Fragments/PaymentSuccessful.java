package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import za.co.varsitycollage.st10050487.eventat.Home;
import za.co.varsitycollage.st10050487.eventat.R;

public class PaymentSuccessful extends Fragment {

    public PaymentSuccessful() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_successful, container, false);

        // Generate a random 6-digit order ID
        String orderId = generateRandomOrderId();
        TextView orderIdTextView = view.findViewById(R.id.OrderID);
        orderIdTextView.setText("Order ID: " + orderId);

        SendingToTicketdownload(view);
        SendingToHomePage(view);

        return view;
    }

    private String generateRandomOrderId() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return String.valueOf(randomNumber);
    }

    private @NonNull View SendingToTicketdownload(View view) {
        // Find the 'Download Ticket' button and set a click listener
        Button downloadTicketButton = view.findViewById(R.id.DownloadTicketButton);
        downloadTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ticketDownload fragment
                Fragment ticketDownloadFragment = new ticketDownload();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, ticketDownloadFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private @NonNull View SendingToHomePage(View view) {
        // Find the button and set a click listener
        Button goToHomeButton = view.findViewById(R.id.go_to_home);
        goToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Home activity
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
            }
        });

        return view;
    }
}