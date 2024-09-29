package za.co.varsitycollage.st10050487.eventat.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import za.co.varsitycollage.st10050487.eventat.GoogleMapsAPI
import za.co.varsitycollage.st10050487.eventat.R
import java.util.*
import android.net.Uri
import android.widget.Toast
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import za.co.varsitycollage.st10050487.eventat.Event

const val ARG_PARAM1 = "param1"
const val ARG_PARAM2 = "param2"
private const val LOCATION_REQUEST_CODE = 1001

class CreateEvent : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var database: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initializing the Firebase Database
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var eventLocationTextView: TextView
    private lateinit var selectedImageView: ImageView
    private lateinit var uploadVideoButton: MaterialButton
    private lateinit var selectedVideoView: ImageView
    private lateinit var eventDateTextView: TextView

    private val getLocationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedLocation = result.data?.getStringExtra("selected_location")
            eventLocationTextView.text = selectedLocation
        }
    }

    //Storing the selected image URI
    private val getImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it // Save the selected image URI
            selectedImageView.visibility = View.VISIBLE
            selectedImageView.setImageURI(it)
        }
    }

    private val getVideoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            videoUri = it
            try {
                val videoPath = getRealPathFromURI(videoUri!!)

                val thumbnail = videoPath?.let { path ->
                    ThumbnailUtils.createVideoThumbnail(
                        path,
                        MediaStore.Video.Thumbnails.MINI_KIND
                    )
                }

                if (thumbnail != null) {
                    selectedVideoView.visibility = View.VISIBLE
                    selectedVideoView.setImageBitmap(thumbnail)
                } else {
                    Toast.makeText(requireContext(), "Failed to create thumbnail", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error creating thumbnail: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_event, container, false)

        // Initializing ImageView
        selectedImageView = view.findViewById(R.id.selected_image_view)
        uploadVideoButton = view.findViewById(R.id.upload_video)

        // Finding the TextView for event date
        eventDateTextView = view.findViewById(R.id.event_date)
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
        // Finding the TextView for event location
        eventLocationTextView = view.findViewById<TextView>(R.id.event_location)
        // Finding and setting click listener on upload button
        val uploadImageButton = view.findViewById<MaterialButton>(R.id.upload_image)
        // Initializing ImageView for video thumbnail
        selectedVideoView = view.findViewById<ImageView>(R.id.selected_video_view)
        val createEventButton: MaterialButton = view.findViewById<MaterialButton>(R.id.create_btn)
        // Initializing the eventDateTextView
        eventDateTextView = view.findViewById(R.id.event_date)
        //Finding and setting click listener for the reset button
        val resetButton: MaterialButton = view.findViewById<MaterialButton>(R.id.reset_btn)

        // Setting up the Spinner for event categories
        val eventCategorySpinner: Spinner = view.findViewById(R.id.event_category)
        val categories = arrayOf("Select Category", "Conference", "Workshop", "Webinar", "Meetup", "Social", "Sports", "Music", "Art", "Festival")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventCategorySpinner.adapter = adapter

        // Setting up an OnClickListener to show DatePickerDialog when the TextView is clicked
        eventDateTextView.setOnClickListener {
            showDatePickerDialog(eventDateTextView)
        }

        // Setting OnClickListeners to show TimePickerDialog when Start and End Time is clicked
        startTimeTextView.setOnClickListener {
            showTimePickerDialog(startTimeTextView)
        }

        endTimeTextView.setOnClickListener {
            showTimePickerDialog(endTimeTextView)
        }

        // Setting up click listener for the event location TextView
        eventLocationTextView.setOnClickListener {
            val intent = Intent(requireContext(), GoogleMapsAPI::class.java)
            getLocationLauncher.launch(intent)
        }
        // Setting up click listener for the reset button
        resetButton.setOnClickListener{
            resetFields()
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

        payAtEventRadioButton.setOnClickListener {
            if (payAtEventRadioButton.isChecked) {
                ticketPriceAtVenueEditText.isEnabled = true
                ticketPriceAtVenueEditText.requestFocus()
                freeEventRadioButton.isChecked = false
            }
        }

        uploadImageButton.setOnClickListener {
            getImageLauncher.launch("image/*")
        }

        // Set up the click listener for the "upload_video" button
        uploadVideoButton.setOnClickListener {
            getVideoLauncher.launch("video/*")
        }
        createEventButton.setOnClickListener {
            saveEventToFirebase()
        }

        return view
    }

    private fun showDatePickerDialog(eventDateTextView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedDay.toString().padStart(2, '0')}/${(selectedMonth + 1).toString().padStart(2, '0')}/$selectedYear"
                eventDateTextView.text = formattedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(timeTextView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeTextView.text = formattedTime
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val idx = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                it.getString(idx)
            } else {
                null
            }
        }
    }

    private fun saveEventToFirebase() {
        // Gather event details from UI elements
        val eventNameEditText = view?.findViewById<EditText>(R.id.event_name)
        val eventName = eventNameEditText?.text.toString()
        val eventDate = eventDateTextView.text.toString()
        val startTimeTextView = view?.findViewById<TextView>(R.id.start_time)
        val startTime = startTimeTextView?.text.toString()
        val endTimeTextView = view?.findViewById<TextView>(R.id.end_time)
        val endTime = endTimeTextView?.text.toString()
        val eventCategorySpinner = view?.findViewById<Spinner>(R.id.event_category)
        val eventCategory = eventCategorySpinner?.selectedItem.toString()
        val eventLocation = eventLocationTextView.text.toString()
        val numberOfAttendeesEditText = view?.findViewById<EditText>(R.id.attendees)
        val attendeesNum = numberOfAttendeesEditText?.text.toString()
        val eventDescriptionEditText = view?.findViewById<EditText>(R.id.event_details)
        val eventDescription = eventDescriptionEditText?.text.toString()

        // Radio buttons for event payment types
        val freeEventRadioButton = view?.findViewById<RadioButton>(R.id.free_event)
        val paidEventRadioButton = view?.findViewById<RadioButton>(R.id.paid_event)
        val payAtEventRadioButton = view?.findViewById<RadioButton>(R.id.pay_at_event)

        // Ticket prices
        val ticketPriceTextEditText = view?.findViewById<EditText>(R.id.ticket_price)
        val ticketPrice = ticketPriceTextEditText?.text.toString()
        val ticketPriceAtVenueEditText = view?.findViewById<EditText>(R.id.ticket_price_at_venue)
        val ticketPriceAtVenue = ticketPriceAtVenueEditText?.text.toString()

        // Check if all required fields are filled
        if (!validateInputs(eventNameEditText, eventName, startTimeTextView, startTime, endTimeTextView, endTime,
                eventDateTextView, eventDate, eventLocationTextView, eventLocation, numberOfAttendeesEditText, attendeesNum,
                eventDescriptionEditText, eventDescription, eventCategorySpinner, eventCategory,
                freeEventRadioButton, paidEventRadioButton, payAtEventRadioButton, ticketPriceTextEditText, ticketPrice,
                ticketPriceAtVenueEditText, ticketPriceAtVenue)) {
            return
        }

        // Determine event type and pricing
        var isPaidEvent = false
        var finalTicketPrice: String? = null
        var finalTicketPriceAtVenue: String? = null

        if (paidEventRadioButton?.isChecked == true) {
            isPaidEvent = true
            finalTicketPrice = ticketPrice
        }
        if (payAtEventRadioButton?.isChecked == true) {
            isPaidEvent = true
            finalTicketPriceAtVenue = ticketPriceAtVenue
        }

        // Create the Event object
        val event = Event(
            name = eventName,
            date = eventDate,
            startTime = startTime,
            endTime = endTime,
            category = eventCategory,
            location = eventLocation,
            attendents = attendeesNum,
            description = eventDescription,
            ticketPrice = finalTicketPrice,
            ticketPriceAtVenue = finalTicketPriceAtVenue,
            isPaidEvent = isPaidEvent
        )

        // Proceed with saving the event to Firebase
        val eventId = database.child("events").push().key
        if (eventId != null) {
            // Upload image and video logic
            uploadImageAndGetUrl(eventId) { imageUrl ->
                uploadVideoAndGetUrl(eventId) { videoUrl ->
                    event.imageUrl = imageUrl
                    event.videoUrl = videoUrl
                    database.child("events").child(eventId).setValue(event)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(requireContext(), "Event saved successfully!", Toast.LENGTH_SHORT).show()
                                resetFields()
                            } else {
                                Toast.makeText(requireContext(), "Error saving event: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }


    //A function to validate the input fields
    private fun validateInputs(
        eventNameEditText: EditText?,
        eventName: String,
        startTimeTextView: TextView?,
        startTime: String,
        endTimeTextView: TextView?,
        endTime: String,
        eventDateTextView: TextView?,
        eventDate: String,
        eventLocationTextView: TextView?,
        eventLocation: String,
        eventAttendeesNumberEditText: EditText?,
        attendeesNumber: String,
        eventDescriptionEditText: EditText?,
        eventDescription: String,
        eventCategorySpinner: Spinner?,
        eventCategory: String,
        freeEventRadioButton: RadioButton?,
        paidEventRadioButton: RadioButton?,
        payAtEventRadioButton: RadioButton?,
        ticketPriceTextEditText: EditText?,
        tickPrice: String,
        ticketPriceAtVenueEditText: EditText?,
        ticketPriceAtVenue: String
    ): Boolean {
        var isValid = true

        // Checking event name
        if (eventName.isEmpty()) {
            eventNameEditText?.error = "Event name is required"
            isValid = false
        }

        // Checking start time
        if (startTime.isEmpty()) {
            startTimeTextView?.error = "Start time is required"
            isValid = false
        }

        // Checking end time
        if (endTime.isEmpty()) {
            endTimeTextView?.error = "End time is required"
            isValid = false
        }
        // Checking event date
        if (eventDate.isEmpty()) {
            eventDateTextView?.error = "Event date is required"
            isValid = false
        }
        // Checking event location
        if(eventLocation.isEmpty()) {
            eventLocationTextView?.error = "Event location is required"
            isValid = false
        }
        // Checking ticket number
        if (attendeesNumber.isEmpty()) {
            eventAttendeesNumberEditText?.error = "Ticket number is required"
            isValid = false
        }
        // Checking event description
        if (eventDescription.isEmpty()) {
            eventDescriptionEditText?.error = "Event description is required"
            isValid = false
        }
        // Checking event category
        if (eventCategory == "Select Category") {
            eventCategorySpinner?.let {
                (it.selectedView as TextView).error = "Please select an event category"
            }
            Toast.makeText(requireContext(), "Please select an event category", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        // Checking if the user has selected an image
        if (imageUri == null) {
            val toast = Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT)
            // Getting the Toast view
            val view = toast.view
            // Finding the TextView within the Toast (Toast uses a TextView to display the message)
            val text = view?.findViewById<TextView>(android.R.id.message)
            // Setting the text color to red
            text?.setTextColor(Color.RED)
            // Showing the error message
            toast.show()

            isValid = false
        }
        // Checking if a user has selected a free event radio button or a paid event radio button or a pay at event radio button
        if (!(freeEventRadioButton?.isChecked ?: false) &&
            !(paidEventRadioButton?.isChecked ?: false) &&
            !(payAtEventRadioButton?.isChecked ?: false)) {

            val toast = Toast.makeText(requireContext(), "Please select an event type", Toast.LENGTH_SHORT)
            // Getting the Toast view
            val view = toast.view
            // Finding the TextView within the Toast (Toast uses a TextView to display the message)
            val text = view?.findViewById<TextView>(android.R.id.message)
            // Setting the text color to red
            text?.setTextColor(Color.RED)
            // Showing the error message
            toast.show()

            isValid = false
        }
        // Checking if the price is entered for paid event if the paid event radio button is selected
        if (paidEventRadioButton?.isChecked == true && tickPrice.isEmpty()) {
            ticketPriceTextEditText?.error = "Ticket price is required"
            isValid = false
        }
        // Checking if the price is entered for pay at event if the pay at event radio button is selected
        if (payAtEventRadioButton?.isChecked == true && ticketPriceAtVenue.isEmpty()) {
            ticketPriceAtVenueEditText?.error = "Ticket price is required"
            isValid = false
        }


        // Clear error when user starts typing
        eventNameEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) eventNameEditText.error = null
        }
        startTimeTextView?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) startTimeTextView.error = null
        }
        endTimeTextView?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) endTimeTextView.error = null
        }
        eventDateTextView?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) eventDateTextView.error = null
        }
        eventDateTextView?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) eventDateTextView.error = null
        }
        eventLocationTextView?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) eventLocationTextView.error = null
        }
        eventAttendeesNumberEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) eventAttendeesNumberEditText.error = null
        }
        eventDescriptionEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) eventDescriptionEditText.error = null
        }
        eventCategorySpinner?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) (eventCategorySpinner.selectedView as TextView).error = null
        }
        freeEventRadioButton?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) freeEventRadioButton.error = null
        }
        paidEventRadioButton?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) paidEventRadioButton.error = null
        }
        payAtEventRadioButton?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) payAtEventRadioButton.error = null
        }
        ticketPriceTextEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) ticketPriceTextEditText.error = null
        }
        ticketPriceAtVenueEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) ticketPriceAtVenueEditText.error = null
        }
        freeEventRadioButton?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Clear errors when free event is selected
                ticketPriceTextEditText?.error = null
                ticketPriceAtVenueEditText?.error = null
            }
        }
        return isValid
    }

    //A function to reset all input fields and error messages
    private fun resetFields() {
        // Reset TextViews and EditTexts
        view?.findViewById<EditText>(R.id.event_name)?.text?.clear()
        view?.findViewById<TextView>(R.id.event_date)?.text = ""
        view?.findViewById<TextView>(R.id.start_time)?.text = ""
        view?.findViewById<TextView>(R.id.end_time)?.text = ""
        view?.findViewById<EditText>(R.id.attendees)?.text?.clear()
        view?.findViewById<EditText>(R.id.event_details)?.text?.clear()
        view?.findViewById<TextView>(R.id.event_location)?.text = ""

        // Reset error messages
        view?.findViewById<EditText>(R.id.event_name)?.error = null
        view?.findViewById<TextView>(R.id.event_date)?.error = null
        view?.findViewById<TextView>(R.id.start_time)?.error = null
        view?.findViewById<TextView>(R.id.end_time)?.error = null
        view?.findViewById<EditText>(R.id.attendees)?.error = null
        view?.findViewById<TextView>(R.id.event_location)?.error = null

        // Reset Spinner to the first option
        view?.findViewById<Spinner>(R.id.event_category)?.setSelection(0)

        // Uncheck RadioButtons
        view?.findViewById<RadioButton>(R.id.free_event)?.isChecked = false
        view?.findViewById<RadioButton>(R.id.paid_event)?.isChecked = false
        view?.findViewById<RadioButton>(R.id.pay_at_event)?.isChecked = false

        // Clear ticket price fields and disable them
        val ticketPriceEditText = view?.findViewById<EditText>(R.id.ticket_price)
        val ticketPriceAtVenueEditText = view?.findViewById<EditText>(R.id.ticket_price_at_venue)
        ticketPriceEditText?.text?.clear()
        ticketPriceAtVenueEditText?.text?.clear()
        ticketPriceEditText?.isEnabled = false
        ticketPriceAtVenueEditText?.isEnabled = false

        // Clear image and video views
        selectedImageView.visibility = View.GONE
        selectedVideoView.visibility = View.GONE
        imageUri = null
        videoUri = null
    }


    // Helper function to show error on TextView
    private fun showError(textView: TextView, errorMessage: String) {
        textView.error = errorMessage
        textView.setTextColor(Color.RED)
    }


    // Helper function to show a red toast message
    private fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        val view = toast.view
        val text = view?.findViewById<TextView>(android.R.id.message)
        text?.setTextColor(Color.RED)
        toast.show()
    }


    private fun uploadImageAndGetUrl(eventId: String, onComplete: (String?) -> Unit) {
        imageUri?.let { uri ->
            val imageRef = storageRef.child("events/$eventId/image.jpg")
            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        onComplete(downloadUrl.toString())
                    }.addOnFailureListener {
                        onComplete(null)
                    }
                }.addOnFailureListener {
                    onComplete(null)
                }
        } ?: onComplete(null)
    }

    private fun uploadVideoAndGetUrl(eventId: String, onComplete: (String?) -> Unit) {
        videoUri?.let { uri ->
            val videoRef = storageRef.child("events/$eventId/video.mp4")
            videoRef.putFile(uri)
                .addOnSuccessListener {
                    videoRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        onComplete(downloadUrl.toString())
                    }.addOnFailureListener {
                        onComplete(null)
                    }
                }.addOnFailureListener {
                    onComplete(null)
                }
        } ?: onComplete(null)
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



