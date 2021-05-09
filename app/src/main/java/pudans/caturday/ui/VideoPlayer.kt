package pudans.caturday.ui

import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun VideoPlayer(
	page: Int,
	currentPage: Int,
	uri: String
): MediaPlayback {
	val context = LocalContext.current

	Log.d("qwerty", "VideoPlayer $page $currentPage")

	val exoPlayer = remember {
		SimpleExoPlayer.Builder(context).build().apply {
			playWhenReady = false
			videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT
			repeatMode = Player.REPEAT_MODE_ALL
			setMediaItem(MediaItem.fromUri(uri))
			prepare()
		}
	}

	LaunchedEffect(page, currentPage) {
		exoPlayer.playWhenReady = page == currentPage
	}

	DisposableEffect(
		AndroidView(
			factory = {
				PlayerView(it).apply {
					layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
					hideController()
					useController = false
					resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
					player = exoPlayer
				}
			})
	) {

		onDispose {
			exoPlayer.release()
		}
	}

	return remember(exoPlayer) {
		object : MediaPlayback {
			override fun playPause() {
				exoPlayer.playWhenReady = !exoPlayer.playWhenReady
			}

			override fun forward(durationInMillis: Long) {
				exoPlayer.seekTo(exoPlayer.currentPosition + durationInMillis)
			}

			override fun rewind(durationInMillis: Long) {
				exoPlayer.seekTo(exoPlayer.currentPosition - durationInMillis)
			}
		}
	}
}