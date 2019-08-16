package droid.ankit.network

import droid.ankit.database.PinPoint

class GoTennaServiceImpl: BaseServiceImpl() {

    private val apiInterface:GoTennaApiInterface = GoTennaService.goTennaApiInterface

    suspend fun getPins(): List<PinPoint>? {
            return safeApiCall(
                //await the result of deferred type
                call = {apiInterface.getPins().await()},
                error = "Error fetching pins"
            )
    }
}