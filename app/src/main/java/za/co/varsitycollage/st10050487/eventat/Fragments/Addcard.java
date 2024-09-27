package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import za.co.varsitycollage.st10050487.eventat.R;

public class Addcard extends Fragment {

    private EditText cardName, cardNumber, expiryDate, cvv;
    private DatabaseReference databaseReference;

    public Addcard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_addcard, container, false);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("CardInfo");

        // Initialize views
        cardName = view.findViewById(R.id.cardname);
        cardNumber = view.findViewById(R.id.cardnumber);
        expiryDate = view.findViewById(R.id.ExpiryDate);
        cvv = view.findViewById(R.id.Cvv);

        // Set up the date picker for expiry date
        setupExpiryDatePicker();

        // Set up the button to navigate to PaymentMethod fragment
        SendingBacktoPaymentMethod(view);

        // Set up the button to navigate to PaymentSuccessful fragment
        setupMakePaymentButton(view);

        return view;
    }

    private void setupExpiryDatePicker() {
        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the EditText
                        expiryDate.setText(String.format("%02d/%d", month + 1, year));
                    }
                }, year, month, calendar.get(Calendar.DAY_OF_MONTH));

                // Hide the day spinner if it exists
                int daySpinnerId = getResources().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = datePickerDialog.getDatePicker().findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }

                datePickerDialog.show();
            }
        });
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
                // Store card information in Firebase
                storeCardInfo();
            }
        });
    }

    private void storeCardInfo() {
        String name = cardName.getText().toString().trim();
        String number = cardNumber.getText().toString().trim();
        String expiry = expiryDate.getText().toString().trim();
        String cvvCode = cvv.getText().toString().trim();

        // Create a unique ID for each card entry
        String cardId = databaseReference.push().getKey();

        // Create a CardInfo object
        CardInfo cardInfo = new CardInfo(name, number, expiry, cvvCode, null);

        // Store the card information in Firebase
        saveCardInfoToFirebase(cardId, cardInfo);
    }

    private void saveCardInfoToFirebase(String cardId, CardInfo cardInfo) {
        if (cardId != null) {
            databaseReference.child(cardId).setValue(cardInfo).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Card information saved successfully", Toast.LENGTH_SHORT).show();
                    navigateToPaymentSuccessful();
                } else {
                    Toast.makeText(getActivity(), "Failed to save card information", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToPaymentSuccessful() {
        Fragment paymentSuccessfulFragment = new PaymentSuccessful();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_EventInfo_container, paymentSuccessfulFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}