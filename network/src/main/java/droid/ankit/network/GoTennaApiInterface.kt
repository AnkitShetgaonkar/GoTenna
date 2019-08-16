package droid.ankit.network

import droid.ankit.database.PinPoint
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface GoTennaApiInterface {
    @GET("get_map_pins.php")
    fun getPins():Deferred<Response<List<PinPoint>>>
}