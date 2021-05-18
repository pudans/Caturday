package pudans.caturday.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pudans.caturday.UploadVideoViewModel

@Composable
fun UploadVideoScreen(
	viewModel: UploadVideoViewModel
) {

	Scaffold(
		topBar = { Toolbar() },

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
							.background(color = Color.LightGray)
					) {

						val uri = viewModel.getVideoSourceState()

						if (uri.value != null) {

							val isMuted = viewModel.getVideMutedState()

							UploadVideoPlayer(
								uri.value!!,
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
							Icon(
								Icons.Filled.PlayCircleFilled,
								contentDescription = "",
								modifier = Modifier
									.size(32.dp)
									.align(alignment = Alignment.Center)
							)
						}
					}


					Spacer(modifier = Modifier.height(16.dp))

					Button(
						onClick = { viewModel.onSelectSourceClick() },
						modifier = Modifier.fillMaxWidth(),
						shape = RoundedCornerShape(12.dp),
					) {
						Text(
							text = "SELECT SOURCE"
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
						color = Color.Black
					)

					val checkerItems = viewModel.getCheckerItemsState()

					LazyColumn(
						modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
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
private fun Toolbar() {
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
					.align(alignment = Alignment.CenterEnd)
					.size(24.dp),
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

