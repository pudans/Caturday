package pudans.caturday.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import pudans.caturday.network.CatApiClient
import javax.inject.Inject

class MainRepository
@Inject constructor(
	private val mClient: CatApiClient
) {

	@WorkerThread
	fun getImages(

	) = flow {

		val response = mClient.getImages().await()

		if (response.isSuccessful) {
			emit(response.body())
		} else {
			error("${response.errorBody()}")
		}

	}.onStart { }.onCompletion { }.flowOn(Dispatchers.IO)
}
