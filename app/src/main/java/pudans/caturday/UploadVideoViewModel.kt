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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pudans.caturday.repository.MLVisionRepository
import pudans.caturday.repository.UploadFileRepository
import pudans.caturday.state.CheckerItemState
import javax.inject.Inject


@HiltViewModel
class UploadVideoViewModel
@Inject constructor(
	private val mUploadFileRepository: UploadFileRepository,
	private val mCatCheckerRepository: MLVisionRepository
) : ViewModel() {


	private val mFileSelectorState = MutableLiveData<Boolean>()
	private val mVideoIsMuted = mutableStateOf(false)
	private val mCheckerItemsState = mutableStateOf(emptyList<CheckerItemState>())
	private val mSourceState = mutableStateOf<Uri?>(null)
	private val mUploadButtonState = mutableStateOf(false)

	fun getVideoSourceState(): State<Uri?> = mSourceState

	fun getFileSelectorState(): LiveData<Boolean> = mFileSelectorState

	fun getVideMutedState(): State<Boolean> = mVideoIsMuted

	fun getCheckerItemsState(): State<List<CheckerItemState>> = mCheckerItemsState

	fun getUploadButtonState(): State<Boolean> = mUploadButtonState

	fun setVideoSource(uri: Uri?) {
		mVideoIsMuted.value = true
		mFileSelectorState.value = false
		mSourceState.value = uri
	}

	fun onSelectSourceClick() {
		mFileSelectorState.value = true
		mUploadButtonState.value = false
	}

	fun onUploadVideoClick() {
		mUploadFileRepository.doWork(mSourceState.value!!)
	}

	fun onChangeMuteState() {
		mVideoIsMuted.value = mVideoIsMuted.value.not()
	}

	fun onFrameBitmapChanged(bitmap: Bitmap?) {

		bitmap?.let {
			viewModelScope.launch {
				mCatCheckerRepository.work(bitmap).collect { states ->
					mCheckerItemsState.value = states
					mUploadButtonState.value = states.any { it.labelName == "Cat" }
				}
			}
		}
	}
}