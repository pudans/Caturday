
package pudans.caturday

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pudans.caturday.model.Video
import pudans.caturday.repository.FeedRepository
import pudans.caturday.repository.MainRepository
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

	private val mFeedState = mutableStateOf<Array<Video>>(emptyArray())

	fun onRefresh() {
		viewModelScope.launch {

			mFeedRepository.getFeed().collect {
				mFeedState.value = it.toTypedArray()
			}
		}
	}

	fun observeFeedItems(): State<Array<Video>> = mFeedState
}