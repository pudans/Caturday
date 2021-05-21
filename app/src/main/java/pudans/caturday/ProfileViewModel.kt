package pudans.caturday

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import pudans.caturday.model.Video
import pudans.caturday.repository.ProfileLikedRepository
import pudans.caturday.repository.ProfileUploadedRepository
import pudans.caturday.state.ProfileState
import pudans.caturday.state.ProfileVideoItemState
import pudans.caturday.state.ProfileVideoListState
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel
@Inject constructor(
	firebaseAuth: FirebaseAuth,
	private val mLikedRepository: ProfileLikedRepository,
	private val mUploadedRepository: ProfileUploadedRepository
) : ViewModel() {

	private val mLikedVideosState = mutableStateOf<ProfileVideoListState>(ProfileVideoListState.Loading)
	private val mUploadedVideosState = mutableStateOf<ProfileVideoListState>(ProfileVideoListState.Loading)
	private val mScreenState = mutableStateOf(createProfileState(firebaseAuth.currentUser))

	init {
		viewModelScope.launch {
			mLikedRepository.getLikedVideos()
				.onStart { mLikedVideosState.value = ProfileVideoListState.Loading }
				.collect { result ->
					mLikedVideosState.value = when {
						result.isNotEmpty() -> ProfileVideoListState.Data(result.map { createProfileVideoItemState(it) })
						else -> ProfileVideoListState.Empty
					}
				}
		}

		viewModelScope.launch {
			mUploadedRepository.getUploadedVideos()
				.onStart { mUploadedVideosState.value = ProfileVideoListState.Loading }
				.collect { result ->
					mUploadedVideosState.value = when {
						result.isNotEmpty() -> ProfileVideoListState.Data(result.map { createProfileVideoItemState(it) })
						else -> ProfileVideoListState.Empty
					}
				}
		}
	}

	fun observeLikedVideosState(): State<ProfileVideoListState> = mLikedVideosState

	fun observeUploadedVideosState(): State<ProfileVideoListState> = mUploadedVideosState

	fun observeProfileState(): State<ProfileState> = mScreenState

	fun onVideoClick(videoId: String) {
		// TODO
	}

	private fun createProfileState(user: FirebaseUser?): ProfileState = ProfileState(
		title = user?.displayName ?: "",
		avatarUrl = user?.photoUrl?.toString() ?: "",
		nick = "@" + user?.email?.split("@")?.first()
	)

	private fun createProfileVideoItemState(video: Video) = ProfileVideoItemState(
		id = video.videoName,
		url = video.previewUrl
	)
}