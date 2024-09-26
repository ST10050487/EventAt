package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.co.varsitycollage.st10050487.eventat.R;

public class PaymentSuccessful extends Fragment {


    public PaymentSuccessful() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_successful, container, false);
    }
}