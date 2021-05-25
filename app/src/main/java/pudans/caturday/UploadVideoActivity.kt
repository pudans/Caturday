package pudans.caturday

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Parcelable
import androidx.activity.compose.setContent
import pudans.caturday.ui.UploadVideoScreen

@AndroidEntryPoint
class UploadVideoActivity : AppCompatActivity() {

	private val model: UploadVideoViewModel by viewModels()

	private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
		model.setVideoSource(uri)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			UploadVideoScreen {
				finish()
			}
		}

		model.getFileSelectorState().observeForever {
			if (it) {
				getContent.launch("video/*")
			}
		}

		(intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { uri ->
			model.setVideoSource(uri)
		} ?: kotlin.run {
			getContent.launch("video/*")
		}
	}
}


