package za.co.varsitycollage.st10050487.eventat

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventInforAdapter(private val eventList : ArrayList<Event>) : RecyclerView.Adapter<EventInforAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImage = itemView.findViewById<ImageView>(R.id.event_image)
        val eventName = itemView.findViewById<TextView>(R.id.event_name)
        val eventDate = itemView.findViewById<TextView>(R.id.event_date)
        val eventLocation = itemView.findViewById<TextView>(R.id.event_location)
        val eventTicketPrice = itemView.findViewById<TextView>(R.id.ticket_price)
        val eventTicketPriceAtVenue = itemView.findViewById<TextView>(R.id.ticket_price_at_venue)

    }
}