package pudans.caturday.state

data class FeedItemState(
	val videoUrl: String,
	val uploaderEmail: String,
	val uploaderAvatarUrl: String = "",
	val uploadTimestamp: Long = 0L,
	val likedEmails: Array<String> = emptyArray()
)
