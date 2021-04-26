package pudans.caturday.network

import com.skydoves.sandwich.ApiResponse
import pudans.caturday.model.Image
import javax.inject.Inject

class CatApiClient
@Inject constructor(
	private val mCatApiService: CatApiService
) {

	suspend fun getImages(): ApiResponse<Image> =
		mCatApiService.getImages(
			limit = PAGING_SIZE
		)

	companion object {
		private const val PAGING_SIZE = 2
	}
}
