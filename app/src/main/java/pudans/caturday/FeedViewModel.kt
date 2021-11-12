package pudans.caturday

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pudans.caturday.model.Video
import pudans.caturday.model.VideosResult
import pudans.caturday.repository.FeedRepository
import pudans.caturday.repository.LikeRepository
import pudans.caturday.state.FeedItemState
import pudans.caturday.state.FeedScreenState
import javax.inject.Inject

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
				.collect { result ->
					mFeedScreenState.value = when (result) {
						is VideosResult.Data -> {
							FeedScreenState.Data(result.data.mapIndexed { index, video ->
								generateFeedItemState(video, index, result.data)
							})
						}
						VideosResult.Error -> FeedScreenState.Empty
						VideosResult.Loading -> FeedScreenState.Loading
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
			mFeedLikeRepository.likeOrDislike(videoId).collect {}
		}
	}
}