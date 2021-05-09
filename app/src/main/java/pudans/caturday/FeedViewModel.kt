
package pudans.caturday

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pudans.caturday.model.FeedItem
import pudans.caturday.repository.FeedRepository
import pudans.caturday.repository.MainRepository
import pudans.caturday.ui.Feed
import timber.log.Timber
import javax.inject.Inject

/**
 * Showcases different patterns using the liveData coroutines builder.
 */
@HiltViewModel
class FeedViewModel
@Inject constructor(
	private val dataSource: MainRepository,
	private val mFeedRepository: FeedRepository
) : ViewModel() {

	private val mFeedState = mutableStateOf<Array<FeedItem>>(emptyArray())

	private val _name = MutableLiveData("")
	val imageUrl: LiveData<String> = _name

	init {

		mFeedState.value = arrayOf(
			FeedItem("id", "https://firebasestorage.googleapis.com/v0/b/caturday-a9a65.appspot" +
				".com/o/videos%2F3316441d-7ae5-4be3-acc9-3aac31605013.mp4?alt=media&token=f1dc1330-f623-463c-9e60-693e6804f46f"),
			FeedItem("id1", "https://firebasestorage.googleapis.com/v0/b/caturday-a9a65.appspot" +
				".com/o/videos%2Ff9ed2536-746a-41b5-b865-7fb605cfd0e3.mp4?alt=media&token=bf84c9c4-744b-4892-a99f-e2fa5694cb3e"),
			FeedItem("id2", "https://firebasestorage.googleapis.com/v0/b/caturday-a9a65.appspot" +
				".com/o/videos%2F8c9eba9c-3004-4d78-834d-ec5ec014e276.mp4?alt=media&token=735b29ea-c120-4bb5-bbf5-6aa06a419fac"),
		)
	}

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



		mFeedRepository.getFeed()

	}

	fun observeFeedItems(): MutableState<Array<FeedItem>> = mFeedState

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