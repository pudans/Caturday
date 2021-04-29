package pudans.caturday.network

import kotlinx.coroutines.Deferred
import okhttp3.Call
import pudans.caturday.model.Image
import retrofit2.Response
import javax.inject.Inject

class CatApiClient
@Inject constructor(
	private val mCatApiService: CatApiService
) {

	suspend fun getImages(): Deferred<Response<List<Image>>> =
		mCatApiService.getImages(
			limit = PAGING_SIZE
		)

	companion object {
		private const val PAGING_SIZE = 2
	}
}
