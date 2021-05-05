package pudans.caturday.repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import pudans.caturday.model.FeedItem
import javax.inject.Inject
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.util.*

class MLCatFinderRepository
@Inject constructor(
	private val mFirebaseStorage: FirebaseStorage,
	private val mFirebaseDatabase: FirebaseDatabase
) {

	@WorkerThread
	fun onFrame(bitmap: Bitmap) {
		val image = InputImage.fromBitmap(bitmap, 0)
		val options = ImageLabelerOptions.Builder()
			.setConfidenceThreshold(0.1f)
			.build()

		val labeler = ImageLabeling.getClient(options)

		labeler.process(image)
			.addOnSuccessListener { labels ->
				Log.d("asdddddd success", labels.toString())
				labels.forEach {
					if (it.text == "Cat") {
						Log.d("dfffffdf success", "$it")
					}
				}
			}
			.addOnFailureListener { e ->
				Log.d("asdddddd error", e.toString())
			}

	}
}