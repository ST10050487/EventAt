package za.co.varsitycollage.st10050487.eventat

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

class GoogleMapsAPI : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.google_maps_api)

        // Initializing APIs of Places Places
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.MAPS_KEY))
        }

        // Initialize Map Fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize AutocompleteSupportFragment
        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        // Set the fields to return after a place is selected
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the place selected by the user
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Handle the selected location
                val latLng = place.latLng
                if (latLng != null) {
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(latLng).title(place.name))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                Log.i("GoogleMapsAPI", "An error occurred: $status")
            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Set default location (optional)
        val defaultLocation = LatLng(-34.0, 151.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }
}
