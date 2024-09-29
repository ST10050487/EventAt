package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import za.co.varsitycollage.st10050487.eventat.R;

public class EventPromotion extends Fragment {

    //Declaring variables
    TextView instagram, facebook, twitter;

    String eventTitle = "";
    String eventPrice = "";
    String eventDate = "";
    String eventAddress = "";
    String eventTime = "";
    String eventWeather = "";
    String eventParticipants = "";

    public EventPromotion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_promotion, container, false);

        // Initializing the variables
        instagram = view.findViewById(R.id.instagram);
        facebook = view.findViewById(R.id.face_book);
        twitter = view.findViewById(R.id.twit);

        // Retrieve the event data from the arguments bundle
        if (getArguments() != null) {
             eventTitle = getArguments().getString("eventTitle");
             eventPrice = getArguments().getString("eventPrice");
             eventDate = getArguments().getString("eventDate");
             eventAddress = getArguments().getString("eventAddress");
             eventTime = getArguments().getString("eventTime");
             eventWeather = getArguments().getString("eventWeather");
             eventParticipants = getArguments().getString("eventParticipants");

        }
        // Calling the allClickListeners method
        allClickListeners();

        return BackToInfoEvent(view);
    }

    // A method to set up all the click listeners
    private void allClickListeners() {
            // Set up the Instagram button to share the event details on Instagram
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct the message with event details
                String message = "Event Title: " + eventTitle + "\n" +
                        "Event Price: R " + eventPrice + "\n" +
                        "Event Date: " + eventDate + "\n" +
                        "Event Address: " + eventAddress + "\n" +
                        "Event Time: " + eventTime + "\n" +
                        "Event Weather: " + eventWeather + "\n" +
                        "Event Participants: " + eventParticipants;

                // Open Instagram, allowing the user to copy the details manually
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"));
                startActivity(intent);

                // You can also show a Toast to inform the user to copy the message
                Toast.makeText(getContext(), "Copy the event details to share on Instagram:\n" + message, Toast.LENGTH_LONG).show();
            }
        });

        // Set up the Facebook button to share event details on Facebook
        // Set up the Facebook button to share event details on Facebook
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct the message with event details
                String message = "Event Title: " + eventTitle + "\n" +
                        "Event Price: R " + eventPrice + "\n" +
                        "Event Date: " + eventDate + "\n" +
                        "Event Address: " + eventAddress + "\n" +
                        "Event Time: " + eventTime + "\n" +
                        "Event Weather: " + eventWeather + "\n" +
                        "Event Participants: " + eventParticipants;

                // Construct the Facebook share URL
                String facebookShareUrl = "https://www.facebook.com/sharer/sharer.php?u="
                        + Uri.encode("https://EventAt.com")
                        + "&quote=" + Uri.encode(message);

                // Create an intent to view the Facebook share URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookShareUrl));
                startActivity(intent);
            }
        });


        // Set up the Twitter button to share event details on Twitter
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String twitterShareUrl = "https://twitter.com/intent/tweet?text="
                        + Uri.encode("Event Title: " + eventTitle + "\n" +
                        "Event Price: " + "R " + eventPrice + "\n" +
                        "Event Date: " + eventDate + "\n" +
                        "Event Address: " + eventAddress + "\n" +
                        "Event Time: " + eventTime + "\n" +
                        "Event Weather: " + eventWeather + "\n" +
                        "Event Participants: " + eventParticipants);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterShareUrl));
                startActivity(intent);
            }
        });
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
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}