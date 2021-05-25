package pudans.caturday.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
class FeedRepository
@Inject constructor(
	firebaseDatabase: FirebaseDatabase
) {

	private val mChannelFlow = ConflatedBroadcastChannel<List<Video>>()

	init {

		firebaseDatabase.reference.addValueEventListener(object : ValueEventListener {

			override fun onDataChange(snapshot: DataSnapshot) {
				val result = snapshot.children.mapNotNull { it.getValue<Video>() }.sortedBy { -it.uploadTimestamp }
				mChannelFlow.sendBlocking(result)
			}

			override fun onCancelled(error: DatabaseError) {
//				TODO("Not yet implemented")
			}

		})
	}

	fun getFeed(): Flow<List<Video>> = mChannelFlow.asFlow()
}