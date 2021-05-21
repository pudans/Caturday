package pudans.caturday.state

data class FeedItemState(
	val videoUrl: String,
	val videoPreviewUrl: String,
	val uploaderAvatarUrl: String,
	val uploaderNick: String,
	val likedNicks: String,
	val likesCount: Int,
	val isLikedByUser: Boolean
)
