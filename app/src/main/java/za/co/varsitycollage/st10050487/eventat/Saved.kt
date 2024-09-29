package za.co.varsitycollage.st10050487.eventat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import za.co.varsitycollage.st10050487.eventat.databinding.FragmentSavedBinding

class Saved : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var eventsAdapter: EventsAdapter
    private var eventList: MutableList<Event> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        eventsAdapter = EventsAdapter(eventList)
        binding.savedEventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }

        // Initialize Firebase Database reference for all events
        database = FirebaseDatabase.getInstance().getReference("events")

        // Fetch events from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                if (snapshot.exists()) {
                    for (eventSnapshot in snapshot.children) {
                        val event = eventSnapshot.getValue(Event::class.java)
                        event?.let { eventList.add(it) }
                    }
                    eventsAdapter.notifyDataSetChanged() // Notify adapter about data change
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors here
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





