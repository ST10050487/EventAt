package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import za.co.varsitycollage.st10050487.eventat.R;

public class InfoEvent extends Fragment {
    private static final String ARG_EVENT_NAME = "eventName";
    private String eventName;
    private static final String API_KEY = "73e64f4603ce99b1b81a64d0f7363b2d";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private TextView eventTitle, eventPrice, eventDate, eventAddress, eventTime, eventParticipants, eventWeather;
    private DatabaseReference databaseReference;
    private ImageView eventImage;
    public static double BASE_PRICE;
    public static Boolean PAID_EVENT;

    public InfoEvent() {
        // Required empty public constructor
    }

    public static InfoEvent newInstance(String eventName) {
        InfoEvent fragment = new InfoEvent();
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
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_event, container, false);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        // Initialize views
        eventTitle = view.findViewById(R.id.EventTitle);
        eventPrice = view.findViewById(R.id.EventPrice);
        eventDate = view.findViewById(R.id.EventDate);
        eventAddress = view.findViewById(R.id.EventAddress);
        eventTime = view.findViewById(R.id.EventTime);
        eventWeather = view.findViewById(R.id.EventWeather);
        eventParticipants = view.findViewById(R.id.EventParticipants);
        eventImage = view.findViewById(R.id.EventImage);

        // Fetch event data from Firebase
        fetchEventData();

        // Fetch weather data
        fetchWeatherData();

        SendingToEventPromotion(view);
        SendingBtnToTicketStage(view);

        return view;
    }

    private void fetchEventData() {
        if (eventName == null) {
            Log.e("InfoEvent", "Event name is null");
            return;
        }

        databaseReference.orderByChild("name").equalTo(eventName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        String name = eventSnapshot.child("name").getValue(String.class);
                        String date = eventSnapshot.child("date").getValue(String.class);
                        String location = eventSnapshot.child("location").getValue(String.class);
                        String startTime = eventSnapshot.child("startTime").getValue(String.class);
                        String ticketPrice = eventSnapshot.child("ticketPrice").getValue(String.class);
                        String imageUrl = eventSnapshot.child("imageUrl").getValue(String.class);
                        String participants = eventSnapshot.child("attendents").getValue(String.class);
                        Boolean paidEvent = eventSnapshot.child("paidEvent").getValue(Boolean.class);

                        // Check if the event is paid or if the ticket price is null/empty
                        if (paidEvent == null || !paidEvent || ticketPrice == null || ticketPrice.isEmpty()) {
                            eventPrice.setText("Free");
                            PAID_EVENT = false;
                        } else {
                            eventPrice.setText("R" + ticketPrice);
                            BASE_PRICE = Double.parseDouble(ticketPrice);
                        }

                        // Update UI with the retrieved data
                        eventTitle.setText(name);
                        eventDate.setText("Date: " + date);
                        eventAddress.setText("Address: " + location);
                        eventTime.setText("Time: " + startTime);
                        eventParticipants.setText("Participants: " + participants);

                        // Set the image using Glide
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(getContext()).load(imageUrl).into(eventImage);
                        } else {
                            // Set default image if imageUrl is null or empty
                            eventImage.setImageResource(R.drawable.springbox);
                        }

                        // Set the image dimensions programmatically
                        ViewGroup.LayoutParams layoutParams = eventImage.getLayoutParams();
                        layoutParams.width = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._411sdp);
                        layoutParams.height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._265sdp);
                        eventImage.setLayoutParams(layoutParams);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    private void fetchWeatherData() {
        new FetchWeatherTask().execute();
    }

    private class FetchWeatherTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String urlString = WEATHER_URL + "?lat=-33.918861&lon=18.423300&appid=" + API_KEY + "&units=metric";
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    double temperature = jsonObject.getJSONObject("main").getDouble("temp");
                    String weatherText = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    eventWeather.setText(String.format("Weather: %.1f °C, %s", temperature, weatherText));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void SendingToEventPromotion(View view) {
        // Set up the share button to navigate to EventPromotion
        Button shareButton = view.findViewById(R.id.share_icon);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a bundle with event data to pass to EventPromotion
                Bundle bundle = new Bundle();
                bundle.putString("eventTitle", eventTitle.getText().toString());
                bundle.putString("eventPrice", eventPrice.getText().toString());
                bundle.putString("eventDate", eventDate.getText().toString());
                bundle.putString("eventAddress", eventAddress.getText().toString());
                bundle.putString("eventTime", eventTime.getText().toString());
                bundle.putString("eventWeather", eventWeather.getText().toString());
                bundle.putString("eventParticipants", eventParticipants.getText().toString());

                // Create an instance of EventPromotion fragment
                EventPromotion eventPromotion = new EventPromotion();
                eventPromotion.setArguments(bundle);  // Set the bundle with event data

                // Use FragmentManager to replace the current fragment with EventPromotion
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_EventInfo_container, eventPromotion);
                transaction.addToBackStack(null);  // Add to back stack to allow back navigation
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
            // Create an instance of ChoosingTicketStage fragment with the event name
            ChoosingTicketStage choosingTicketStage = ChoosingTicketStage.newInstance(eventName);

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
        ImageView eventImage = view.findViewById(R.id.EventImage);

        // Set dummy values for each view
        eventTitle.setText("Spring bok vs Argentina");
        supporting.setText("R 350");
        eventDate.setText("Date: " + "2021-09-22");
        eventAddress.setText("Address: " + "Cape Town Stadium");
        eventTime.setText("Time: " + "14:00");
        eventWeather.setText("Weather: " + "Sunny");
        eventParticipants.setText("Participants: " + "1000");

        return view;
    }
}