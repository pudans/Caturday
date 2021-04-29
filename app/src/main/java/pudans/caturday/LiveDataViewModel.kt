
package pudans.caturday

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

/**
 * Showcases different patterns using the liveData coroutines builder.
 */
@HiltViewModel
class LiveDataViewModel
@Inject constructor(
	private val dataSource: MainRepository
) : ViewModel() {

	// Exposed LiveData from a function that returns a LiveData generated with a liveData builder
//	val currentTime = dataSource.getCurrentTime()

	// Coroutines inside a transformation
//	val currentTimeTransformed = currentTime.switchMap {
//		// timeStampToTime is a suspend function so we need to call it from a coroutine.
//		liveData { emit(timeStampToTime(it)) }
//	}

	// Exposed liveData that emits and single value and subsequent values from another source.
//	val currentWeather: LiveData<String> = liveData {
//		emit(LOADING_STRING)
//		emitSource(dataSource.fetchWeather())
//	}

	// Exposed cached value in the data source that can be updated later on
//	val cachedValue = dataSource.cachedData

	// Called when the user clicks on the "FETCH NEW DATA" button. Updates value in data source.

	private val _name = MutableLiveData("")
	val imageUrl: LiveData<String> = _name

	fun onRefresh() {
		// Launch a coroutine that reads from a remote data source and updates cache
		val data = viewModelScope.launch {

			val info = dataSource.getImages()

			info.collect {
				Timber.d(it?.toString())

				_name.value = it!!.first().url
			}

//			Timber.d("result", info)
		}

		data.start()


	}

	// Simulates a long-running computation in a background thread
//	private suspend fun timeStampToTime(timestamp: Long): String {
//		delay(500)  // Simulate long operation
//		val date = Date(timestamp)
//		return date.toString()
//	}

//	companion object {
//		// Real apps would use a wrapper on the result type to handle this.
//		const val LOADING_STRING = "Loading..."
//	}
}


/**
 * Factory for [LiveDataViewModel].
 */
//object LiveDataVMFactory : ViewModelProvider.Factory {
//
//	private val dataSource = MainRepository(
//
//	)
//
//	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//		@Suppress("UNCHECKED_CAST")
//		return LiveDataViewModel(dataSource) as T
//	}
//}
