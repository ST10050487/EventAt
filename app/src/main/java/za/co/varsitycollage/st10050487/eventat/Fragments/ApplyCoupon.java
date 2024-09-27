package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

        return AddingAKeyboardFeature(view);
    }

    private static @NonNull View AddingAKeyboardFeature(View view) {
        // Request focus for the EditText
        EditText editText = view.findViewById(R.id.supporting_);
        editText.requestFocus();

        return view;
    }
}