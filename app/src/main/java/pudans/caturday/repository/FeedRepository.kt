package pudans.caturday.repository

import com.google.firebase.database.FirebaseDatabase
import pudans.caturday.model.Video
import javax.inject.Inject
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@ExperimentalCoroutinesApi
class FeedRepository
@Inject constructor(
	firebaseDatabase: FirebaseDatabase
) {

	private val mChannelFlow = ConflatedBroadcastChannel<List<Video>>()

	init {
		firebaseDatabase.reference.get().addOnSuccessListener { dataSnapshot ->
			val result = dataSnapshot.children.mapNotNull { it.getValue<Video>() }
			mChannelFlow.sendBlocking(result)
		}
	}

	fun getFeed(): Flow<List<Video>> = mChannelFlow.asFlow()
}