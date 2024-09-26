package za.co.varsitycollage.st10050487.eventat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        // Set up PlaceSelectionListener
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Get the location of the selected place
                val latLng = place.latLng
                if (latLng != null) {
                    // Move the camera to the selected location
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    // Optionally, add a marker at the selected location
                    googleMap.addMarker(MarkerOptions().position(latLng).title(place.name))

                    // Return the selected location to CreateEvent
                    val resultIntent = Intent().apply {
                        putExtra("selected_location", place.name)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish() // Close the GoogleMapsAPI activity
                }
            }


            override fun onError(status: com.google.android.gms.common.api.Status) {
                Log.e("GoogleMapsAPI", "An error occurred: $status")
            }
        })

        // Request location permissions
        requestLocationPermissions()
    }

    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }
        // No need to call enableMyLocation here
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
    }

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

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Set default location (optional)
        val defaultLocation = LatLng(-34.0, 151.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
        // Enabling location layer
        enableMyLocation()
    }
}

