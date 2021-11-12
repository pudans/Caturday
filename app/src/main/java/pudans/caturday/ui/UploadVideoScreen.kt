package pudans.caturday.ui

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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import pudans.caturday.UploadVideoViewModel
import pudans.caturday.state.CheckerItemState
import pudans.caturday.state.UploadVideoState

@Composable
fun UploadVideoScreen(
	onCloseClick: () -> Unit
) {

	val viewModel = hiltViewModel<UploadVideoViewModel>()

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

				var muteState by remember { mutableStateOf(true) }

				Box(
					modifier = Modifier
						.fillMaxWidth()
						.aspectRatio(0.56f)
						.clip(RoundedCornerShape(24.dp))
						.clickable { muteState = !muteState }
						.background(color = Color.LightGray)
						.weight(1f)
				) {

					val uri by viewModel.getVideoSourceState()

					if (uri != null) {

						UploadVideoPlayer(
							uri!!,
							muteState,
						) {
							viewModel.onFrameBitmapChanged(it)
						}
					}

					if (muteState) {
						Icon(
							Icons.Filled.VolumeOff,
							contentDescription = "",
							modifier = Modifier
								.size(32.dp)
								.align(Alignment.Center)
						)
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
						color = Color.Black,
						style = TextStyle(
							fontFamily = FontFamily.Default,
							fontWeight = FontWeight.Bold,
							fontSize = 20.sp
						)
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


			val uploadState by viewModel.getUploadState()

			when (uploadState) {

				is UploadVideoState.Default -> {
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
				is UploadVideoState.Finished -> {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.align(Alignment.CenterHorizontally)
							.height(56.dp),
					) {
						Text(
							text = "SUCCESS",
							color = Color.Black,
							textAlign = TextAlign.Center
						)
					}
				}
				is UploadVideoState.Loading -> {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.height(56.dp),
					) {

						CircularProgressIndicator(
							modifier = Modifier
								.size(56.dp)
								.align(Alignment.Center),
							color = Color.Black
						)
					}
				}
				is UploadVideoState.Started -> {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.height(56.dp),
					) {
						CircularProgressIndicator(
							modifier = Modifier
								.size(56.dp)
								.align(Alignment.Center),
							color = Color.Black
						)
					}
				}
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
				modifier = Modifier
					.size(24.dp)
					.clickable(onClick = onCloseClick)
					.align(alignment = Alignment.CenterEnd),
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
	onFrameBitmap: (Long?) -> (Unit)
) {
	val context = LocalContext.current

	val exoPlayer = remember {
		SimpleExoPlayer.Builder(context).build().apply {
			playWhenReady = false
			videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT
			repeatMode = Player.REPEAT_MODE_ALL

			var lastFrame = 0L
			setVideoFrameMetadataListener { presentationTimeUs, _, _, _ ->
				if (lastFrame == 0L || (System.currentTimeMillis() - lastFrame > 1000L)) {
					lastFrame = System.currentTimeMillis()
					onFrameBitmap.invoke(presentationTimeUs)
				}
			}
		}
	}

	LaunchedEffect(uri) {
		exoPlayer.playWhenReady = true
		exoPlayer.setMediaItem(MediaItem.fromUri(uri))
		exoPlayer.prepare()
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
	) {
		Text(
			text = state.name,
			modifier = Modifier.align(Alignment.CenterStart),
			color = Color.Black,
			style = if (state.isAccent) {
				TextStyle(
					fontFamily = FontFamily.Default,
					fontWeight = FontWeight.Bold,
					fontSize = 20.sp
				)
			} else {
				TextStyle(
					fontFamily = FontFamily.Default,
					fontWeight = FontWeight.Normal,
					fontSize = 16.sp
				)
			}
		)
		Text(
			text = state.value,
			modifier = Modifier.align(Alignment.CenterEnd),
			color = Color.Black,
			style = if (state.isAccent) {
				TextStyle(
					fontFamily = FontFamily.Default,
					fontWeight = FontWeight.Bold,
					fontSize = 20.sp
				)
			} else {
				TextStyle(
					fontFamily = FontFamily.Default,
					fontWeight = FontWeight.Normal,
					fontSize = 16.sp
				)
			}
		)
	}
}

