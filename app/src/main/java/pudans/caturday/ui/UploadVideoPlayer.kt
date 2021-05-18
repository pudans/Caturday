package pudans.caturday.ui

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.OPTION_CLOSEST
import android.net.Uri
import android.view.TextureView
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

@Composable
fun UploadVideoPlayer(
	uri: Uri,
	isMuted: Boolean,
	onFrameBitmap: (Bitmap?) -> (Unit)
) {
	val context = LocalContext.current
	val retriever = MediaMetadataRetriever()


	val exoPlayer = remember {
		SimpleExoPlayer.Builder(context).build().apply {
			playWhenReady = false
			videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT
			repeatMode = Player.REPEAT_MODE_OFF

			var lastFrame = 0L
			setVideoFrameMetadataListener { presentationTimeUs, _, _, _ ->

				if (lastFrame == 0L || (System.currentTimeMillis() - lastFrame > 1000L)) {
					val bitmap = retriever.getFrameAtTime(presentationTimeUs, OPTION_CLOSEST)
					onFrameBitmap.invoke(bitmap)
					lastFrame = System.currentTimeMillis()
				}
			}
		}
	}

	LaunchedEffect(uri) {
		exoPlayer.playWhenReady = true
		exoPlayer.setMediaItem(MediaItem.fromUri(uri))
		exoPlayer.prepare()

		retriever.setDataSource(context, uri)
	}

	LaunchedEffect(isMuted) {
		exoPlayer.volume = if (isMuted) 0f else 1f
	}

	DisposableEffect(
		AndroidView(
			factory = {
				TextureView(it).apply {
					layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
					exoPlayer.setVideoTextureView(this)
				}
			})
	) {

		onDispose {
			retriever.release()
			exoPlayer.release()
		}
	}
}