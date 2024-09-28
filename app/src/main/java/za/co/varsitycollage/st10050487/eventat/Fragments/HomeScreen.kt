package za.co.varsitycollage.st10050487.eventat.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import za.co.varsitycollage.st10050487.eventat.Event
import za.co.varsitycollage.st10050487.eventat.CloseEventInforAdapter
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

        // Findings the close events recycler view
        closeEventsRecyclerView = view.findViewById(R.id.closeEventsRecyclerView)
        closeEventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        closeEventsRecyclerView.setHasFixedSize(true)

        // Findings the upcoming events recycler view
        upcomingEventsRecyclerView = view.findViewById(R.id.upcomingEventsRecyclerView)
        upcomingEventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        upcomingEventsRecyclerView.setHasFixedSize(true)

        eventList = arrayListOf<Event>()
        closeEventAdapter = CloseEventInforAdapter(eventList)
        closeEventsRecyclerView.adapter = closeEventAdapter

        upcomingEventList = arrayListOf<Event>()
        upcomingEventAdapter = UpcomingEventInforAdapter(upcomingEventList)
        upcomingEventsRecyclerView.adapter = upcomingEventAdapter

        // Calling the functions to get the data from the database
        getUserData()
        getUpcomingEvents(upcomingEventList, upcomingEventAdapter)
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

    private fun getUpcomingEvents(upcomingEventList: ArrayList<Event>, adapter: UpcomingEventInforAdapter) {
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
