package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import za.co.varsitycollage.st10050487.eventat.R;

public class EventPromotion extends Fragment {

    public EventPromotion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_promotion, container, false);

        return BackToInfoEvent(view);
    }

    private @NonNull View BackToInfoEvent(View view) {
        // Set up the arrow button to navigate to InfoEvent
        Button arrowButton = view.findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of InfoEvent fragment
                InfoEvent infoEvent = new InfoEvent();

                // Use FragmentManager to replace the current fragment with InfoEvent
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, infoEvent);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });

        return view;
    }
}