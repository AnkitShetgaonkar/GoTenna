package droid.ankit.gotennademo

import android.os.Handler
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import droid.ankit.database.PinPoint
import droid.ankit.gotennademo.base.BaseActivity
import droid.ankit.gotennademo.base.BaseViewModel
import droid.ankit.gotennademo.util.PermissionCallback
import org.koin.standalone.inject


class MapViewModel:BaseViewModel<MapViewState>(),LifecycleObserver, PermissionCallback {

    private val mDataRepository:DataRepository by inject()
    private val mNetworkStatus:MutableLiveData<Pair<Boolean,Boolean>> = MutableLiveData()

    init {
        state.value = MapViewState()
        updateState { it.copy(fetchingFromNetwork = mNetworkStatus) }
    }

    override fun showPermissionRationale() {
        updateState { it.copy(event=MapScreenEvent.ShowPermissionRationale
            (R.string.user_location_needed)) }
    }

    override fun permissionBlocked() {
        updateState { it.copy(event=MapScreenEvent.PermissionDenied
            (R.string.user_location_denied_message)) }
    }

    override fun permissionGranted() {
        updateState { it.copy(event=MapScreenEvent.PermissionGranted()) }
    }

    override fun getActivity(): BaseActivity? {
        //do nothing
        return null
    }

    override fun notifyPermissionDialogDismissed() {
        //do nothing
    }

    fun getPinPoints() {
        updateState { it.copy(pinPointList = mDataRepository.getPinPoints(true,mNetworkStatus)) }
    }

    fun getCachedPinPoints() {
        updateState { it.copy(pinPointList = mDataRepository.getPinPoints(false,mNetworkStatus)) }
    }

}

sealed class MapScreenEvent:ViewEvent(){
    class ShowPermissionRationale(val message:Int):MapScreenEvent()
    class PermissionDenied(val message:Int):MapScreenEvent()
    class PermissionGranted:MapScreenEvent()
}

data class MapViewState(val pinPointList :LiveData<List<PinPoint>>?=null,
                        val fetchingFromNetwork: LiveData<Pair<Boolean,Boolean>>?=null,
                        val event:MapScreenEvent?=null):ViewState

