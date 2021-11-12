package pudans.caturday.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import pudans.caturday.model.Video
import javax.inject.Inject

class LikeRepository
@Inject constructor(
	private val mFirebaseDatabase: FirebaseDatabase,
	private val mFirebaseAuth: FirebaseAuth
) {

	private val mChannelFlow = MutableSharedFlow<Boolean>()

	fun likeOrDislike(videoId: String): Flow<Boolean> {

		mFirebaseDatabase.reference.get().addOnSuccessListener { dataSnapshot ->
			val result = dataSnapshot.children.mapNotNull { it.getValue<Video>() }

			val targetVideo = result.find { it.id == videoId }?.let {
				val dd: MutableList<String>
				if (it.likedEmails == null) {
					dd = ArrayList<String>()
					dd.add(mFirebaseAuth.currentUser?.email ?: "")
				} else {
					dd = it.likedEmails.toMutableList()
					if (dd.contains(mFirebaseAuth.currentUser?.email)) {
						dd.remove(mFirebaseAuth.currentUser?.email)
					} else {
						dd.add(mFirebaseAuth.currentUser?.email ?: "")
					}
				}
				return@let it.copy(likedEmails = dd)
			}

			mFirebaseDatabase.reference.child(videoId).setValue(targetVideo)
		}

		return mChannelFlow
	}
}