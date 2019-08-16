package droid.ankit.gotennademo

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etiennelenhart.eiffel.viewmodel.delegate.providedViewModel
import androidx.recyclerview.widget.DividerItemDecoration




class LocationFragment: BottomSheetDialogFragment() {

    private val viewModel by providedViewModel<MapViewModel>()
    private val TAG:String = LocationFragment::class.java.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bottom, container,false)
        context?.let {
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            val locationAdapter = LocationAdapter(context!!)
            recyclerView.adapter = locationAdapter

            viewModel.getCachedPinPoints()
            //viewModel.test.
            viewModel.observeState(this.viewLifecycleOwner){
                Log.e(TAG,"observe $it")
                it.pinPointList?.observe(this,Observer {pinPoints->
                    locationAdapter.setData(pinPoints)
                })
            }
            lifecycle.addObserver(viewModel)
        }

        return view
    }

}