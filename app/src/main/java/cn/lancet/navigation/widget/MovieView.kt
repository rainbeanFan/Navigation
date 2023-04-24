package cn.lancet.navigation.widget

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.transition.TransitionManager
import cn.lancet.navigation.R
import java.io.IOException
import java.lang.ref.WeakReference

@RequiresApi(Build.VERSION_CODES.N)
class MovieView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?=null,
    defStyleAttr: Int = 0
):RelativeLayout(context, attrs, defStyleAttr){

    companion object {

        private const val TAG = "MovieView"

        private const val FAST_FORWARD_REWIND_INTERVAL = 5000

        private const val TIMEOUT_CONTROLS = 3000L

    }

    abstract class MovieListener {

        open fun onMovieStarted() {}

        open fun onMovieStopped() {}

        open fun onMovieMinimized() {}

    }

    private val surfaceView: SurfaceView

    private val toggle:AppCompatImageButton
    private val shade:View
    private val fastForward:AppCompatImageButton
    private val fastRewind:AppCompatImageButton
    private val minimize:AppCompatImageButton

    internal var mediaPlayer:MediaPlayer?=null

    @RawRes
    private var videoResourceId:Int = 0

    var title:String = ""

    private var adjustViewBounds:Boolean = false
    private var timeoutHandler:TimeoutHandler?=null
    private var movieListener:MovieListener? = null

    private var savedCurrentPosition:Int = 0


    init {
        setBackgroundColor(Color.BLACK)

        View.inflate(context, R.layout.view_movie,this)
        surfaceView = findViewById(R.id.surface)
        shade = findViewById(R.id.shade)
        toggle = findViewById(R.id.toggle)
        fastForward = findViewById(R.id.fast_forward)
        fastRewind = findViewById(R.id.fast_rewind)
        minimize = findViewById(R.id.minimize)

        val a = context.obtainStyledAttributes(attrs,R.styleable.MovieView,
            defStyleAttr,R.style.Widget_PictureInPicture_MovieView)
        setVideoResourceId(a.getResourceId(R.styleable.MovieView_android_src,0))
        setAdjustViewBounds(a.getBoolean(R.styleable.MovieView_android_adjustViewBounds,false))
        title = a.getString(R.styleable.MovieView_android_title)?:""
        a.recycle()

        val listener = OnClickListener { view ->
            when(view.id) {
                R.id.surface -> toggleControls()
                R.id.toggle -> toggle()
                R.id.fast_forward -> fastForward()
                R.id.fast_rewind -> fastRewind()
                R.id.minimize -> movieListener?.onMovieMinimized()
            }

            mediaPlayer?.let { player->
                if (timeoutHandler == null){
                    timeoutHandler = TimeoutHandler(this@MovieView)
                }
                timeoutHandler?.let { handler->
                    handler.removeMessages(TimeoutHandler.MESSAGE_HIDE_CONTROLS)
                    if (player.isPlaying){
                        handler.sendEmptyMessageDelayed(TimeoutHandler.MESSAGE_HIDE_CONTROLS,
                            TIMEOUT_CONTROLS)
                    }
                }
            }

        }

        surfaceView.setOnClickListener(listener)
        toggle.setOnClickListener(listener)
        fastForward.setOnClickListener(listener)
        fastRewind.setOnClickListener(listener)
        minimize.setOnClickListener(listener)

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun surfaceCreated(holder: SurfaceHolder) {
                openVideo(holder.surface)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                mediaPlayer?.let { savedCurrentPosition = it.currentPosition }
                closeVideo()
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mediaPlayer?.let { player->
            val videoWidth = player.videoWidth
            val videoHeight = player.videoHeight
            if (videoWidth!=0 && videoHeight!=0){
                val aspectRatio = videoHeight.toFloat()/videoWidth
                val width = MeasureSpec.getSize(widthMeasureSpec)
                val widthMode = MeasureSpec.getMode(widthMeasureSpec)
                val height = MeasureSpec.getSize(heightMeasureSpec)
                val heightMode = MeasureSpec.getMode(heightMeasureSpec)

                if (adjustViewBounds){
                    if (widthMode == MeasureSpec.EXACTLY && heightMode!=MeasureSpec.EXACTLY){
                        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((width * aspectRatio).toInt(),MeasureSpec.EXACTLY))
                    }else if(widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
                        super.onMeasure(MeasureSpec.makeMeasureSpec((height/aspectRatio).toInt(), MeasureSpec.EXACTLY), heightMeasureSpec)
                    }else{
                        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((width*aspectRatio).toInt(),MeasureSpec.EXACTLY))
                    }
                }else{
                    val viewRatio = height.toFloat() / width
                    if (aspectRatio > viewRatio){
                        val padding = ((width - height/aspectRatio)/2).toInt()
                        setPadding(padding,0,padding,0)
                    }else{
                        val padding = ((height - width * aspectRatio)/2).toInt()
                        setPadding(0,padding,0,padding)
                    }
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                }
                return
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDetachedFromWindow() {
        timeoutHandler?.removeMessages(TimeoutHandler.MESSAGE_HIDE_CONTROLS)
        timeoutHandler = null
        super.onDetachedFromWindow()
    }

    fun getVideoResourceId():Int = videoResourceId

    fun setMovieListener(movieListener:MovieListener?) {
        this.movieListener = movieListener
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setVideoResourceId(@RawRes id: Int){
        if (id == videoResourceId) return
        videoResourceId = id
        val surface = surfaceView.holder.surface
        if (surface!=null && surface.isValid){
            closeVideo()
            openVideo(surface)
        }
    }

    fun setAdjustViewBounds(adjustViewBounds: Boolean){
        if (this.adjustViewBounds == adjustViewBounds) return
        this.adjustViewBounds = adjustViewBounds
        if (adjustViewBounds){
            background = null
        }else{
            setBackgroundColor(Color.BLACK)
        }
        requestLayout()
    }

    fun showControls() {
        TransitionManager.beginDelayedTransition(this)
        shade.visibility = View.VISIBLE
        toggle.visibility = View.VISIBLE
        fastForward.visibility = View.VISIBLE
        fastRewind.visibility = View.VISIBLE
        minimize.visibility = View.VISIBLE
    }

    fun hideControls() {
        TransitionManager.beginDelayedTransition(this)
        shade.visibility = View.INVISIBLE
        toggle.visibility = View.INVISIBLE
        fastForward.visibility = View.INVISIBLE
        fastRewind.visibility = View.INVISIBLE
        minimize.visibility = View.INVISIBLE
    }

    private fun fastForward(){
        mediaPlayer?.let { it.seekTo(it.currentPosition + FAST_FORWARD_REWIND_INTERVAL) }
    }

    private fun fastRewind(){
        mediaPlayer?.let { it.seekTo(it.currentPosition - FAST_FORWARD_REWIND_INTERVAL) }
    }

    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition?:0

    val isPlaying: Boolean
        get() = mediaPlayer?.isPlaying?:false

    fun play(){
        if (mediaPlayer == null) return
        mediaPlayer!!.start()
        adjustToggleState()
        keepScreenOn = true
        movieListener?.onMovieStarted()
    }

    fun pause(){
        if (mediaPlayer == null) {
            adjustToggleState()
            return
        }
        mediaPlayer!!.pause()
        adjustToggleState()
        keepScreenOn = false
        movieListener?.onMovieStopped()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    internal fun openVideo(surface: Surface){
        if (videoResourceId == 0) return
        mediaPlayer = MediaPlayer()
        mediaPlayer?.let { player->
            player.setSurface(surface)
            startVideo()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startVideo() {
        mediaPlayer?.let { player ->
            player.reset()
            try {
                resources.openRawResourceFd(videoResourceId).use { fd ->
                    player.setDataSource(fd)
                    player.setOnPreparedListener { mediaPlayer ->
                        requestLayout()
                        if (savedCurrentPosition>0){
                            mediaPlayer.seekTo(savedCurrentPosition)
                            savedCurrentPosition = 0
                        }else{
                            play()
                        }
                    }
                    player.setOnCompletionListener {
                        adjustToggleState()
                        keepScreenOn = false
                        movieListener?.onMovieStopped()
                    }
                    player.prepare()
                }
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    internal fun closeVideo() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
    private fun toggle(){
        mediaPlayer?.let { if (it.isPlaying) pause() else play() }
    }
    private fun toggleControls(){
        if (shade.visibility == View.VISIBLE){
            hideControls()
        }else{
            showControls()
        }
    }
    private fun adjustToggleState(){
        mediaPlayer?.let {
            if (it.isPlaying){
                toggle.contentDescription = "Pause"
                toggle.setImageResource(R.drawable.ic_pause_64dp)
            }else{
                toggle.contentDescription = "Play"
                toggle.setImageResource(R.drawable.ic_play_arrow_64dp)
            }
        }
    }

    private class TimeoutHandler(view:MovieView):Handler(Looper.getMainLooper()) {

        private val movieViewRef:WeakReference<MovieView> = WeakReference(view)

        override fun handleMessage(msg: Message) {
            when(msg.what){
                MESSAGE_HIDE_CONTROLS -> {
                    movieViewRef.get()?.hideControls()
                }
                else -> super.handleMessage(msg)
            }
            super.handleMessage(msg)
        }

        companion object {
            const val MESSAGE_HIDE_CONTROLS = 1
        }

    }


























}