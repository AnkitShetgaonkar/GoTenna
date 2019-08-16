package droid.ankit.gotennademo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.etiennelenhart.eiffel.state.peek
import com.etiennelenhart.eiffel.viewmodel.delegate.providedViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import droid.ankit.database.PinPoint
import droid.ankit.gotennademo.base.BaseActivity
import droid.ankit.gotennademo.util.PermissionCallback
import droid.ankit.gotennademo.util.PermissionManager
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import java.util.*
import java.util.function.Consumer


class MapsActivity : BaseActivity(), OnMapReadyCallback, PermissionCallback {


    private val permissionManager: PermissionManager by inject()

    private val viewModel by providedViewModel<MapViewModel>()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        viewModel.observeState(this){
            it.event?.peek { event-> handleScreenEvent(event) }
            it.pinPointList?.observe(this,androidx.lifecycle.Observer {pinPoints->
                markPinPointData(pinPoints)
                Log.e("Live Data","printing out size  "+pinPoints.size)
            })
        }
        lifecycle.addObserver(viewModel)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val btnShow = findViewById<Button>(R.id.btnShowList)
        btnShow.setOnClickListener{
            showBottomLocations()
        }
    }

    private fun handleScreenEvent(event: MapScreenEvent): Boolean {
        return when(event) {
            is MapScreenEvent.ShowPermissionRationale->{
                Snackbar.make(
                    findViewById(R.id.myCoordinatorLayout),
                    event.message,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.ok){
                    permissionManager.showPermissionDialog(this)
                }.show()
                true
            }

            is MapScreenEvent.PermissionDenied->{
                Snackbar.make(
                    findViewById(R.id.myCoordinatorLayout),
                    R.string.user_location_denied_message,
                    Snackbar.LENGTH_INDEFINITE
                ).show()
                true
            }

            is MapScreenEvent.PermissionGranted->{
                markUserLocation()
                true
            }


            else->false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.getPinPoints()
        permissionManager.checkLocationPermission(this)
    }

    private fun markPinPointData(pinPoints: List<PinPoint>) {
        for(pinPoint in pinPoints) {
            val userLocation = LatLng(pinPoint.latitude, pinPoint.longitude)
            mMap.addMarker(MarkerOptions().position(userLocation)
                .title(pinPoint.name)
                .snippet(pinPoint.description))
        }
    }

    private fun showBottomLocations() {
        val addPhotoBottomDialogFragment = LocationFragment()
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,
            "locations_fragment"
        )
    }
    @SuppressLint("MissingPermission")
    private fun markUserLocation() {
        if (!permissionManager.isPermissionGranted()) {
            return
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val userLocation = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions()
                .position(userLocation).title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        permissionManager.onRequestPermissionsResult(requestCode,permissions, grantResults,this)
    }

    override fun showPermissionRationale() {
        viewModel.showPermissionRationale()
    }

    override fun permissionBlocked() {
        viewModel.permissionBlocked()
    }

    override fun permissionGranted() {
        viewModel.permissionGranted()
    }

    override fun getActivity(): BaseActivity {
        return this
    }

}
