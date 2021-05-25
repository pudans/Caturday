package pudans.caturday.model

data class Video(
	val id: String? = null,
	val url: String? = null,
	val preview: PreviewImage? = null,
	val uploader: User? = null,
	val uploadTimestamp: Long = 0L,
	val likedEmails: List<String>? = emptyList()
)
