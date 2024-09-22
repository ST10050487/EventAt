package za.co.varsitycollage.st10050487.eventat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import za.co.varsitycollage.st10050487.eventat.Fragments.InfoEvent;

public class EventInfoBooking {
    private View eventInfoView;

    public void createEventInfoView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        eventInfoView = inflater.inflate(R.layout.fragment_info_event, new ConstraintLayout(context), false);
    }

    public View getEventInfoView() {
        return eventInfoView;
    }
}