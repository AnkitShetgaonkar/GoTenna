package droid.ankit.gotennademo.base

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewState
import com.etiennelenhart.eiffel.viewmodel.StateViewModel
import org.koin.standalone.KoinComponent

open class BaseViewModel<T: ViewState>: StateViewModel<T>(), KoinComponent {
    override val state = MutableLiveData<T>()
}
