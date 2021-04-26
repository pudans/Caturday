package pudans.caturday

import androidx.annotation.WorkerThread
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
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

		val response = mClient.getImages()
		response.suspendOnSuccess {

			emit(data!!)
		}
			// handles the case when the API request gets an error response.
			// e.g., internal server error.
			.onError { }
			// handles the case when the API request gets an exception response.
			// e.g., network connection error.
			.onException { }

	}.onStart { }.onCompletion { }.flowOn(Dispatchers.IO)
}
