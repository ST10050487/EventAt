package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Random;

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

        // Set up the apply coupon button
        Button applyCouponButton = view.findViewById(R.id.TaptoApply);
        applyCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCoupon();
            }
        });
    }

    private void applyCoupon() {
        // Generate a random number between 10 and 100
        Random random = new Random();
        int discount = 10 + random.nextInt(91);

        // Retrieve the current ticket price from the SummaryEvent fragment
        TextView totalAmountTextView = getActivity().findViewById(R.id.totalAmount);
        String totalAmountText = totalAmountTextView.getText().toString().replace("R", "").trim();
        int currentPrice = Integer.parseInt(totalAmountText);

        // Subtract the discount from the current price
        int newPrice = currentPrice - discount;

        // Update the UI with the new ticket price
        totalAmountTextView.setText("R " + newPrice);

        // Show a Toast message indicating the discount applied
        Toast.makeText(getActivity(), "Coupon applied successfully! Discount: R" + discount, Toast.LENGTH_LONG).show();
    }
    private static @NonNull View AddingAKeyboardFeature(View view) {
        // Request focus for the EditText
        EditText editText = view.findViewById(R.id.stage_price);
        editText.requestFocus();

        return view;
    }
}