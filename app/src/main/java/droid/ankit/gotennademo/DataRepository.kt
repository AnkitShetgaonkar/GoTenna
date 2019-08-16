package droid.ankit.gotennademo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    override fun getPinPoints(refresh: Boolean, status:MutableLiveData<Pair<Boolean,Boolean>>): LiveData<List<PinPoint>> {
        if(refresh)
            refreshPinPoints(status)
        return pinPointDao.loadAll()
    }

    private fun refreshPinPoints(status:MutableLiveData<Pair<Boolean,Boolean>>){
        var pair = Pair(first=true,second=false)
        status.postValue(pair)
        GlobalScope.launch {
            val networkPinPoints :List<PinPoint>? = api.getPins()
            if (networkPinPoints != null) {
                pair = Pair(first=false,second=true)
                pinPointDao.saveAll(networkPinPoints)
            }else{
                pair = Pair(first=false,second=false)
            }
            status.postValue(pair)
        }
    }
}

interface DataService {
    fun getPinPoints(refresh: Boolean,status:MutableLiveData<Pair<Boolean,Boolean>>):LiveData<List<PinPoint>>?
}