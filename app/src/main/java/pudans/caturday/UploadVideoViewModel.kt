package pudans.caturday

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
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

	fun onFrame(bitmap: Bitmap) {
		val image = InputImage.fromBitmap(bitmap, 0)
		val options = ImageLabelerOptions.Builder()
			.setConfidenceThreshold(0.1f)
			.build()

		val labeler = ImageLabeling.getClient(options)

		labeler.process(image)
			.addOnSuccessListener { labels ->
				Log.d("asdddddd success", labels.toString())
				labels.forEach {
					if (it.text == "Cat") {
						Log.d("dfffffdf success", "$it")
					}
				}
			}
			.addOnFailureListener { e ->
				Log.d("asdddddd error", e.toString())
			}
	}
}