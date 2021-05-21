package pudans.caturday.state

sealed class ProfileVideoListState {
	object Loading : ProfileVideoListState()
	object Empty : ProfileVideoListState()
	data class Data(val items: List<ProfileVideoItemState>) : ProfileVideoListState()
}
