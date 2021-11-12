package pudans.caturday.repository

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pudans.caturday.model.PreviewImage
import pudans.caturday.model.User
import pudans.caturday.model.Video
import pudans.caturday.state.UploadVideoState
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class UploadFileRepository
@Inject constructor(
	private val mFirebaseStorage: FirebaseStorage,
	private val mFirebaseDatabase: FirebaseDatabase,
	private val mFirebaseAuth: FirebaseAuth
) {

	private val retriever = MediaMetadataRetriever()
	private val mChannelFlow = MutableStateFlow<UploadVideoState>(UploadVideoState.Default)

	fun doWork(uri: Uri): Flow<UploadVideoState> {

		mChannelFlow.value = UploadVideoState.Started

		retriever.setDataSource(mFirebaseDatabase.app.applicationContext, uri)
		val firstFrame = retriever.getFrameAtIndex(0)

		uploadFile(uri, firstFrame)

		return mChannelFlow
	}

	private fun uploadFile(file: Uri, firstFrame: Bitmap?) {

		val videoId = UUID.randomUUID().toString()

		val listRef = mFirebaseStorage.reference.child("videos").child(videoId)

		val metadata = StorageMetadata.Builder()
			.setContentType("videos/mp4")
			.build()

		val uploadTask = listRef.putFile(file, metadata)

		uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
			val progress = bytesTransferred.toFloat() / totalByteCount
			mChannelFlow.value = UploadVideoState.Loading(progress)
		}.addOnFailureListener {
			Log.d("asdfggg", "Upload is failed")
		}.addOnSuccessListener {
			listRef.downloadUrl.addOnSuccessListener {
				uploadPreview(videoId, it, firstFrame)
			}
		}
	}


	private fun uploadPreview(videoId: String, videoUri: Uri, firstFrame: Bitmap?) {
		if (firstFrame != null) {
			val baos = ByteArrayOutputStream()
			firstFrame.compress(Bitmap.CompressFormat.JPEG, 100, baos)
			val data: ByteArray = baos.toByteArray()

			val previewId = UUID.randomUUID().toString()
			val listRef = mFirebaseStorage.reference.child("previews").child(previewId)
			val uploadTask = listRef.putBytes(data)
			uploadTask.addOnSuccessListener { taskSnapshot ->
				listRef.downloadUrl.addOnSuccessListener { previewUri ->
					insertData(videoId, videoUri, previewId, previewUri)
				}
			}
		} else {
			insertData(videoId, videoUri, null, null)
		}

	}

	private fun insertData(
		videoId: String, videoUri: Uri,
		previewId: String?, previewUri: Uri?
	) {
		val reference = mFirebaseDatabase.reference

		val newRecord = Video(
			id = videoId,
			url = videoUri.toString(),
			preview = PreviewImage(
				id = previewId,
				url = previewUri.toString()
			),
			uploader = User(
				uid = mFirebaseAuth.currentUser?.uid ?: "",
				name = mFirebaseAuth.currentUser?.displayName ?: "",
				email = mFirebaseAuth.currentUser?.email ?: "",
				photoUrl = mFirebaseAuth.currentUser?.photoUrl?.toString() ?: "",
			),
			uploadTimestamp = System.currentTimeMillis(),
			likedEmails = emptyList()
		)
		reference.child(videoId).setValue(newRecord)

		mChannelFlow.value = UploadVideoState.Finished
	}
}