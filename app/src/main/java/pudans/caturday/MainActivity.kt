package pudans.caturday

import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import pudans.caturday.ui.theme.CaturdayTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private val model: LiveDataViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)


		model.imageUrl.observeForever { url ->
			setContent {
				CaturdayTheme {
					// A surface container using the 'background' color from the theme
					Surface(color = MaterialTheme.colors.background) {
						Greeting(url)
					}
				}
			}
		}

		model.onRefresh()
	}
}

@Composable
fun Greeting(url: String) {

	Image(
		painter = rememberCoilPainter(url),
		contentDescription = "",
	)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	CaturdayTheme {
		Greeting("Android")
	}
}


