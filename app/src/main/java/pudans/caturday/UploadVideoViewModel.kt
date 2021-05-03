
package pudans.caturday

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pudans.caturday.repository.FeedRepository
import pudans.caturday.repository.MainRepository
import pudans.caturday.repository.UploadFileRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * Showcases different patterns using the liveData coroutines builder.
 */
@HiltViewModel
class UploadVideoViewModel
@Inject constructor(
	private val mUploadFileRepository: UploadFileRepository
) : ViewModel() {

	private val _name = MutableLiveData("")
	val imageUrl: LiveData<String> = _name

	fun onRefresh(uri: Uri) {

		mUploadFileRepository.upload(uri)

	}
}