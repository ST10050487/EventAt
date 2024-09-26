package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import za.co.varsitycollage.st10050487.eventat.R;

public class InfoEvent extends Fragment {

    public InfoEvent() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_event, container, false);
        SendingToEventPromotion(view);
        SendingBtnToTicketStage(view);

        return view;


    }

    private void SendingToEventPromotion(View view) {
        // Set up the share button to navigate to EventPromotion
        Button shareButton = view.findViewById(R.id.share_icon);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of EventPromotion fragment
                EventPromotion eventPromotion = new EventPromotion();

                // Use FragmentManager to replace the current fragment with EventPromotion
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, eventPromotion);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });
    }


    private @NonNull View SendingBtnToTicketStage(View view) {
        Button ticketSubmitButton = view.findViewById(R.id.EventSubmitButton);
        // Set an OnClickListener to handle the button click
        ticketSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of ChoosingTicketStage fragment
                ChoosingTicketStage choosingTicketStage = new ChoosingTicketStage();

                // Use FragmentManager to replace the current fragment with ChoosingTicketStage
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, choosingTicketStage);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });

        return EventInfoValues(view);
    }

    private static @NonNull View EventInfoValues(View view) {
        // Find views by ID
        TextView eventTitle = view.findViewById(R.id.EventTitle);
        TextView supporting = view.findViewById(R.id.EventPrice);
        TextView eventDate = view.findViewById(R.id.EventDate);
        TextView eventAddress = view.findViewById(R.id.EventAddress);
        TextView eventTime = view.findViewById(R.id.EventTime);
        TextView eventWeather = view.findViewById(R.id.EventWeather);
        TextView eventParticipants = view.findViewById(R.id.EventParticipants);

        // Set values for each view
        eventTitle.setText("Spring bok vs Argentina");
        supporting.setText("R450");
        eventDate.setText("Date: " + "2021-09-22");
        eventAddress.setText("Address: " + "Cape Town Stadium");
        eventTime.setText("Time: " + "14:00");
        eventWeather.setText("Weather: " + "Sunny");
        eventParticipants.setText("Participants: " + "1000");

        return view;
    }
}