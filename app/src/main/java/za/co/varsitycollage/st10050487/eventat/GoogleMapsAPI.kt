package za.co.varsitycollage.st10050487.eventat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GoogleMapsAPI : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap

    // Define a constant for permission request code
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.google_maps_api)

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.MAPS_KEY))
        }

        // Initialize Map Fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize AutocompleteSupportFragment
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Get source from Intent
        var source = intent.getStringExtra("source")

        // Get the TextView by its ID
        val selectLocationTextView = findViewById<TextView>(R.id.selectLocation)

        // Set the text based on the source value
        when (source) {
            "CreateEvent" -> {
                selectLocationTextView.text = "Search and Select Location Of Event"
            }
            "Login" -> {
                selectLocationTextView.text = "Search and Select Your Current Location"
            }
            else -> {
                selectLocationTextView.text = "Search and Select Location"
            }
        }

        // Set up PlaceSelectionListener
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.d("GoogleMapsAPI", "Place selected: ${place.name}")
                val latLng = place.latLng
                if (latLng != null) {
                    // Move the camera to the selected location
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    googleMap.addMarker(MarkerOptions().position(latLng).title(place.name))

                    // Log the source value
                    Log.d("GoogleMapsAPI", "Source: $source")

                    // Check for null source and set a fallback
                    if (source == null) {
                        Log.e("GoogleMapsAPI", "Source is null, defaulting to 'Unknown'")
                        source = "Unknown"
                    }

                    if (source == "CreateEvent") {
                        // Return the selected location to CreateEvent
                        val resultIntent = Intent().apply {
                            putExtra("selected_location", place.name)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    } else if (source == "Login") {
                        // Save the selected location in Firebase for the logged-in user
                        saveLocationToFirebase(place.name)
                        finish()
                    }
                }
            }
            override fun onError(status: Status) {
                Log.e("GoogleMapsAPI", "An error occurred: $status")
            }
        })
    // Request location permissions
        requestLocationPermissions()
    }

    // A function to request location permissions
    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    // A function to enable location layer if permission is granted
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
    }

    // A function to handle permission request results
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    enableMyLocation()
                } else {
                    // Permission denied, handle accordingly
                    Log.e("GoogleMapsAPI", "Location permission denied")
                }
            }
        }
    }

    // A function to handle map ready event
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Set default location (optional)
        val defaultLocation = LatLng(-34.0, 151.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
        // Enabling location layer
        enableMyLocation()
    }

    // A function to save the selected location to Firebase
    private fun saveLocationToFirebase(locationName: String) {
        // Retrieve logged-in user email from SharedPreferences
        val sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val loggedInUserEmail = sharedPreferences.getString("loggedInUserEmail", null)

        if (loggedInUserEmail != null) {
            // Replace dots in the email with an underscore
            val safeEmail = loggedInUserEmail.replace(".", "_")

            // Assuming you have a Firebase reference set up to get user ID
            val usersRef = FirebaseDatabase.getInstance().getReference("Users")
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var userId: String? = null
                    for (userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)
                        if (email == loggedInUserEmail) {
                            // Getting the user ID
                            userId = userSnapshot.key
                            break
                        }
                    }

                    if (userId != null) {
                        // Save location using user ID directly under "locations"
                        val databaseReference = FirebaseDatabase.getInstance().getReference("Users/$userId/locations")

                        // Save the location name directly, without using push()
                        databaseReference.setValue(locationName)
                            .addOnSuccessListener {
                                Log.d("GoogleMapsAPI", "Location saved to Firebase successfully.")

                                // Navigate to the Home class
                                val intent = Intent(this@GoogleMapsAPI, Home::class.java)
                                startActivity(intent)
                                // Closing the current activity
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.e("GoogleMapsAPI", "Failed to save location: ${e.message}")
                            }
                    } else {
                        Log.e("GoogleMapsAPI", "User with email $loggedInUserEmail not found.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("GoogleMapsAPI", "Error fetching users: ${error.message}")
                }
            })
        } else {
            Log.e("GoogleMapsAPI", "No logged-in user found.")
        }
    }


}


