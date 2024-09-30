package za.co.varsitycollage.st10050487.eventat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import za.co.varsitycollage.st10050487.eventat.Fragments.InfoEvent;

public class EventInfoBooking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info_booking);

        // Retrieve the event name from the intent
        String eventName = getIntent().getStringExtra("EVENT_NAME");

        // Check if eventName is null
        if (eventName == null) {
            throw new IllegalArgumentException("Event name cannot be null");
        }

        // Load the InfoEvent fragment with the event name
        if (savedInstanceState == null) {
            Fragment infoEventFragment = InfoEvent.newInstance(eventName);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_EventInfo_container, infoEventFragment);
            transaction.commit();
        }
    }
}