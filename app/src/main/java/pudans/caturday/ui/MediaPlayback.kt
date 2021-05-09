package pudans.caturday.ui

interface MediaPlayback {

    fun playPause()

    fun forward(durationInMillis: Long)

    fun rewind(durationInMillis: Long)
}