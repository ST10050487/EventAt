// ChoosingTicketStage.java
package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import za.co.varsitycollage.st10050487.eventat.R;

public class ChoosingTicketStage extends Fragment {

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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choosing_ticket_stage, container, false);

        ButtonBlocksPrices(view);

        blockInfoViewModel = new ViewModelProvider(requireActivity()).get(BlockInfoViewModel.class);
        view = Adding_MinusTickets(view);


        return view;
    }

    @SuppressLint("SetTextI18n")
    private void ButtonBlocksPrices(View view) {
        // Initialize buttons
        aBlockButton = view.findViewById(R.id.A_block_txt);
        bBlockButton = view.findViewById(R.id.b_block_txt);
        cBlockButton = view.findViewById(R.id.c_block_txt);
        dBlockButton = view.findViewById(R.id.d_block_txt);
        ticketNo = view.findViewById(R.id.ticketNo);

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


    private @NonNull View Adding_MinusTickets(View view) {
        ticketNo = view.findViewById(R.id.ticketNo);
        ticketAdd = view.findViewById(R.id.ticketAdd);
        ticketMinus = view.findViewById(R.id.ticketMinus);
        arrowButton = view.findViewById(R.id.arrowButton);
        ticketSubmitButton = view.findViewById(R.id.TicketSubmitButton);

        ticketAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNumber = Integer.parseInt(ticketNo.getText().toString());
                ticketNo.setText(String.valueOf(currentNumber + 1));
            }
        });

        ticketMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNumber = Integer.parseInt(ticketNo.getText().toString());
                if (currentNumber > 1) {
                    ticketNo.setText(String.valueOf(currentNumber - 1));
                }
            }
        });

        // Navigate back to InfoEvent fragment
        GoingBackToEventInfo();

        // Navigate to SummaryEvent fragment
        SendingToSummaryEvent();

        return view;
    }

    private void SendingToSummaryEvent() {
        // Navigate to SummaryEvent fragment
        ticketSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment summaryEventFragment = new SummaryEvent();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, summaryEventFragment);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });
    }

    private void GoingBackToEventInfo() {
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to InfoEvent fragment
                Fragment infoEventFragment = new InfoEvent();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, infoEventFragment);
                transaction.addToBackStack(null); // Add to back stack to allow back navigation
                transaction.commit();
            }
        });
    }
}