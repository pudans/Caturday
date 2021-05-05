package pudans.caturday

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.coil.CoilImage
import com.google.accompanist.coil.rememberCoilPainter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import dagger.hilt.android.AndroidEntryPoint
import pudans.caturday.ui.theme.CaturdayTheme
import pudans.caturday.ui.theme.Shapes
import pudans.caturday.ui.theme.Typography

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private val model: LiveDataViewModel by viewModels()

	private val mExoPlayer: SimpleExoPlayer by lazy {
		SimpleExoPlayer.Builder(baseContext).build()
	}

	private lateinit var mVideView: PlayerView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity_layout)

		mVideView = findViewById(R.id.player)

		mVideView.player = mExoPlayer


//		model.imageUrl.observeForever { url ->
//			setContent {
//				MaterialTheme {
//					Surface(color = MaterialTheme.colors.background) {
//						Image(
//							painter = rememberCoilPainter(url),
//							contentDescription = "",
//						)
//					}
//				}
//			}
//		}

		model.onRefresh()
	}

	override fun onStart() {
		super.onStart()

		playVideo(Uri.parse("https://firebasestorage.googleapis.com/v0/b/caturday-a9a65.appspot.com/o/videos%2Fef9a80c0-76bb-4112-9d5d-acc9900de1f8.mp4?alt=media&token=224dad1f-40d4-4d55-a18a-18875f82d0c9"))

//		playVideo(Uri.parse("https://firebasestorage.googleapis.com/v0/b/caturday-a9a65.appspot.com/o?name=videos%2F68ba5959-f37d-4acf-b75f-af759d54ac22.mp4"))
	}

	private fun playVideo(uri: Uri) {
		val mediaItem: MediaItem = MediaItem.fromUri(uri)
		mExoPlayer.setMediaItem(mediaItem)
		mExoPlayer.prepare()
		mExoPlayer.play()
	}
}


