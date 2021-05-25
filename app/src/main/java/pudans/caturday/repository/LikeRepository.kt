package pudans.caturday.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import pudans.caturday.model.Video
import javax.inject.Inject
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@FlowPreview
@ExperimentalCoroutinesApi
class LikeRepository
@Inject constructor(
	private val mFirebaseDatabase: FirebaseDatabase,
	private val mFirebaseAuth: FirebaseAuth
) {

	private val mChannelFlow = ConflatedBroadcastChannel<Boolean>()

	init {
//		firebaseDatabase.reference.get().addOnSuccessListener { dataSnapshot ->
//			val result = dataSnapshot
//				.children
//				.mapNotNull { it.getValue<Video>() }
//				.filter { it.likedEmails?.contains(firebaseAuth.currentUser?.email) ?: false }
//			mChannelFlow.sendBlocking(result)
//		}
	}

	fun getLikedVideos(videoId: String): Flow<Boolean> {

		mFirebaseDatabase.reference.get().addOnSuccessListener { dataSnapshot ->
			val result = dataSnapshot.children.mapNotNull { it.getValue<Video>() }

			val targetVideo = result.find { it.id == videoId }?.let {
				val dd: ArrayList<String>
				if (it.likedEmails == null) {
					dd = ArrayList<String>()
					dd.add(mFirebaseAuth.currentUser?.email ?: "")
				} else {
					dd = ArrayList<String>(it.likedEmails)
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

		return mChannelFlow.asFlow()
	}
}