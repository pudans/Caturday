package pudans.caturday.ui

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.pageChanges
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import pudans.caturday.model.FeedItem

@ExperimentalPagerApi
@Composable
fun Feed(items: Array<FeedItem>) {
	Box(modifier = Modifier.background(color = Color.Black)) {
		FeedList(items)
	}
}

@ExperimentalPagerApi
@Composable
private fun FeedList(items: Array<FeedItem>) {

	val pagerState = rememberPagerState(pageCount = items.size)

	snapshotFlow { pagerState.currentPage }.collectAsState(initial = 0)

	VerticalPager(
		state = pagerState
	) { page ->


		Box(
//			modifier = Modifier.fillParentMaxSize(),
		) {


//			Log.d("qwerty", "VerticalPager $page")

			val mediaPlayback = VideoPlayer(
				page,
				pagerState.currentPage,
				uri = items[page].url
			)

//			IconButton(onClick = {
//				mediaPlayback.rewind(10_000)
//			}) {
//				Icon(Icons.Filled)
//			}

//				Column(
//					modifier = Modifier.align(Alignment.BottomStart),
//				) {
//					ReelFooter(reel = reel)
//					Divider()
//				}
		}
	}
}