package za.co.varsitycollage.st10050487.eventat.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
import androidx.activity.result.ActivityResultLauncher
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
        val eventName = view?.findViewById<EditText>(R.id.event_name)?.text.toString()
        val eventDate = eventDateTextView.text.toString()
        val startTime = view?.findViewById<TextView>(R.id.start_time)?.text.toString()
        val endTime = view?.findViewById<TextView>(R.id.end_time)?.text.toString()
        val eventCategory = view?.findViewById<Spinner>(R.id.event_category)?.selectedItem.toString()
        val eventLocation = eventLocationTextView.text.toString()
        val ticketPrice = view?.findViewById<TextView>(R.id.ticket_price)?.text.toString()
        val ticketPriceAtVenue = view?.findViewById<TextView>(R.id.ticket_price_at_venue)?.text.toString()
        val isPaidEvent = view?.findViewById<RadioButton>(R.id.paid_event)?.isChecked == true

        // Create Event object
        val event = Event(
            name = eventName,
            date = eventDate,
            startTime = startTime,
            endTime = endTime,
            category = eventCategory,
            location = eventLocation,
            ticketPrice = ticketPrice,
            ticketPriceAtVenue = ticketPriceAtVenue,
            isPaidEvent = isPaidEvent
        )

        // Push event to Firebase Database
        val eventId = database.child("events").push().key
        if (eventId != null) {
            // Upload image and get the download URL
            uploadImageAndGetUrl(eventId) { imageUrl ->
                // Upload video and get the download URL
                uploadVideoAndGetUrl(eventId) { videoUrl ->
                    // Save event with URLs to Firebase Database
                    event.imageUrl = imageUrl
                    event.videoUrl = videoUrl
                    database.child("events").child(eventId).setValue(event)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(requireContext(), "Event saved successfully!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Error saving event: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    private fun uploadImageAndGetUrl(eventId: String, onComplete: (String?) -> Unit) {
        imageUri?.let { uri ->
            val imageRef = storageRef.child("events/$eventId/image.jpg") // Define your storage path
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
            val videoRef = storageRef.child("events/$eventId/video.mp4") // Define your storage path
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



