package pudans.caturday.model

data class Video(
	val videoName: String = "",
	val videoUrl: String = "",
	val previewName: String = "",
	val previewUrl: String = "",
	val uploaderUid: String = "",
	val uploaderEmail: String = "",
	val uploaderAvatarUrl: String = "",
	val uploadTimestamp: Long = 0L,
	val likedEmails: List<String> = emptyList()
)
