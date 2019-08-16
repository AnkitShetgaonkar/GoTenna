package droid.ankit.gotennademo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
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
import org.koin.android.ext.android.inject




class MapsActivity : BaseActivity(), OnMapReadyCallback, PermissionCallback {


    private val permissionManager: PermissionManager by inject()

    private val TAG:String = MapsActivity::class.java.name

    private val viewModel by providedViewModel<MapViewModel>()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var networkProgress:ProgressBar
    private lateinit var dataStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        networkProgress = findViewById(R.id.progress)
        dataStatus = findViewById(R.id.tvDataStatus)

        viewModel.observeState(this){
            it.event?.peek { event-> handleScreenEvent(event) }
            it.pinPointList?.observe(this, Observer { pinPoints->
                markPinPointData(pinPoints)
                Log.e(TAG,"printing out size of points "+pinPoints.size)
            })
            it.fetchingFromNetwork?.observe(this, Observer { fetchPair->
                networkCall(fetchPair)
            })
        }
        lifecycle.addObserver(viewModel)


        val btnShow = findViewById<Button>(R.id.btnShowList)
        btnShow.setOnClickListener{
            showBottomLocations()
        }

        var mapFragment = getActivity()
            .supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(R.id.mapView, mapFragment).commit()
        }
        mapFragment?.let {
            mapFragment.getMapAsync(this)
        }
    }


    private fun networkCall(fetchPair: Pair<Boolean,Boolean>) {
        if(fetchPair.first){
            networkProgress.visibility = View.VISIBLE
            dataStatus.visibility = View.INVISIBLE
            return
        }
        // throttling network loader so that it doesn't look glitchy
        Handler().postDelayed({
            networkProgress.visibility = View.INVISIBLE
        },1000)
        // network call failed, cache copy(if any) was returned
        if(!fetchPair.second) {
            dataStatus.visibility = View.VISIBLE
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
        if(!::mMap.isInitialized) {
            Log.e(TAG,"map is null while plotting points ")
            return
        }
        for(pinPoint in pinPoints) {
            val userLocation = LatLng(pinPoint.latitude, pinPoint.longitude)
            mMap.addMarker(MarkerOptions().position(userLocation)
                .title(pinPoint.name)
                .snippet(pinPoint.description))
        }
    }

    private fun showBottomLocations() {
        val addBottomDialogFragment = LocationFragment()
        addBottomDialogFragment.show(
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
            location?.let {
                val userLocation = LatLng(location.latitude,location.longitude)
                mMap.addMarker(MarkerOptions()
                    .position(userLocation).title(resources.getString(R.string.user_location_status))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,14f))

            }
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
