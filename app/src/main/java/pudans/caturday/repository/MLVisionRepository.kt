package pudans.caturday.repository

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pudans.caturday.model.Video
import pudans.caturday.state.CheckerItemState
import kotlin.math.min

@FlowPreview
@ExperimentalCoroutinesApi
class MLVisionRepository
@Inject constructor(
	@ApplicationContext private val context: Context,
	private val mFirebaseStorage: FirebaseStorage,
	private val mFirebaseDatabase: FirebaseDatabase
) {

	private val mImageLabeling = ImageLabeling.getClient(
		ImageLabelerOptions.Builder()
			.setConfidenceThreshold(0.1f)
			.build()
	)

	val retriever = MediaMetadataRetriever()

	private val mChannelFlow = ConflatedBroadcastChannel<List<ImageLabel>>()

//	suspend fun work(presentationTimeUs: Long, uri: Uri): Flow<List<ImageLabel>> = flow {
//		val retriever = MediaMetadataRetriever()
//		val bitmap = retriever.getFrameAtTime(presentationTimeUs, MediaMetadataRetriever.OPTION_CLOSEST)
//
//		val image = InputImage.fromBitmap(bitmap!!, 0)
//
//		val task = mImageLabeling.process(image)
//
//		delay(1000L)
//
//		if (task.isSuccessful) {
//			emit(task.result)
//		} else {
//			emit(emptyList<ImageLabel>())
//		}
//
//	}.flowOn(context = Dispatchers.IO)

	fun setUri(uri: Uri) {
		retriever.setDataSource(context, uri)
	}

	fun work(presentationTimeUs: Long): Flow<List<ImageLabel>> {
//		Log.d("alskdnasljdbljb1", "$presentationTimeUs $uri")



//		Log.d("alskdnasljdbljb2", "$retriever")

		val bitmap = retriever.getFrameAtTime(presentationTimeUs, MediaMetadataRetriever.OPTION_CLOSEST)

//		Log.d("alskdnasljdbljb3", "$retriever")

		val image = InputImage.fromBitmap(bitmap!!, 0)

		mImageLabeling.process(image)
			.addOnCompleteListener { task ->
//				Log.d("alskdnasljdbljb4", "addOnCompleteListener")
				mChannelFlow.sendBlocking(task.result)
			}
			.addOnCanceledListener {
//				Log.d("alskdnasljdbljb4", "addOnCanceledListener")

			}
			.addOnFailureListener {
//				Log.d("alskdnasljdbljb4", "addOnFailureListener $it")
			}

//		Log.d("alskdnasljdbljb4", "${task.isSuccessful}")
//		Log.d("alskdnasljdbljb4", "${task.exception}")
//
//
//		if (task.isSuccessful) {
//			mChannelFlow.sendBlocking(task.result)
//		} else {
//			mChannelFlow.sendBlocking(emptyList<ImageLabel>())
//		}

		Log.d("alskdnasljdbljb4", "-------------------")

		return mChannelFlow.asFlow().flowOn(Dispatchers.IO)
	}
}