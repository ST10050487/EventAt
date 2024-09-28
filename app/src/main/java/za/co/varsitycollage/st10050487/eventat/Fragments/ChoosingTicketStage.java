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

        // Initialize buttons
        aBlockButton = view.findViewById(R.id.A_block_txt);
        bBlockButton = view.findViewById(R.id.b_block_txt);
        cBlockButton = view.findViewById(R.id.c_block_txt);
        dBlockButton = view.findViewById(R.id.d_block_txt);

        // Set button texts with prices
        double basePrice = InfoEvent.BASE_PRICE;
        aBlockButton.setText("A block (VIP Premium) R" + (int) Math.round(basePrice * 1.20));
        bBlockButton.setText("B Block (Premium) R" + (int) Math.round(basePrice * 1.15));
        cBlockButton.setText("C Block (Premium) R" + (int) Math.round(basePrice * 1.15));
        dBlockButton.setText("D block (Superior) R" + basePrice);

        view = Adding_MinusTickets(view);

        blockInfoViewModel = new ViewModelProvider(requireActivity()).get(BlockInfoViewModel.class);
        view = SendingBtnInfoToSummaryEvent(view);

        return view;
    }

    private @NonNull View SendingBtnInfoToSummaryEvent(View view) {
        aBlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = (int) Math.round(InfoEvent.BASE_PRICE * 1.20);
                String blockInfo = "A block (VIP Premium) " + price;
                Toast.makeText(getContext(), blockInfo, Toast.LENGTH_SHORT).show();
                blockInfoViewModel.setBlockInfo(blockInfo);
            }
        });

        bBlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = (int) Math.round(InfoEvent.BASE_PRICE * 1.15);
                String blockInfo = "B Block (Premium)  " + price;
                Toast.makeText(getContext(), blockInfo, Toast.LENGTH_SHORT).show();
                blockInfoViewModel.setBlockInfo(blockInfo);
            }
        });

        cBlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = (int) Math.round(InfoEvent.BASE_PRICE * 1.15);
                String blockInfo = "C Block (Premium)  " + price;
                cBlockButton.setText(blockInfo);
                Toast.makeText(getContext(), blockInfo, Toast.LENGTH_SHORT).show();
                blockInfoViewModel.setBlockInfo(blockInfo);
            }
        });

        dBlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomPrice = 250 + (int) (Math.random() * ((350 - 250) + 1));
                String blockInfo = "D block (Superior)  " + randomPrice;
                Toast.makeText(getContext(), blockInfo, Toast.LENGTH_SHORT).show();
                blockInfoViewModel.setBlockInfo(blockInfo);
            }
        });

        return view;
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