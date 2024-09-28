package za.co.varsitycollage.st10050487.eventat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.eventat.databinding.EventItemBinding

class EventsAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount() = events.size

    inner class EventViewHolder(private val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.eventName.text = event.name
            binding.eventLocation.text = event.location
            binding.eventDate.text = event.date
            binding.eventPrice.text = "Starting from R ${event.ticketPrice}"
        }
    }
}
