package droid.ankit.network
import android.content.Context
import droid.ankit.database.PinPoint

class FakeApi(private val context: Context) {

    fun getPinPoint():List<PinPoint> {
        Thread.sleep(2000)
        return listOf(PinPoint(0,"test1","descrip1",0.0,0.0),
            PinPoint(1,"test2","descrip2",0.0,0.0))
    }

}

