package com.example.exoplayersoundonly

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var audioPlayer: SimpleExoPlayer? = null

    private val playerEventListener = object : Player.EventListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
        }

        override fun onPlayerError(error: ExoPlaybackException) {
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPlayer(buildMediaSource())

        audio.setOnClickListener {
            audioPlayer?.playWhenReady = true
        }
    }

    private fun initPlayer(mediaSource: MediaSource?) {
        mediaSource ?: return
        val trackSelector = DefaultTrackSelector(this)
        val loadControl = DefaultLoadControl()
        audioPlayer = SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).setLoadControl(loadControl).build()
        audioPlayer?.prepare(mediaSource)
        audioPlayer?.addListener(playerEventListener)
        audioPlayer?.playWhenReady = false
    }

    private fun buildMediaSource(): MediaSource? {
        return try {
            val uri = Uri.parse(SAMPLE_MP3)
            val factory = DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)))
            ProgressiveMediaSource.Factory(factory).createMediaSource(uri)
        } catch (e: Exception) {
            null
        }
    }

    private fun releasePlayer() {
        audioPlayer?.let {
            it.stop()
            it.release()
            audioPlayer = null
        }
    }

    override fun onPause() {
        releasePlayer()
        super.onPause()
    }

    companion object {
        private const val SAMPLE_MP3 = "http://www.ne.jp/asahi/music/myuu/wave/menuettm.mp3"
    }
}