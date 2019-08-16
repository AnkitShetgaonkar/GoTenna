package droid.ankit.gotennademo

import android.util.Log
import androidx.lifecycle.LiveData
import droid.ankit.database.PinPoint
import droid.ankit.database.PinPointDao
import droid.ankit.network.GoTennaServiceImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject



class DataRepository:KoinComponent,DataService {

    private val pinPointDao:PinPointDao by inject()
    private val api:GoTennaServiceImpl by inject()


    override fun getPinPoints(): LiveData<List<PinPoint>> {
        refreshPinPoints()
        return pinPointDao.loadAll()
    }

    private fun refreshPinPoints(){
        GlobalScope.launch {
            val networkPinPoints :List<PinPoint>? = api.getPins()
            if (networkPinPoints != null) {
                pinPointDao.saveAll(networkPinPoints)
            }
        }
    }
}

interface DataService {
    fun getPinPoints():LiveData<List<PinPoint>>?
}