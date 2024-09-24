package za.co.varsitycollage.st10050487.eventat

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat

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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Permissions are already granted, you can access location
            if (::googleMap.isInitialized) {
                enableMyLocation()
            } else {
                // Map is not initialized yet, enableMyLocation will be called in onMapReady
            }
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, enable location functionality
                    if (::googleMap.isInitialized) {
                        enableMyLocation()
                    } else {
                        Log.e("GoogleMapsAPI", "GoogleMap is not initialized yet.")
                    }
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
        enableMyLocation()
    }
}
