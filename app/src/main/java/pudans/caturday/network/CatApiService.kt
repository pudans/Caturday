package pudans.caturday.network

import com.skydoves.sandwich.ApiResponse
import pudans.caturday.model.Image
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApiService {

	@GET("v1/images/search")
	suspend fun getImages(
		@Query("limit") limit: Int,
	): ApiResponse<Image>
//
//	@GET("pokemon/{name}")
//	suspend fun fetchPokemonInfo(@Path("name") name: String): ApiResponse<PokemonInfo>
}
