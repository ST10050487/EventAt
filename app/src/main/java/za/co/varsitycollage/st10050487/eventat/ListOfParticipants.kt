package za.co.varsitycollage.st10050487.eventat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class ListOfParticipants : AppCompatActivity() {

    private lateinit var searchBar: EditText
    private lateinit var soldButton: Button
    private lateinit var scrollView: ScrollView
    private lateinit var homeIcon: ImageView
    private lateinit var myEventsIcon: ImageView
    private lateinit var createEventIcon: ImageView
    private lateinit var profileIcon: ImageView
    private lateinit var participantNames: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_participants)

        // Initialize Views
        searchBar = findViewById(R.id.search_bar)
        soldButton = findViewById(R.id.sold_button)
        scrollView = findViewById(R.id.participants_scroll)
        homeIcon = findViewById(R.id.home_icon)
        myEventsIcon = findViewById(R.id.my_events_icon)
        createEventIcon = findViewById(R.id.create_event_icon)
        profileIcon = findViewById(R.id.profile_icon)

        // Participant cards' name views
        participantNames = listOf(
            findViewById(R.id.participant_name),
            findViewById(R.id.participant_name1),
            findViewById(R.id.participant_name2),
            findViewById(R.id.participant_name3),
            findViewById(R.id.participant_name4)
        )

        // Set listeners for the search bar
        setupSearchBar()

        // Set listener for the "Sold" button
        soldButton.setOnClickListener {
            Toast.makeText(this, "Sold Button Clicked", Toast.LENGTH_SHORT).show()
        }

        // Set bottom navigation icons' listeners
        setupBottomNavigation()
    }

    // Search bar functionality
    private fun setupSearchBar() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterParticipants(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Filter participants based on search query
    private fun filterParticipants(query: String) {
        participantNames.forEach { participantName ->
            val participantCard = participantName.parent.parent as CardView
            if (participantName.text.toString().contains(query, ignoreCase = true)) {
                participantCard.visibility = android.view.View.VISIBLE
            } else {
                participantCard.visibility = android.view.View.GONE
            }
        }
    }

    // Bottom navigation functionality
    private fun setupBottomNavigation() {
        homeIcon.setOnClickListener {
            Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show()
        }

        myEventsIcon.setOnClickListener {
            Toast.makeText(this, "My Events Clicked", Toast.LENGTH_SHORT).show()
        }

        createEventIcon.setOnClickListener {
            Toast.makeText(this, "Create Event Clicked", Toast.LENGTH_SHORT).show()
        }

        profileIcon.setOnClickListener {
            Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
