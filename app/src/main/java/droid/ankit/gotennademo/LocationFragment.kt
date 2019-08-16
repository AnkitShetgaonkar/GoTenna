package droid.ankit.gotennademo

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etiennelenhart.eiffel.state.peek
import com.etiennelenhart.eiffel.viewmodel.delegate.providedViewModel
import droid.ankit.database.PinPoint
import kotlinx.android.synthetic.main.fragment_bottom.*


class LocationFragment: BottomSheetDialogFragment() {

    private val viewModel by providedViewModel<MapViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bottom, container,false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val locationAdapter = LocationAdapter(context!!)
        recyclerView.adapter = LocationAdapter(context!!)


        viewModel.observeState(this){
            Log.e("MapFragment ","test $it")
            it.pinPointList?.observe(this,androidx.lifecycle.Observer {pinPoints->
                Log.e("Live Data in fragments ","printing out size  "+pinPoints.size)
                locationAdapter.setData(pinPoints)
            })
        }
        lifecycle.addObserver(viewModel)

        return view
    }

}