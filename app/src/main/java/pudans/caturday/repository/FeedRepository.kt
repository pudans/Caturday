package pudans.caturday.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pudans.caturday.model.Video
import pudans.caturday.model.VideosResult
import javax.inject.Inject

class FeedRepository
@Inject constructor(
	firebaseDatabase: FirebaseDatabase
) {

	private val mChannelFlow = MutableStateFlow<VideosResult>(VideosResult.Loading)

	init {
		firebaseDatabase.reference.addValueEventListener(object : ValueEventListener {

			override fun onDataChange(snapshot: DataSnapshot) {
				val result = snapshot.children.mapNotNull { it.getValue<Video>() }.sortedBy { -it.uploadTimestamp }
				mChannelFlow.value = VideosResult.Data(result)
			}

			override fun onCancelled(error: DatabaseError) {
				mChannelFlow.value = VideosResult.Error
			}
		})
	}

	fun getFeed(): Flow<VideosResult> = mChannelFlow
}