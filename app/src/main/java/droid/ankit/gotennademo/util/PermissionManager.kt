package droid.ankit.gotennademo.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import droid.ankit.gotennademo.base.BaseActivity

class PermissionManager(private val context: Context){

    fun checkLocationPermission(permissionsCallback:PermissionCallback) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(permissionsCallback.getActivity()!!,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionsCallback.showPermissionRationale()
            } else {
                ActivityCompat.requestPermissions(permissionsCallback.getActivity()!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_FINE_LOCATION
                )
            }
        }else{
            permissionsCallback.permissionGranted()
        }
    }

    fun showPermissionDialog(permissionsCallback:PermissionCallback) {
        ActivityCompat.requestPermissions(permissionsCallback.getActivity()!!,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSIONS_FINE_LOCATION
        )
    }

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>, grantResults: IntArray,
                                   permissionsCallback:PermissionCallback){
        when (requestCode) {
            MY_PERMISSIONS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    permissionsCallback.permissionGranted()
                }else{
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (permissionsCallback.getActivity()!!,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        permissionsCallback.showPermissionRationale()
                    }else{
                        permissionsCallback.permissionBlocked()
                    }
                }
                return
            }
        }
    }

    fun isPermissionGranted():Boolean {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED)
    }

    companion object {
        private const val MY_PERMISSIONS_FINE_LOCATION = 3434
    }
}



interface PermissionCallback {
    fun showPermissionRationale()
    fun permissionBlocked()
    fun permissionGranted()
    fun getActivity():BaseActivity?
}

