package pudans.caturday

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pudans.caturday.model.Video
import pudans.caturday.model.VideosResult
import pudans.caturday.repository.ProfileLikedRepository
import pudans.caturday.repository.ProfileUploadedRepository
import pudans.caturday.state.ProfileState
import pudans.caturday.state.ProfileVideoItemState
import pudans.caturday.state.ProfileVideoListState
import javax.inject.Inject

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
				.collect { result ->
					mLikedVideosState.value = when {
						result is VideosResult.Data && result.data.isNotEmpty() ->
							ProfileVideoListState.Data(result.data.map { createProfileVideoItemState(it) })
						result is VideosResult.Loading -> ProfileVideoListState.Loading
						else -> ProfileVideoListState.Empty
					}
				}
		}

		viewModelScope.launch {
			mUploadedRepository.getUploadedVideos()
				.collect { result ->
					mUploadedVideosState.value = when {
						result is VideosResult.Data && result.data.isNotEmpty() ->
							ProfileVideoListState.Data(result.data.map { createProfileVideoItemState(it) })
						result is VideosResult.Loading -> ProfileVideoListState.Loading
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
		id = video.id ?: "",
		url = video.preview?.url ?: ""
	)
}