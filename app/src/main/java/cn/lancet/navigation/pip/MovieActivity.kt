package cn.lancet.navigation.pip

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.util.Linkify
import android.util.Rational
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.doOnLayout
import cn.lancet.navigation.MainActivity
import cn.lancet.navigation.databinding.ActivityMoiveBinding
import cn.lancet.navigation.widget.MovieView

class MovieActivity: AppCompatActivity() {

    companion object {

        private const val MEDIA_ACTIONS_PLAY_PAUSE =
            PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY_PAUSE

        private const val MEDIA_ACTIONS_ALL =
            MEDIA_ACTIONS_PLAY_PAUSE or
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS

        private const val PLAYLIST_SIZE = 2

    }

    private lateinit var binding:ActivityMoiveBinding

    private lateinit var session:MediaSessionCompat

    private val movieListener = @RequiresApi(Build.VERSION_CODES.N) object : MovieView.MovieListener() {
        override fun onMovieStarted() {
            updatePlaybackState(
                PlaybackStateCompat.STATE_PLAYING,
                binding.movie.getCurrentPosition(),
                binding.movie.getVideoResourceId()
            )
        }

        override fun onMovieStopped() {
            updatePlaybackState(
                PlaybackStateCompat.STATE_PAUSED,
                binding.movie.getCurrentPosition(),
                binding.movie.getVideoResourceId()
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onMovieMinimized() {
            minimize()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Linkify.addLinks(binding.explanation,Linkify.ALL)
        binding.pip.setOnClickListener { minimize() }
        binding.switchExample.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.movie.doOnLayout { updatePictureInPictureParams()}
        binding.movie.setMovieListener(movieListener)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        initializeMediaSession()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun initializeMediaSession(){
        session = MediaSessionCompat(this,"Movie")
        session.isActive = true
        MediaControllerCompat.setMediaController(this,session.controller)

        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,binding.movie.title)
            .build()
        session.setMetadata(metadata)

        session.setCallback(MediaSessionCallback(binding.movie))

        val state = if (binding.movie.isPlaying){
            PlaybackStateCompat.STATE_PLAYING
        }else{
            PlaybackStateCompat.STATE_PAUSED
        }
        updatePlaybackState(
            state, MEDIA_ACTIONS_ALL,binding.movie.getCurrentPosition(),binding.movie.getVideoResourceId()
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStop() {
        super.onStop()
        binding.movie.pause()
        session.release()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRestart() {
        super.onRestart()
        if (!isInPictureInPictureMode){
            binding.movie.showControls()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        adjustFullScreen(newConfig)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            adjustFullScreen(resources.configuration)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode){
            binding.movie.hideControls()
        }else{
            if (!binding.movie.isPlaying){
                binding.movie.showControls()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePictureInPictureParams():PictureInPictureParams{
        val aspectRatio = Rational(binding.movie.width,binding.movie.height)
        val visibleRect = Rect()
        binding.movie.getGlobalVisibleRect(visibleRect)
        val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .setSourceRectHint(visibleRect)
                .setAutoEnterEnabled(true)
                .build()
        } else {
            PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .setSourceRectHint(visibleRect)
                .build()
        }
        setPictureInPictureParams(params)
        return params
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun minimize(){
        enterPictureInPictureMode(updatePictureInPictureParams())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun adjustFullScreen(config: Configuration){
        val insetsController = ViewCompat.getWindowInsetsController(window.decorView)
        insetsController?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            insetsController?.hide(WindowInsetsCompat.Type.systemBars())
            binding.scroll.visibility = View.GONE
            binding.movie.setAdjustViewBounds(false)
        }else{
            insetsController?.show(WindowInsetsCompat.Type.systemBars())
            binding.scroll.visibility = View.VISIBLE
            binding.movie.setAdjustViewBounds(true)
        }
    }




    private fun updatePlaybackState(
        @PlaybackStateCompat.State state:Int,
        position: Int,
        mediaId: Int
    ){
        val actions = session.controller.playbackState.actions
        updatePlaybackState(state,actions,position, mediaId)
    }

    private fun updatePlaybackState(
        @PlaybackStateCompat.State state:Int,
        playbackActions:Long,
        position:Int,
        mediaId:Int
    ){
        val builder = PlaybackStateCompat.Builder()
            .setActions(playbackActions)
            .setActiveQueueItemId(mediaId.toLong())
            .setState(state,position.toLong(),1.0f)
        session.setPlaybackState(builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private inner class MediaSessionCallback(
        private val movieView: MovieView
    ):MediaSessionCompat.Callback() {

        private var indexInPlaylist:Int = 1
        override fun onPlay() {
            movieView.play()
        }
        override fun onPause() {
            movieView.pause()
        }
        override fun onSkipToNext() {
            movieView.startVideo()
            if (indexInPlaylist < PLAYLIST_SIZE){
                indexInPlaylist++
                if (indexInPlaylist>= PLAYLIST_SIZE){
                    updatePlaybackState(
                        PlaybackStateCompat.STATE_PLAYING,
                        MEDIA_ACTIONS_PLAY_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS,
                        movieView.getCurrentPosition(),
                        movieView.getVideoResourceId()
                    )
                }else{
                    updatePlaybackState(
                        PlaybackStateCompat.STATE_PLAYING,
                        MEDIA_ACTIONS_ALL,
                        movieView.getCurrentPosition(),
                        movieView.getVideoResourceId()
                    )
                }
            }
        }

        override fun onSkipToPrevious() {
            movieView.startVideo()
            if (indexInPlaylist>0){
                indexInPlaylist--
                if (indexInPlaylist<=0){
                    updatePlaybackState(
                        PlaybackStateCompat.STATE_PLAYING,
                        MEDIA_ACTIONS_PLAY_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT,
                        movieView.getCurrentPosition(),
                        movieView.getVideoResourceId()
                    )
                }else{
                    updatePlaybackState(
                        PlaybackStateCompat.STATE_PLAYING,
                        MEDIA_ACTIONS_ALL,
                        movieView.getCurrentPosition(),
                        movieView.getVideoResourceId()
                    )
                }
            }
        }

    }















}