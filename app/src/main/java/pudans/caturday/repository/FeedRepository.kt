package pudans.caturday.repository

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import pudans.caturday.model.FeedItem
import javax.inject.Inject
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class FeedRepository
@Inject constructor(
	private val mFirebaseStorage: FirebaseStorage
) {


	fun getFeed(): List<FeedItem> {

		val listRef = mFirebaseStorage.reference.child("videos")

// You'll need to import com.google.firebase.storage.ktx.component1 and
// com.google.firebase.storage.ktx.component2

		listRef.listAll()
			.addOnSuccessListener { (items, prefixes) ->
				prefixes.forEach { prefix ->

					Log.d("qwerty1", "$prefix")
				}

				items.forEach { item ->
					Log.d("qwerty2", "$item")

					Log.d("qwerty2", "${item.downloadUrl}")

					Log.d("qwerty2", "${item.downloadUrl}")

					Log.d("qwerty2", "${item.metadata}")

					Log.d("qwerty2", "${item.name}")
				}
			}
			.addOnFailureListener {
				// Uh-oh, an error occurred!
			}

		return emptyList()
	}
}