package droid.ankit.network

import android.util.Log
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

open class BaseServiceImpl {
    suspend fun <T : Any> safeApiCall(call : suspend()-> Response<T>, error : String) :  T?{
        val result :Result<T> = safeApiResponse(call, error)
        var output : T? = null
        when(result){
            is Result.Success -> {
                output = result.data
            }
            is Result.Error -> Log.e("Error", "The $error and the ${result.exception}")
        }
        return output
    }
    private suspend fun<T : Any> safeApiResponse(call: suspend()-> Response<T>, error: String) : Result<T>{
        return try {
            val response = call.invoke()
            if (response.isSuccessful)
                Result.Success(response.body()!!)
            else
                Result.Error(IOException("OOps .. Something went wrong due to  $error"))
        }catch (ex:Exception){
            Result.Error(ex)
        }
    }
}