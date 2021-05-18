package pudans.caturday.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import pudans.caturday.FeedViewModel
import pudans.caturday.model.Video

@ExperimentalPagerApi
@Composable
fun FeedScreen(viewModel: FeedViewModel) {
	Box(modifier = Modifier.background(color = Color.Black)) {

		val items by viewModel.observeFeedItems()

		FeedList(
			items
		)
	}
}

@ExperimentalPagerApi
@Composable
private fun FeedList(
	items: Array<Video>
) {

	if (items.isEmpty()) return

	val pagerState = rememberPagerState(pageCount = items.size)

	snapshotFlow { pagerState.currentPage }.collectAsState(initial = 0)

	VerticalPager(
		state = pagerState
	) { page ->

		val state = items[page]

		Box(
//			modifier = Modifier.fillParentMaxSize(),
		) {


			FeedVideoPlayer(
				uri = state.videoUrl,
				isPlayWhenReady = page == pagerState.currentPage
			)

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.align(Alignment.BottomCenter)
					.padding(16.dp)
			) {

				Text(
					text = "@${state.uploaderEmail.split('@').first()}",
					color = Color.White
				)

				Text(
					text = "Likes: ${state.likedEmails.map { "@${state.uploaderEmail.split('@').first()}" }}",
					color = Color.White
				)
			}

			Column(
				modifier = Modifier
					.align(Alignment.CenterEnd)
					.padding(16.dp)
			) {

				Icon(
					painter = rememberCoilPainter(state.uploaderAvatarUrl),
					contentDescription = "",
					modifier = Modifier.size(48.dp)
				)

				Spacer(modifier = Modifier.height(16.dp))

				Icon(
					imageVector = Icons.Filled.Favorite,
					tint = Color.White,
					contentDescription = "",
					modifier = Modifier.size(48.dp)
				)
			}
		}
	}
}