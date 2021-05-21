package pudans.caturday.state

sealed class FeedScreenState {
	object Loading : FeedScreenState()
	object Empty : FeedScreenState()
	data class Data(val items: List<FeedItemState>) : FeedScreenState()
}
