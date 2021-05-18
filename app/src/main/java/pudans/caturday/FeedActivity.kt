package pudans.caturday

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import pudans.caturday.ui.FeedScreen

@ExperimentalPagerApi
@AndroidEntryPoint
class FeedActivity : AppCompatActivity() {

	private val mViewModel: FeedViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			MaterialTheme {
				Surface(color = MaterialTheme.colors.background) {

					FeedScreen(mViewModel)
				}
			}
		}

		mViewModel.onRefresh()
	}
}


