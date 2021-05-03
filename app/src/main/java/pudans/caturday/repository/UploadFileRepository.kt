package pudans.caturday.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import pudans.caturday.model.FeedItem
import javax.inject.Inject
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import java.util.*

class UploadFileRepository
@Inject constructor(
	private val mFirebaseStorage: FirebaseStorage,
	private val mFirebaseDatabase: FirebaseDatabase
) {


	fun upload(file: Uri): List<FeedItem> {

		val name = UUID.randomUUID().toString() + ".mp4"

		val listRef = mFirebaseStorage.reference.child("videos").child(name)

		// Create the file metadata
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

			insertData(it)
		}

		return emptyList()
	}

	private fun insertData(task: UploadTask.TaskSnapshot) {
		val reference = mFirebaseDatabase.reference

		val database = reference.database

		val key = reference.child("videos").push()

		Log.d("qwerty", "${task.uploadSessionUri}")
		Log.d("qwerty", "${task.uploadSessionUri?.encodedPath}")
		Log.d("qwerty", "${task.metadata}")
		Log.d("qwerty", "${task.metadata?.path}")


		reference.child(key.key!!).setValue(Video(task.metadata!!.name!!, task.task.result.uploadSessionUri.toString()))
	}

	@IgnoreExtraProperties
	data class Video(val name: String, val url: String)
}