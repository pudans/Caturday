package pudans.caturday

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pudans.caturday.repository.MLVisionRepository
import pudans.caturday.repository.UploadFileRepository
import pudans.caturday.state.CheckerItemState
import pudans.caturday.state.UploadVideoState
import javax.inject.Inject
import kotlin.math.min


@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class UploadVideoViewModel
@Inject constructor(
	private val mUploadFileRepository: UploadFileRepository,
	private val mCatCheckerRepository: MLVisionRepository
) : ViewModel() {

	private val mFileSelectorState = MutableLiveData<Boolean>()
	private val mCheckerItemsState = mutableStateOf(emptyList<CheckerItemState>())
	private val mSourceState = mutableStateOf<Uri?>(null)
	private val mUploadButtonState = mutableStateOf(false)
	private val mUploadState = mutableStateOf<UploadVideoState>(UploadVideoState.Default)

	fun getVideoSourceState(): State<Uri?> = mSourceState

	fun getFileSelectorState(): LiveData<Boolean> = mFileSelectorState

	fun getCheckerItemsState(): State<List<CheckerItemState>> = mCheckerItemsState

	fun getUploadButtonState(): State<Boolean> = mUploadButtonState

	fun getUploadState(): State<UploadVideoState> = mUploadState

	fun setVideoSource(uri: Uri?) {
		mFileSelectorState.value = false
		mSourceState.value = uri
		mCatCheckerRepository.setUri(uri!!)
	}

	fun onUploadVideoClick() {
		viewModelScope.launch {
			mUploadFileRepository.doWork(mSourceState.value!!).collect {
				mUploadState.value = it
			}
		}
	}

	fun onFrameBitmapChanged(bitmap: Long?) {
		bitmap?.let {
			viewModelScope.launch {
				mCatCheckerRepository.work(bitmap).collect { labels ->

					val states = labels
						.subList(0, min(10, labels.size))
						.map {
							CheckerItemState(
								name = it.text,
								value = String.format("%.1f%%", it.confidence * 100),
								isAccent = it.text == "Cat"
							)
						}

					mCheckerItemsState.value = states
					mUploadButtonState.value = mUploadButtonState.value || states.any { it.name == "Cat" }
				}
			}
		}
	}
}