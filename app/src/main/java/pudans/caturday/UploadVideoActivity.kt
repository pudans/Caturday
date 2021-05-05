package pudans.caturday

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory

import com.google.android.exoplayer2.source.ExtractorMediaSource

import com.google.android.exoplayer2.source.MediaSource

import com.google.android.exoplayer2.upstream.DataSpec

import com.google.android.exoplayer2.DefaultLoadControl

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.video.VideoFrameMetadataListener
import com.google.android.exoplayer2.video.VideoListener
import android.media.MediaMetadataRetriever
import java.lang.RuntimeException


@AndroidEntryPoint
class UploadVideoActivity : AppCompatActivity() {

	private val model: UploadVideoViewModel by viewModels()

	private lateinit var mVideView: PlayerView

	private var mVideoUri: Uri? = null

	private val mExoPlayer: SimpleExoPlayer by lazy {
		SimpleExoPlayer.Builder(baseContext).build()
	}

	private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
		Log.d("qwerty", "$uri")

		mVideoUri = uri

		playVideo(uri!!)

		val bitmap = mVideView.videoSurfaceView?.drawToBitmap()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.upload_video_activity_layout)

		mVideView = findViewById(R.id.player)

		mVideView.player = mExoPlayer

		findViewById<View>(R.id.button_choose).setOnClickListener {
			getContent.launch("video/*")
		}

		findViewById<View>(R.id.button_upload).setOnClickListener {
			model.onRefresh(mVideoUri!!)
		}

		mExoPlayer.setVideoFrameMetadataListener { presentationTimeUs, releaseTimeNs, format, mediaFormat ->
			val currentFrame = getVideoFrame(baseContext, mVideoUri, presentationTimeUs)
			Log.d("asdfg", "$currentFrame")

			mVideView.post {
				model.onFrame(currentFrame!!)
			}

		}

	}

	private fun playVideo(uri: Uri) {
		val mediaItem: MediaItem = MediaItem.fromUri(uri)
		mExoPlayer.setMediaItem(mediaItem)
		mExoPlayer.prepare()
		mExoPlayer.play()
	}

	fun getVideoFrame(context: Context?, uri: Uri?, time: Long): Bitmap? {
		var bitmap: Bitmap? = null
		val retriever = MediaMetadataRetriever()
		try {
			retriever.setDataSource(context, uri)
			bitmap = retriever.getFrameAtTime(time)
		} catch (ex: RuntimeException) {
			ex.printStackTrace()
		} finally {
			try {
				retriever.release()
			} catch (ex: RuntimeException) {
				ex.printStackTrace()
			}
		}
		return bitmap
	}
}


