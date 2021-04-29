package pudans.caturday.network

import kotlinx.coroutines.Deferred
import okhttp3.Call
import pudans.caturday.model.Image
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApiService {

	@GET("v1/images/search")
	fun getImages(
		@Query("limit") limit: Int,
	): Deferred<Response<List<Image>>>
//
//	@GET("pokemon/{name}")
//	suspend fun fetchPokemonInfo(@Path("name") name: String): ApiResponse<PokemonInfo>
}
