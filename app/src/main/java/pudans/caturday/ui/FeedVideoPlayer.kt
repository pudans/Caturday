package pudans.caturday.ui

import android.util.Log
import android.view.TextureView
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.PlayForWork
import androidx.compose.material.icons.filled.PlayLesson
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.TapAndPlay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

@Composable
fun FeedVideoPlayer(
	uri: String,
	isPlayWhenReady: Boolean
) {
	val context = LocalContext.current

	var playPauseState by remember { mutableStateOf(true) }

	val exoPlayer = remember {
		SimpleExoPlayer.Builder(context).build().apply {
			playWhenReady = false
			videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT
			repeatMode = Player.REPEAT_MODE_ALL
			setMediaItem(MediaItem.fromUri(uri))
			prepare()
		}
	}

	LaunchedEffect(isPlayWhenReady, playPauseState) {
		exoPlayer.playWhenReady = isPlayWhenReady && playPauseState
	}

	Box {
		DisposableEffect(
			AndroidView(
				factory = {
					TextureView(it).apply {
						layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
						exoPlayer.setVideoTextureView(this)
						setOnClickListener {
							playPauseState = !playPauseState
						}
					}
				})
		) {

			onDispose {
				exoPlayer.release()
			}
		}

		if (!playPauseState) {
			Icon(
				imageVector = Icons.Filled.PlayCircleOutline,
				contentDescription = "",
				modifier = Modifier.size(56.dp).align(Alignment.Center),
				tint = Color.White
			)
		}
	}
}