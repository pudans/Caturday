package pudans.caturday.repository

import android.graphics.Bitmap
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pudans.caturday.state.CheckerItemState
import kotlin.math.min

class MLVisionRepository
@Inject constructor(
	private val mFirebaseStorage: FirebaseStorage,
	private val mFirebaseDatabase: FirebaseDatabase
) {

	private val mImageLabeling = ImageLabeling.getClient(
		ImageLabelerOptions.Builder()
			.setConfidenceThreshold(0.1f)
			.build()
	)

	suspend fun work(bitmap: Bitmap): Flow<List<CheckerItemState>> = flow {

		val image = InputImage.fromBitmap(bitmap, 0)

		val task = mImageLabeling.process(image)

		delay(1000L)

		if (task.isSuccessful) {

			val states = task.result
				.subList(0, min(16, task.result.size))
				.map { CheckerItemState(it.text, String.format("%.1f%%", it.confidence * 100), 0) }

			emit(states)

		} else {
			val result = task.exception
			emit(emptyList<CheckerItemState>())
		}

	}.flowOn(context = Dispatchers.IO)
}