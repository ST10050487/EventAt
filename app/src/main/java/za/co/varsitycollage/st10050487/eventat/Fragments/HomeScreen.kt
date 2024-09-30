package za.co.varsitycollage.st10050487.eventat.Fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import za.co.varsitycollage.st10050487.eventat.Event
import za.co.varsitycollage.st10050487.eventat.CloseEventInforAdapter
import za.co.varsitycollage.st10050487.eventat.EventInfoBooking
import za.co.varsitycollage.st10050487.eventat.UpcomingEventInforAdapter
import za.co.varsitycollage.st10050487.eventat.R

class HomeScreen : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbref: DatabaseReference
    private lateinit var eventList: ArrayList<Event>
    private lateinit var closeEventAdapter: CloseEventInforAdapter
    private lateinit var upcomingEventList: ArrayList<Event>
    private lateinit var upcomingEventAdapter: UpcomingEventInforAdapter
    private lateinit var closeEventsRecyclerView: RecyclerView
    private lateinit var upcomingEventsRecyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var locationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerViews and Adapters
        closeEventsRecyclerView = view.findViewById(R.id.closeEventsRecyclerView)
        closeEventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        closeEventsRecyclerView.setHasFixedSize(true)

        // Set horizontal layout manager for upcomingEventsRecyclerView
        upcomingEventsRecyclerView = view.findViewById(R.id.upcomingEventsRecyclerView)
        upcomingEventsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        upcomingEventsRecyclerView.setHasFixedSize(true)

        eventList = arrayListOf()
        closeEventAdapter = CloseEventInforAdapter(eventList) { event ->
            navigateToInfoEvent(event)
        }
        closeEventsRecyclerView.adapter = closeEventAdapter

        upcomingEventList = arrayListOf()
        upcomingEventAdapter = UpcomingEventInforAdapter(upcomingEventList) { event ->
            navigateToInfoEvent(event)
        }
        upcomingEventsRecyclerView.adapter = upcomingEventAdapter

        // Fetch logged-in user data
        sharedPreferences = requireActivity().getSharedPreferences("userPrefs", 0)
        locationTextView = view.findViewById(R.id.location)

        // Fetch data
        getUserData()
        getUpcomingEvents(upcomingEventList, upcomingEventAdapter)
        getUserLocation()  // New method to fetch and display last user location
    }

    private fun navigateToInfoEvent(event: Event) {
        val intent = Intent(requireContext(), EventInfoBooking::class.java)
        intent.putExtra("EVENT_NAME", event.name)
        startActivity(intent)
    }

    private fun getUserLocation() {
        // Retrieve logged-in user email from SharedPreferences
        val loggedInUserEmail = sharedPreferences.getString("loggedInUserEmail", null)

        if (loggedInUserEmail != null) {
            // Replace dots in the email to ensure it's Firebase-safe
            val safeEmail = loggedInUserEmail.replace(".", "_")

            // Reference the Users node in Firebase
            val usersRef = FirebaseDatabase.getInstance().getReference("Users")

            // Search for the user with the given email
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var userId: String? = null

                    // Loop through all users to find the one matching the email
                    for (userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)
                        if (email == loggedInUserEmail) {
                            userId = userSnapshot.key // Get the user ID
                            break // Exit the loop once the user is found
                        }
                    }

                    if (userId != null) {
                        // Reference the user's locations field
                        val dbref = FirebaseDatabase.getInstance().getReference("Users/$userId/locations")
                        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(locationSnapshot: DataSnapshot) {
                                if (locationSnapshot.exists()) {
                                    // Since 'locations' is stored as a string, get the value directly
                                    val lastLocationName = locationSnapshot.getValue(String::class.java)

                                    // Set the location in the TextView
                                    locationTextView.text = lastLocationName ?: "No Locations Found"
                                } else {
                                    locationTextView.text = "No Locations Found"
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                                locationTextView.text = "Error retrieving location"
                            }
                        })
                    } else {
                        locationTextView.text = "User not found"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    locationTextView.text = "Error retrieving user information"
                }
            })
        } else {
            locationTextView.text = "Email not found in SharedPreferences"
        }
    }



    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("events")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (eventSnapshot in snapshot.children) {
                        val event = eventSnapshot.getValue(Event::class.java)
                        eventList.add(event!!)
                    }
                    closeEventAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun getUpcomingEvents(
        upcomingEventList: ArrayList<Event>,
        adapter: UpcomingEventInforAdapter
    ) {
        dbref = FirebaseDatabase.getInstance().getReference("events")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Clearing the list before adding new data
                    upcomingEventList.clear()
                    for (eventSnapshot in snapshot.children) {
                        val event = eventSnapshot.getValue(Event::class.java)
                        upcomingEventList.add(event!!)
                    }
                    // Notifying the adapter that the data has changed
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}





