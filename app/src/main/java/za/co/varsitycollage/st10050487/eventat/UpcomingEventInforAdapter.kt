package za.co.varsitycollage.st10050487.eventat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UpcomingEventInforAdapter(private val eventList: ArrayList<Event>) : RecyclerView.Adapter<UpcomingEventInforAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_upcoming_event, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = eventList[position]

        // Null check for imageUrl and load a placeholder if imageUrl is null
        val imageUrl = currentItem.imageUrl
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .apply(
                RequestOptions().placeholder(R.drawable.ic_no_image_icon)
                    .error(R.drawable.ic_no_image_icon)
            )
            .into(holder.eventImage)

        // Set event details
        holder.eventName.text = currentItem.name
        holder.eventDate.text = currentItem.date
        holder.eventLocation.text = currentItem.location
        holder.eventTicketPrice.text = currentItem.ticketPrice
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImage: ImageView = itemView.findViewById(R.id.event_image)
        val eventName: TextView = itemView.findViewById(R.id.name_of_event)
        val eventDate: TextView = itemView.findViewById(R.id.date_of_event)
        val eventLocation: TextView = itemView.findViewById(R.id.location_of_event)
        val eventTicketPrice: TextView = itemView.findViewById(R.id.price)
    }
}

