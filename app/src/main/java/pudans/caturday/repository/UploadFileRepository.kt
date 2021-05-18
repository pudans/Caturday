package pudans.caturday.repository

import android.R.attr
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import pudans.caturday.model.FeedItem
import javax.inject.Inject
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import java.util.*
import android.R.attr.bitmap
import java.io.ByteArrayOutputStream


class UploadFileRepository
@Inject constructor(
	private val mFirebaseStorage: FirebaseStorage,
	private val mFirebaseDatabase: FirebaseDatabase,
	private val mFirebaseAuth: FirebaseAuth
) {

	private val retriever = MediaMetadataRetriever()

	fun doWork(uri: Uri) {

		retriever.setDataSource(mFirebaseDatabase.app.applicationContext, uri)
		val firstFrame = retriever.getFrameAtIndex(0)

		upload(uri, firstFrame!!)
	}

	private fun upload(file: Uri, firstFrame: Bitmap) {

		val name = UUID.randomUUID().toString() + ".mp4"

		val listRef = mFirebaseStorage.reference.child("videos").child(name)

		val metadata = StorageMetadata.Builder()
			.setContentType("videos/mp4")
			.build()

		val uploadTask = listRef.putFile(file, metadata)

		uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
			val progress = (100.0 * bytesTransferred) / totalByteCount
			Log.d("asdfggg", "Upload is $progress% done")
		}.addOnPausedListener {
			Log.d("asdfggg", "Upload is paused")
		}.addOnFailureListener {
			Log.d("asdfggg", "Upload is failed")
		}.addOnSuccessListener {
			Log.d("asdfggg", "Upload is success $it")

			listRef.downloadUrl.addOnSuccessListener {
				uploadPreview(name, it, firstFrame)
			}
		}
	}


	private fun uploadPreview(videoName: String, videoUri: Uri, firstFrame: Bitmap) {
		val baos = ByteArrayOutputStream()
		firstFrame.compress(Bitmap.CompressFormat.JPEG, 100, baos)
		val data: ByteArray = baos.toByteArray()

		val name = UUID.randomUUID().toString() + ".jpg"
		val listRef = mFirebaseStorage.reference.child("previews").child(name)
		val uploadTask = listRef.putBytes(data)
		uploadTask.addOnSuccessListener { taskSnapshot ->
			listRef.downloadUrl.addOnSuccessListener { previewUri ->

				insertData(videoName, videoUri, name, previewUri)
			}
		}
	}

	private fun insertData(
		videoName: String, videoUri: Uri,
		previewName: String, previewUri: Uri
	) {
		val reference = mFirebaseDatabase.reference

		val key = reference.child("videos").push()
		val newRecord = Video(
			videoName = videoName,
			videoUrl = videoUri.toString(),
			previewName = previewName,
			previewUrl = previewUri.toString(),
			uploaderUid = mFirebaseAuth.currentUser!!.uid,
			uploaderEmail = mFirebaseAuth.currentUser!!.email!!,
			uploadTimestamp = System.currentTimeMillis()
		)
		reference.child(key.key!!).setValue(newRecord)
	}

	data class Video(
		val videoName: String,
		val videoUrl: String,
		val previewName: String,
		val previewUrl: String,
		val uploaderUid: String,
		val uploaderEmail: String,
		val uploadTimestamp: Long
	)
}