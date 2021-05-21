package pudans.caturday.ui

import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pudans.caturday.FeedViewModel
import pudans.caturday.state.FeedItemState
import pudans.caturday.state.FeedScreenState

@ExperimentalAnimationApi
@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun FeedScreen() {

	val viewModel = hiltViewModel<FeedViewModel>()

	val state by viewModel.observeFeedScreenState()

	Box(
		modifier = Modifier
			.background(color = Color.Black)
			.fillMaxSize()
	) {

		when (state) {
			is FeedScreenState.Data -> FeedList(items = (state as FeedScreenState.Data).items)
			FeedScreenState.Empty -> EmptyList()
			FeedScreenState.Loading -> Loading()
		}
	}
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
private fun FeedList(
	items: List<FeedItemState>
) {

	val pagerState = rememberPagerState(pageCount = items.size)

//	snapshotFlow { pagerState.currentPage }.collectAsState(initial = 0)

	VerticalPager(
		state = pagerState
	) { page ->

		val itemState = items[page]

		Box(
			modifier = Modifier
				.fillMaxSize()
				.clip(shape = RoundedCornerShape(16.dp)),
		) {

			FeedVideoPlayer(
				videoUrl = itemState.videoUrl,
				previewUrl = itemState.videoPreviewUrl,
				isPlayWhenReady = page == pagerState.currentPage
			)

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.align(Alignment.BottomCenter)
					.padding(16.dp)
			) {

				Text(
					text = itemState.uploaderNick,
					color = Color.White
				)

				Spacer(modifier = Modifier.height(4.dp))

				Text(
					text = itemState.likedNicks,
					color = Color.White
				)
			}

			Column(
				modifier = Modifier
					.align(Alignment.CenterEnd)
					.width(80.dp)
					.padding(16.dp)
			) {

				Image(
					painter = rememberCoilPainter(
						request = itemState.uploaderAvatarUrl,
						fadeIn = true
					),
					contentDescription = "",
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.size(48.dp)
						.align(alignment = Alignment.CenterHorizontally)
						.clip(CircleShape)
						.border(2.dp, Color.White, CircleShape)
				)

				Spacer(modifier = Modifier.height(16.dp))

				Icon(
					imageVector = Icons.Filled.Favorite,
					tint = Color.White,
					contentDescription = "",
					modifier = Modifier.size(48.dp)
				)

				Spacer(modifier = Modifier.height(4.dp))

				Text(
					text = itemState.likesCount.toString(),
					textAlign = TextAlign.Center,
					color = Color.White,
					modifier = Modifier.fillMaxWidth()
				)
			}
		}
	}
}

@Composable
private fun EmptyList() {
	Box(modifier = Modifier.fillMaxSize()) {
		Text(
			text = "EMPTY",
			textAlign = TextAlign.Center,
			color = Color.White,
			modifier = Modifier.align(Alignment.Center)
		)
	}
}

@Composable
private fun Loading() {
	Box(modifier = Modifier.fillMaxSize()) {
		CircularProgressIndicator(
			modifier = Modifier
				.size(64.dp)
				.align(Alignment.Center),
			color = Color.White
		)
	}
}


@ExperimentalAnimationApi
@Composable
fun FeedVideoPlayer(
	videoUrl: String,
	previewUrl: String,
	isPlayWhenReady: Boolean
) {
	val context = LocalContext.current

//	val coroutineScope = rememberCoroutineScope()

	var playPauseState by remember { mutableStateOf(true) }

	var previewImageState by remember { mutableStateOf(true) }

//	var progressState by remember { mutableStateOf(0.0f) }

	val exoPlayer = remember {
		SimpleExoPlayer.Builder(context).build().apply {
			playWhenReady = false
			videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT
			repeatMode = Player.REPEAT_MODE_ALL

			addListener(object : Player.Listener {
				override fun onRenderedFirstFrame() {
					previewImageState = false
				}
			})
		}
	}

	LaunchedEffect(videoUrl) {
		exoPlayer.setMediaItem(MediaItem.fromUri(videoUrl))
		exoPlayer.prepare()

//		coroutineScope.launch {
//			while (exoPlayer.playWhenReady) {
//				val duration = exoPlayer.contentDuration
//				val position = exoPlayer.contentPosition
//
//				Log.d("qwerttttt", "$position $duration")
//
//				delay(100L)
//			}
//		}
	}

	LaunchedEffect(isPlayWhenReady, playPauseState) {
		exoPlayer.playWhenReady = isPlayWhenReady && playPauseState


	}

	Box(
		modifier = Modifier.fillMaxSize().clickable { playPauseState = !playPauseState }
	) {
		DisposableEffect(
			AndroidView(
				factory = {
					TextureView(it).apply {
						layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
						exoPlayer.setVideoTextureView(this)
					}
				})
		) {

			onDispose {
				exoPlayer.release()
			}
		}

//		LinearProgressIndicator(
//			progress = 0.1f,
//			modifier = Modifier
//				.fillMaxWidth()
//				.height(4.dp)
//				.align(Alignment.BottomCenter)
//				.padding(start = 16.dp, end = 16.dp)
//				.clip(shape = RoundedCornerShape(2.dp)),
//			backgroundColor = Color.Transparent,
//			color = Color.Red,
//		)

		AnimatedVisibility(
			visible = previewImageState,
			modifier = Modifier.fillMaxSize(),
			enter = fadeIn(),
			exit = fadeOut()
		) {
			Image(
				contentScale = ContentScale.Crop,
				painter = rememberCoilPainter(request = previewUrl),
				contentDescription = ""
			)
		}

		Box(
			modifier = Modifier.fillMaxSize()
		) {
			AnimatedVisibility(
				visible = !playPauseState,
				modifier = Modifier
					.size(56.dp)
					.align(Alignment.Center),
				enter = fadeIn(),
				exit = fadeOut()
			) {
				Icon(
					imageVector = Icons.Filled.PlayCircleOutline,
					contentDescription = "",
					tint = Color.White
				)
			}
		}
	}
}