package pudans.caturday.state

sealed class UploadVideoState {
	object Default : UploadVideoState()
	object Started : UploadVideoState()
	object Finished : UploadVideoState()
	data class Loading(val progress: Float) : UploadVideoState()
}
