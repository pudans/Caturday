package pudans.caturday

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory

import com.google.android.exoplayer2.source.ExtractorMediaSource

import com.google.android.exoplayer2.source.MediaSource

import com.google.android.exoplayer2.upstream.DataSpec

import com.google.android.exoplayer2.DefaultLoadControl

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer


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
	}

	private fun playVideo(uri: Uri) {
		val mediaItem: MediaItem = MediaItem.fromUri(uri)
		mExoPlayer.setMediaItem(mediaItem)
		mExoPlayer.prepare()
		mExoPlayer.play()
	}
}


