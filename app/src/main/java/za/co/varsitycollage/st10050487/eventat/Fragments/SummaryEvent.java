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

public class SummaryEvent extends Fragment {

    public SummaryEvent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_event, container, false);

        // Find the arrowButton and set an OnClickListener
        view = MovingToStageMethod(view);

        return MovingToPayment(view);
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