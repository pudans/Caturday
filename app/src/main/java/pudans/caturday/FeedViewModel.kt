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
import pudans.caturday.state.FeedItemState
import pudans.caturday.state.FeedScreenState
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class FeedViewModel
@Inject constructor(
	private val mFirebaseAuth: FirebaseAuth,
	private val mFeedRepository: FeedRepository
) : ViewModel() {

	private val mFeedScreenState = mutableStateOf<FeedScreenState>(FeedScreenState.Loading)

	init {
		viewModelScope.launch {
			mFeedRepository.getFeed()
				.onStart { mFeedScreenState.value = FeedScreenState.Loading }
				.collect { result ->
					mFeedScreenState.value = when {
						result.isNotEmpty() -> FeedScreenState.Data(result.map { generateFeedItemState(it) })
						else -> FeedScreenState.Empty
					}
				}
		}
	}

	private fun generateFeedItemState(video: Video) = FeedItemState(
		videoUrl = video.videoUrl,
		videoPreviewUrl = video.previewUrl,
		uploaderAvatarUrl = video.uploaderAvatarUrl,
		uploaderNick = "@${video.uploaderEmail.split('@').first()}",
		likedNicks = "Likes: ${video.likedEmails.joinToString(", ") { "@${video.uploaderEmail.split('@').first()}" }}",
		likesCount = video.likedEmails.size,
		isLikedByUser = video.likedEmails.contains(mFirebaseAuth.currentUser?.email)
	)

	fun observeFeedScreenState(): State<FeedScreenState> = mFeedScreenState
}