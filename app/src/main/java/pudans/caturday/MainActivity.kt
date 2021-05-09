package pudans.caturday

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.exoplayer2.ui.PlayerView
import dagger.hilt.android.AndroidEntryPoint
import pudans.caturday.ui.Feed

@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private val mViewModel: FeedViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

//		model.imageUrl.observeForever { url ->

//		}

		setContent {
			MaterialTheme {
				Surface(color = MaterialTheme.colors.background) {

					val items by mViewModel.observeFeedItems()

					Feed(items = items)
				}
			}
		}

		mViewModel.onRefresh()
	}
}


