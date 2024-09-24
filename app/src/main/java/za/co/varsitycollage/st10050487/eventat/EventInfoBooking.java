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

        // Load the InfoEvent fragment
        if (savedInstanceState == null) {
            Fragment infoEventFragment = new InfoEvent();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_EventInfo_container, infoEventFragment);
            transaction.commit();
        }
    }
}