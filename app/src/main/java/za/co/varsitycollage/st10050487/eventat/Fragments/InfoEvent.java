package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
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
        eventAddress.setText(getString(R.string.supporting_2));
        eventTime.setText("Time: " + "14:00");
        eventWeather.setText("Weather: " + "Sunny");
        eventParticipants.setText("Participants: " + "1000");

        return view;
    }
}