package pudans.caturday.model

sealed interface VideosResult {

	object Loading : VideosResult

	object Error : VideosResult

	data class Data(val data: List<Video>) : VideosResult
}