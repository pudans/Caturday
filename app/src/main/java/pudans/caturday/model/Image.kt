package pudans.caturday.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
	@field:Json(name = "id") val name: String,
	@field:Json(name = "url") val url: String
)
