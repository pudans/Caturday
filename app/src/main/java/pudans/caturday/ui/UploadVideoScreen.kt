package pudans.caturday.ui

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import pudans.caturday.UploadVideoViewModel
import pudans.caturday.state.CheckerItemState

@Composable
fun UploadVideoScreen(
	onCloseClick: () -> Unit
) {

	val viewModel: UploadVideoViewModel = hiltViewModel<UploadVideoViewModel>()

	Scaffold(
		topBar = { Toolbar(onCloseClick) },
		backgroundColor = Color.White,
		contentColor = Color.White
	) {

		Column(
			modifier = Modifier
				.padding(24.dp)
				.fillMaxSize()
		) {

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
			) {

				Column(
					modifier = Modifier
						.fillMaxWidth()
						.weight(1f)
				) {

					Box(
						modifier = Modifier
							.fillMaxWidth()
							.aspectRatio(0.56f)
							.clip(RoundedCornerShape(24.dp))
							.clickable { viewModel.onSelectSourceClick() }
							.background(color = Color.LightGray)
					) {

						val uri by viewModel.getVideoSourceState()

						if (uri != null) {

							val isMuted = viewModel.getVideMutedState()

							UploadVideoPlayer(
								uri!!,
								isMuted.value
							) {
								viewModel.onFrameBitmapChanged(it)
							}

							IconButton(
								onClick = { viewModel.onChangeMuteState() },
								modifier = Modifier.fillMaxSize()
							) {

								if (isMuted.value) {
									Icon(
										Icons.Filled.VolumeOff,
										contentDescription = "",
										modifier = Modifier.size(32.dp)
									)
								}
							}

						} else {

							Text(
								text = "SELECT\nSOURCE",
								textAlign = TextAlign.Center,
								modifier = Modifier.fillMaxHeight().align(Alignment.Center)
							)
						}
					}
				}

				Spacer(modifier = Modifier.width(16.dp))

				Column(
					modifier = Modifier
						.weight(1f)
						.fillMaxWidth(),
				) {

					Text(
						text = "Cat checker:",
						color = Color.Black
					)

					val checkerItems = viewModel.getCheckerItemsState()

					LazyColumn(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 16.dp)
					) {

						items(checkerItems.value) {
							CatCheckerItem(it)
						}
					}
				}
			}

			Spacer(modifier = Modifier.height(64.dp))

			Button(
				shape = RoundedCornerShape(12.dp),
				enabled = viewModel.getUploadButtonState().value,
				modifier = Modifier
					.fillMaxWidth()
					.height(56.dp),
				onClick = { viewModel.onUploadVideoClick() }
			) {
				Text(
					text = "UPLOAD VIDEO"
				)
			}
		}

	}
}

@Composable
private fun Toolbar(
	onCloseClick: () -> Unit
) {
	TopAppBar(
		modifier = Modifier.fillMaxWidth(),
		backgroundColor = MaterialTheme.colors.background
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 12.dp),
		) {
			Icon(
				Icons.Default.Close,
				modifier = Modifier.size(24.dp).clickable(onClick = onCloseClick).align(alignment = Alignment.CenterEnd),
				contentDescription = ""
			)

			Text(
				text = "UPLOAD NEW VIDEO",
				modifier = Modifier.fillMaxWidth(),
				textAlign = TextAlign.Center
			)
		}
	}
}

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
					val bitmap = retriever.getFrameAtTime(presentationTimeUs, MediaMetadataRetriever.OPTION_CLOSEST)
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
					layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
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

@Composable
fun CatCheckerItem(
	state: CheckerItemState
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
//			.padding(12.dp)
	) {
		Text(
			text = state.labelName,
			modifier = Modifier.align(Alignment.CenterStart),
			color = Color.Black
		)
		Text(
			text = state.labelValue,
			modifier = Modifier.align(Alignment.CenterEnd),
			color = Color.Black
		)
	}
}

