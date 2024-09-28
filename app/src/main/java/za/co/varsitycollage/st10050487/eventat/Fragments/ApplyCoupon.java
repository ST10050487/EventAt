package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import za.co.varsitycollage.st10050487.eventat.R;

public class ApplyCoupon extends Fragment {

    public ApplyCoupon() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_coupon, container, false);

        SendingBackToSummaryEvent(view);

        return AddingAKeyboardFeature(view);
    }

    private void SendingBackToSummaryEvent(View view) {
        // Set up the arrow button to navigate back to SummaryEvent
        Button arrowButton = view.findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment summaryEventFragment = new SummaryEvent();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, summaryEventFragment);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });
    }

    private static @NonNull View AddingAKeyboardFeature(View view) {
        // Request focus for the EditText
        EditText editText = view.findViewById(R.id.stage_price);
        editText.requestFocus();

        return view;
    }
}