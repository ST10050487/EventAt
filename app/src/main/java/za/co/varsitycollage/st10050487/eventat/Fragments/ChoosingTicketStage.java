package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import za.co.varsitycollage.st10050487.eventat.R;

public class ChoosingTicketStage extends Fragment {

    private static final String ARG_EVENT_NAME = "eventName";
    private String eventName;
    private DatabaseReference databaseReference;

    private TextView ticketNo;
    private TextView ticketAdd;
    private TextView ticketMinus;
    private Button arrowButton;
    private Button ticketSubmitButton;

    private Button aBlockButton;
    private Button bBlockButton;
    private Button cBlockButton;
    private Button dBlockButton;
    private BlockInfoViewModel blockInfoViewModel;

    public ChoosingTicketStage() {
        // Required empty public constructor
    }

    public static ChoosingTicketStage newInstance(String eventName) {
        ChoosingTicketStage fragment = new ChoosingTicketStage();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_NAME, eventName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventName = getArguments().getString(ARG_EVENT_NAME);
        }

        // Initialize the ViewModel
        blockInfoViewModel = new ViewModelProvider(requireActivity()).get(BlockInfoViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choosing_ticket_stage, container, false);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        // Initialize views
        ticketNo = view.findViewById(R.id.ticketNo);
        ticketAdd = view.findViewById(R.id.ticketAdd);
        ticketMinus = view.findViewById(R.id.ticketMinus);
        arrowButton = view.findViewById(R.id.arrowButton);
        ticketSubmitButton = view.findViewById(R.id.TicketSubmitButton);
        aBlockButton = view.findViewById(R.id.A_block_txt);
        bBlockButton = view.findViewById(R.id.b_block_txt);
        cBlockButton = view.findViewById(R.id.c_block_txt);
        dBlockButton = view.findViewById(R.id.d_block_txt);

        // Fetch event data from Firebase
        fetchEventData();

        // Set up button listeners
        ticketAdd.setOnClickListener(v -> {
            int currentNumber = Integer.parseInt(ticketNo.getText().toString());
            ticketNo.setText(String.valueOf(currentNumber + 1));
        });

        ticketMinus.setOnClickListener(v -> {
            int currentNumber = Integer.parseInt(ticketNo.getText().toString());
            if (currentNumber > 1) {
                ticketNo.setText(String.valueOf(currentNumber - 1));
            }
        });

        // Navigate back to InfoEvent fragment
        arrowButton.setOnClickListener(v -> {
            Fragment infoEventFragment = new InfoEvent();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_EventInfo_container, infoEventFragment);
            transaction.addToBackStack(null); // Add to back stack to allow back navigation
            transaction.commit();
        });

        // Navigate to SummaryEvent fragment
        ticketSubmitButton.setOnClickListener(v -> {
            Fragment summaryEventFragment = new SummaryEvent();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_EventInfo_container, summaryEventFragment);
            transaction.addToBackStack(null); // Add to back stack to allow back navigation
            transaction.commit();
        });

        return view;
    }

    private void fetchEventData() {
        if (eventName == null) {
            Log.e("ChoosingTicketStage", "Event name is null");
            return;
        }

        databaseReference.orderByChild("name").equalTo(eventName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        Boolean paidEvent = eventSnapshot.child("paidEvent").getValue(Boolean.class);
                        String priceString = eventSnapshot.child("ticketPrice").getValue(String.class);

                        double basePrice = 0.0;
                        if (priceString != null) {
                            try {
                                basePrice = Double.parseDouble(priceString);
                            } catch (NumberFormatException e) {
                                Log.e("ChoosingTicketStage", "Failed to convert ticketPrice to double", e);
                            }
                        }

                        InfoEvent.BASE_PRICE = basePrice;
                        InfoEvent.PAID_EVENT = paidEvent != null ? paidEvent : false;

                        ButtonBlocksPrices();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void ButtonBlocksPrices() {
        // Set button texts with prices
        double basePrice = InfoEvent.BASE_PRICE;
        Boolean paidEvent = InfoEvent.PAID_EVENT;

        if (paidEvent == null || !paidEvent) {
            // If the event is free, disable the buttons and set text to "Free"
            aBlockButton.setText("A block (VIP Premium) Free");
            bBlockButton.setText("B Block (Premium) Free");
            cBlockButton.setText("C Block (Premium) Free");
            dBlockButton.setText("D block (Superior) Free");

            aBlockButton.setEnabled(false);
            bBlockButton.setEnabled(false);
            cBlockButton.setEnabled(false);
            dBlockButton.setEnabled(false);
        } else {
            // If the event is paid, set the prices and enable the buttons
            aBlockButton.setText("A block (VIP Premium) R" + (int) Math.round(basePrice * 1.20));
            bBlockButton.setText("B Block (Premium) R" + (int) Math.round(basePrice * 1.15));
            cBlockButton.setText("C Block (Premium) R" + (int) Math.round(basePrice * 1.15));
            dBlockButton.setText("D block (Superior) R" + basePrice);

            aBlockButton.setEnabled(true);
            bBlockButton.setEnabled(true);
            cBlockButton.setEnabled(true);
            dBlockButton.setEnabled(true);

            aBlockButton.setOnClickListener(v -> {
                int price = (int) Math.round(basePrice * 1.20);
                updateBlockInfo("A block (VIP Premium)", price);
            });

            bBlockButton.setOnClickListener(v -> {
                int price = (int) Math.round(basePrice * 1.15);
                updateBlockInfo("B Block (Premium)", price);
            });

            cBlockButton.setOnClickListener(v -> {
                int price = (int) Math.round(basePrice * 1.15);
                updateBlockInfo("C Block (Premium)", price);
            });

            dBlockButton.setOnClickListener(v -> {
                int randomPrice = 250 + (int) (Math.random() * ((350 - 250) + 1));
                updateBlockInfo("D block (Superior)", randomPrice);
            });
        }
    }

    private void updateBlockInfo(String blockName, int price) {
        int numberOfTickets = Integer.parseInt(ticketNo.getText().toString());
        int totalPrice = price * numberOfTickets;
        String blockInfo = blockName + " R" + totalPrice;
        blockInfoViewModel.setBlockInfo(blockInfo);
        Toast.makeText(getContext(), blockInfo, Toast.LENGTH_SHORT).show();
    }
}