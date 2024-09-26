package za.co.varsitycollage.st10050487.eventat.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import za.co.varsitycollage.st10050487.eventat.R
import java.util.*

const val ARG_PARAM1 = "param1"
const val ARG_PARAM2 = "param2"

class CreateEvent : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        // Inflating the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_event, container, false)

        // Finding the TextView for event date
        val eventDateTextView: TextView = view.findViewById(R.id.event_date)
        // Finding the TextViews for start and end times
        val startTimeTextView = view.findViewById<TextView>(R.id.start_time)
        val endTimeTextView = view.findViewById<TextView>(R.id.end_time)
        // Finding the EditTexts for ticket prices
        val ticketPriceEditText = view.findViewById<TextView>(R.id.ticket_price)
        val ticketPriceAtVenueEditText = view.findViewById<TextView>(R.id.ticket_price_at_venue)
        // Finding the RadioButtons for paid and free events
        val paidEventRadioButton = view.findViewById<RadioButton>(R.id.paid_event)
        val payAtEventRadioButton = view.findViewById<RadioButton>(R.id.pay_at_event)
        val freeEventRadioButton = view.findViewById<RadioButton>(R.id.free_event)

        // Setting up the Spinner for event categories
        val eventCategorySpinner: Spinner = view.findViewById(R.id.event_category)
        val categories = arrayOf("Select Category","Conference", "Workshop", "Webinar", "Meetup", "Social", "Sports", "Music", "Art", "Festival")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventCategorySpinner.adapter = adapter

        // Setting up an OnClickListener to show DatePickerDialog when the TextView is clicked
        eventDateTextView.setOnClickListener {
            showDatePickerDialog(eventDateTextView)
        }
        // Setting onClickListeners to show TimePickerDialog when Start Time is clicked
        startTimeTextView.setOnClickListener {
            showTimePickerDialog(startTimeTextView)
        }
        // Setting onClickListeners to show TimePickerDialog when End Time is clicked
        endTimeTextView.setOnClickListener {
            showTimePickerDialog(endTimeTextView)
        }
        // Disabling EditTexts based on RadioButton selection
        freeEventRadioButton.setOnClickListener {
            if (freeEventRadioButton.isChecked) {
                ticketPriceEditText.isEnabled = false
                ticketPriceAtVenueEditText.isEnabled = false
                ticketPriceAtVenueEditText.text = ""
                ticketPriceEditText.text = ""
                paidEventRadioButton.isChecked = false
                payAtEventRadioButton.isChecked = false
            }
        }
        // Enabling EditTexts based on RadioButton selection
        paidEventRadioButton.setOnClickListener {
            if (paidEventRadioButton.isChecked) {
                ticketPriceEditText.isEnabled = true
                ticketPriceEditText.requestFocus()
                freeEventRadioButton.isChecked = false
            }
        }
        // Enabling EditTexts based on RadioButton selection
        payAtEventRadioButton.setOnClickListener {
            if (payAtEventRadioButton.isChecked) {
                ticketPriceAtVenueEditText.isEnabled = true
                ticketPriceAtVenueEditText.requestFocus()
                freeEventRadioButton.isChecked = false
            }
        }

        return view
    }

    // A function to display DatePickerDialog and set the selected date in the TextView
    private fun showDatePickerDialog(eventDateTextView: TextView) {
        // Use the current date as the default date in the picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and set the date when picked
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the TextView with the selected date
                val formattedDate = "${selectedDay.toString().padStart(2, '0')}/${(selectedMonth + 1).toString().padStart(2, '0')}/$selectedYear"
                eventDateTextView.text = formattedDate
            },
            year, month, day
        )
        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    // Function to display TimePickerDialog and set the selected time in the TextView
    private fun showTimePickerDialog(timeTextView: TextView) {
        // Use the current time as the default time in the picker
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create and show TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // Format the time as HH:MM and set it in the TextView
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeTextView.text = formattedTime
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateEvent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


