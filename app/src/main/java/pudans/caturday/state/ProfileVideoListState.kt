package pudans.caturday.state

sealed interface ProfileVideoListState {
	object Loading : ProfileVideoListState
	object Empty : ProfileVideoListState
	data class Data(val items: List<ProfileVideoItemState>) : ProfileVideoListState
}
