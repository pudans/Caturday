package pudans.caturday

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import pudans.caturday.model.Video
import pudans.caturday.repository.FeedRepository
import pudans.caturday.repository.LikeRepository
import pudans.caturday.state.FeedItemState
import pudans.caturday.state.FeedScreenState
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class FeedViewModel
@Inject constructor(
	private val mFirebaseAuth: FirebaseAuth,
	private val mFeedRepository: FeedRepository,
	private val mFeedLikeRepository: LikeRepository
) : ViewModel() {

	private val mFeedScreenState = mutableStateOf<FeedScreenState>(FeedScreenState.Loading)

	init {
		viewModelScope.launch {
			mFeedRepository.getFeed()
				.onStart { mFeedScreenState.value = FeedScreenState.Loading }
				.collect { result ->
					mFeedScreenState.value = when {
						result.isNotEmpty() -> FeedScreenState.Data(result.mapIndexed { index, video ->
							generateFeedItemState(video, index, result)
						})
						else -> FeedScreenState.Empty
					}
				}
		}
	}

	private fun generateFeedItemState(video: Video, index: Int, list: List<Video>) = FeedItemState(
		videoId = video.id ?: "",
		videoUrl = video.url ?: "",
		videoPreviewUrl = video.preview?.url ?: "",
		uploaderAvatarUrl = video.uploader?.photoUrl ?: "",
		uploaderNick = "@${video.uploader?.email?.split('@')?.first()}",
		likedNicks = "Likes: ${video.likedEmails?.joinToString(", ") { "@${it.split('@').first()}" }}",
		likesCount = video.likedEmails?.size ?: 0,
		isLikedByUser = video.likedEmails?.contains(mFirebaseAuth.currentUser?.email) ?: false,
		numberOfVideos = "${index + 1}/${list.size}",
	)

	fun observeFeedScreenState(): State<FeedScreenState> = mFeedScreenState

	fun likeOrDislike(videoId: String) {

		viewModelScope.launch {
			mFeedLikeRepository.getLikedVideos(videoId).collect {

			}
		}

	}
}