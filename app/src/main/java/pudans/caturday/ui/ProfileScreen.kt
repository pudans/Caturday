package pudans.caturday.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import pudans.caturday.ProfileViewModel
import pudans.caturday.state.ProfileState
import pudans.caturday.state.ProfileVideoItemState
import pudans.caturday.state.ProfileVideoListState

@FlowPreview
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@Composable
fun ProfileScreen() {

	val viewModel = hiltViewModel<ProfileViewModel>()

	Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {

		val profileState by viewModel.observeProfileState()

		Toolbar(profileState)

		Spacer(modifier = Modifier.size(24.dp))

		AvatarImage(profileState.avatarUrl)

		Spacer(modifier = Modifier.size(16.dp))

		ProfileNick(profileState.nick)

		Spacer(modifier = Modifier.size(24.dp))

		val pages = arrayOf("UPLOADED", "LIKED")
		val pagerState = rememberPagerState(pageCount = pages.size)

		Tabs(pages, pagerState)

		val uploadedVideos by viewModel.observeUploadedVideosState()
		val likedVideos by viewModel.observeLikedVideosState()

		val data = arrayOf(uploadedVideos, likedVideos)

		Pager(data, pagerState) {
			viewModel.onVideoClick(it)
		}
	}
}

@Composable
private fun Toolbar(
	state: ProfileState
) {
	TopAppBar(
		modifier = Modifier.fillMaxWidth(),
		backgroundColor = Color.Black
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 12.dp),
		) {
			Text(
				text = state.title,
				modifier = Modifier.fillMaxWidth(),
				textAlign = TextAlign.Center,
				color = Color.White
			)
		}
	}
}

@Composable
private fun AvatarImage(avatarUrl: String) {
	Image(
		painter = rememberCoilPainter(
			request = avatarUrl,
			fadeIn = true
		),
		contentDescription = "",
		contentScale = ContentScale.Crop,
		modifier = Modifier
			.size(80.dp)
			.clip(CircleShape)
			.border(2.dp, Color.White, CircleShape)
	)
}

@Composable
private fun ProfileNick(nick: String) {
	Text(
		text = nick,
		color = Color.White,
		textAlign = TextAlign.Center,
		modifier = Modifier.fillMaxWidth()
	)
}

@ExperimentalPagerApi
@Composable
private fun Tabs(pages: Array<String>, pagerState: PagerState) {
	val coroutineScope = rememberCoroutineScope()

	TabRow(
		backgroundColor = Color.Black,
		contentColor = Color.White,
		selectedTabIndex = pagerState.currentPage,
		indicator = { tabPositions ->
			TabRowDefaults.Indicator(
				Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
			)
		}
	) {
		pages.forEachIndexed { index, title ->
			Tab(
				text = {
					Text(
						text = title,
						color = Color.White
					)
				},
				selected = pagerState.currentPage == index,
				onClick = {
					coroutineScope.launch {
						pagerState.animateScrollToPage(index)
					}
				}
			)
		}
	}
}

@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
private fun Pager(
	data: Array<ProfileVideoListState>,
	pagerState: PagerState,
	onItemClick: (String) -> Unit
) {
	HorizontalPager(state = pagerState) { page ->
		when (val listState = data[page]) {
			is ProfileVideoListState.Data -> List(listState.items, onItemClick)
			ProfileVideoListState.Empty -> EmptyList()
			ProfileVideoListState.Loading -> Loading()
		}
	}
}

@ExperimentalFoundationApi
@Composable
private fun List(
	items: List<ProfileVideoItemState>,
	onItemClick: (String) -> Unit
) {
	LazyVerticalGrid(
		cells = GridCells.Fixed(3),
		modifier = Modifier.fillMaxSize()
	) {
		items(items.size) { ListItem(items[it], onItemClick) }
	}
}

@Composable
private fun ListItem(
	item: ProfileVideoItemState,
	onClick: (String) -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.aspectRatio(0.56f)
			.clickable { onClick.invoke(item.id) }
	) {
		Image(
			painter = rememberCoilPainter(
				request = item.url,
				fadeIn = true
			),
			contentDescription = ""
		)
	}
}

@Composable
private fun EmptyList() {
	Box(modifier = Modifier.fillMaxSize()) {
		Text(
			text = "EMPTY",
			color = Color.White,
			textAlign = TextAlign.Center,
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