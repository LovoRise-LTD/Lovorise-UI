package com.lovorise.app.libs.media_player

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.ExoDatabaseProvider
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheKeyFactory
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.transformer.Composition
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil3.PlatformContext
import com.lovorise.app.libs.camera.ImageAspect
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelsLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.io.File

@OptIn(UnstableApi::class)
@Composable
actual fun VideoPlayerView(modifier: Modifier,url:String,thumbnailUrl:String,isVideoPausedInitially:Boolean,isPlaying:Boolean,onContentDuration:(Long)->Unit,onCurrentPosition:(Long)->Unit,clipRange: ClosedFloatingPointRange<Float>,enableAdvancedSettings:Boolean,isMuted:Boolean,onLoadingState:(Boolean)->Unit,aspect: ImageAspect,enableLoader:Boolean,slowVideo:Boolean){

    val context = LocalContext.current

    val videoPlayer = remember(url) { VideoPlayer(context) }
    val loadingState by videoPlayer.isLoading.collectAsState()

    LaunchedEffect(loadingState) {
        onLoadingState(loadingState)
    }

//    LaunchedEffect(videoPlayer.player?.currentPosition) {
//        while ()
//        println("the current position videoPlayer.player?.currentPosition is ${videoPlayer.player?.currentPosition}")
//        videoPlayer.player?.currentPosition?.let { onCurrentPosition(it) }
//    }

    LaunchedEffect(loadingState) {
        if (!loadingState && enableAdvancedSettings){
            videoPlayer.getVideoDuration()?.let {
                println("the total duration is $it ${it.toFloat()/5000f} ")
                if (it < 5000 && slowVideo){
                    videoPlayer.player?.playbackParameters = PlaybackParameters(it.toFloat()/5000f)
                    onContentDuration(it)
                }else{
                    onContentDuration(it)
                }
            }
        }
    }

    DisposableEffect(key1 = url) {
        videoPlayer.playVideo(url, shouldPauseInitially = isVideoPausedInitially, if (enableAdvancedSettings) clipRange else null)

        onDispose {
            // Cleanup the player when the composable is disposed
            println("clearing the player")
            videoPlayer.stopVideo()
        }
    }

    LaunchedEffect(isMuted) {
        videoPlayer.player?.volume = if (isMuted) 0f else 1f
    }



    LaunchedEffect(isPlaying) {
        if (isPlaying){
            videoPlayer.setClipRange(clipRange)
            videoPlayer.resumeVideo()
            if (enableAdvancedSettings) {
                while (true) {
                    delay(100)
                    videoPlayer.handleClipRange()
                    videoPlayer.player?.currentPosition?.let { onCurrentPosition(it) }
                    if (videoPlayer.player?.duration != null && videoPlayer.player?.duration!! > 0 && videoPlayer.player?.duration == videoPlayer.player?.currentPosition){
                        videoPlayer.player?.pause()
                        delay(100)
                        videoPlayer.player?.play()
                    }
                }
            }
        }else{
            videoPlayer.pauseVideo()
        }
    }

    LaunchedEffect(clipRange) {
        if (enableAdvancedSettings) {
            println("the clip range is $clipRange")
            videoPlayer.setClipRange(clipRange)
        }
    }

    Box(modifier.fillMaxSize()) {

        AndroidView(
            modifier = modifier.fillMaxSize().background(Color.Black),
            factory = {
                PlayerView(context).apply {
                    player = videoPlayer.player!!

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setResizeMode(if (aspect == ImageAspect.ASPECT_FIT) AspectRatioFrameLayout.RESIZE_MODE_FIT else AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)
                    useController = false
                }
            },
            update = {
            }
        )

        if (loadingState) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ReelsLoader(thumbnailUrl = thumbnailUrl, aspect = aspect, enableLoader = enableLoader)
            }
        }
    }

}

@OptIn(UnstableApi::class)
actual fun exportVideoSegment(context: PlatformContext,url: String, clipRange: ClosedFloatingPointRange<Float>, onExportComplete: (Boolean, String?) -> Unit) {
    // Prepare the Transformer
    val transformer = Transformer.Builder(context).build()
    val duration = getVideoDuration(context,Uri.parse(url))
    val outputFile = File(context.cacheDir, "ClippedVideo${Clock.System.now().toEpochMilliseconds()}.mp4")


    // Configure the input media item and set the time range
    val mediaItem = MediaItem.Builder()
        .setUri(url)
        .setClippingConfiguration(
            MediaItem.ClippingConfiguration.Builder()
                .setStartPositionMs(getDurationForFloat(duration,clipRange.start))
                .setEndPositionMs(getDurationForFloat(duration,clipRange.endInclusive))
                .build()
        ).build()


    // Start the transformation process
    transformer.start(
        mediaItem,
        outputFile.absolutePath
    )

    transformer.addListener( object : Transformer.Listener{
        override fun onCompleted(composition: Composition, exportResult: ExportResult) {
            super.onCompleted(composition, exportResult)
            onExportComplete(true, outputFile.absolutePath)
        }

        override fun onError(
            composition: Composition,
            exportResult: ExportResult,
            exportException: ExportException
        ) {
            super.onError(composition, exportResult, exportException)
            exportException.printStackTrace()
            onExportComplete(false, null)
        }
    })
}

private fun getDurationForFloat(duration:Long,value:Float):Long{
    return (duration * (value/100)).toLong()
}

fun getVideoDuration(context: Context, videoUri: Uri): Long {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, videoUri)
        val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        durationString?.toLong() ?: 0L
    } catch (e: Exception) {
        e.printStackTrace()
        0L
    } finally {
        retriever.release()
    }
}

@OptIn(UnstableApi::class)
actual fun preloadVideoListInBackground(urls:List<String>,context: PlatformContext){
    if (urls.isEmpty()) return
//    val cacheSize = 100L * 1024L * 1024L // 100 MB cache size
    println("the preloadVideoListInBackground is invoked ")
    val cache: SimpleCache = CacheSingleton.getInstance(context)

    val dataSourceFactory = DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory())
    val cacheDataSourceFactory = CacheDataSource.Factory()
        .setCache(cache)
        .setUpstreamDataSourceFactory(dataSourceFactory)
        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

    CoroutineScope(Dispatchers.IO).launch {
        urls.forEach { url ->

            if (!isVideoCached(cache, url)) { // Only preload if not already cached
                println("caching the video $url")
                preloadVideo(
                    url, cacheDataSourceFactory,
                    context = context
                )

            }else{
                println("the video is already cached $url")
            }
        }
    }
}

@OptIn(UnstableApi::class)
private suspend fun preloadVideo(url: String, cacheDataSourceFactory: CacheDataSource.Factory, context: PlatformContext) {
    // Create a new coroutine context for the background work
    withContext(Dispatchers.IO) {
        val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(url))

        val exoPlayer = ExoPlayer.Builder(context).build()

        // Prepare the player (still needs to be called on the main thread)
        withContext(Dispatchers.Main) {
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
        }

        // Add listener on the main thread to release when ready
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    exoPlayer.release()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                exoPlayer.release()
            }
        })
    }
}

@OptIn(UnstableApi::class)
fun isVideoCached(cache: Cache, url: String): Boolean {
    // Create a DataSpec for the URL
    val dataSpec = DataSpec(Uri.parse(url))

    // Create a CacheKeyFactory to generate the cache key
    val cacheKeyFactory = CacheKeyFactory.DEFAULT

    // Generate the cache key
    val cacheKey = cacheKeyFactory.buildCacheKey(dataSpec)

    // Check if the cache has any entries for this cache key
    return cache.getCachedSpans(cacheKey).isNotEmpty()
}



@UnstableApi
actual class VideoPlayer actual constructor(private val context: PlatformContext) {

   // var player: ExoPlayer? = ExoPlayer.Builder(context).build()
    var isLoading = MutableStateFlow(true)

    // Cache-related properties
 //   private val cacheSize: Long = 1024 * 1024 * 1024 // 100 MB
    private val cache: SimpleCache = CacheSingleton.getInstance(context)
    private val dataSourceFactory: CacheDataSource.Factory
    private var clipRange: ClosedFloatingPointRange<Float> = 0f..100f



    init {

        println("the initialization of AndroidVideoPlayer")

        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        val upstreamFactory = DefaultDataSource.Factory(context, httpDataSourceFactory)
        dataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    var player:ExoPlayer? = ExoPlayer.Builder(context)
        .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory))
        .build()


    fun getVideoDuration():Long?{
        return player?.duration
    }

    fun setClipRange(range: ClosedFloatingPointRange<Float>){
        //if (range.start != clipRange.start){
            player?.seekTo(getDurationForFloat(range.start))
       // }
        clipRange = range
        //println("the current position is from set clip range ${player?.duration} $range  ${player?.currentPosition} ${getDurationForFloat(clipRange.endInclusive - clipRange.start)}")

    }

    fun handleClipRange(){
        if (player != null && player!!.currentPosition >= getDurationForFloat(clipRange.endInclusive)) {
            player?.pause()
            player?.seekTo(getDurationForFloat(clipRange.start)) // Optionally loop back to start
            player?.play()
        }
    }

    private fun getDurationForFloat(value:Float):Long{
        return ((player?.duration ?: 0L) * (value/100)).toLong()
    }


    // Function to play video with caching
    fun playVideo(url: String,shouldPauseInitially:Boolean,range: ClosedFloatingPointRange<Float>?) {
        player?.apply {
            repeatMode = Player.REPEAT_MODE_ONE

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    this@VideoPlayer.isLoading.update {
                        playbackState == Player.STATE_BUFFERING
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    // Handle the error here
                    // For example, log the error or update the UI to inform the user
                    Log.e("AndroidVideoPlayer", "Playback error: ${error.message}")
                    // You can also show an error message to the user, if necessary
                    this@VideoPlayer.isLoading.update { false } // Stop loading if an error occurs
                    // Optionally, reset the player or take other appropriate actions
                }

                override fun onEvents(player: Player, events: Player.Events) {
                    if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)) {
                        // Check if playback has reached or exceeded the end position
                       // println("event has occurred:  EVENT_POSITION_DISCONTINUITY $clipRange")

                    }
                }
            })
        }
        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse(url))
            .build()

        player?.apply {
            setMediaItem(mediaItem)
            prepare()
            if (range != null) {
                setClipRange(range)
            }
            if (!shouldPauseInitially){
                play()
            }
          //  playWhenReady = true
        }
    }

    fun resumeVideo(){

        player?.play()
    }

    fun pauseVideo(){
        player?.pause()
    }



    fun stopVideo() {
        player?.stop()
        player?.release()
     //   player = null
        println("released the player")
    }

    // Optionally, clear the cache when needed
    fun clearCache() {
        cache.release()
    }
}


@UnstableApi
object CacheSingleton {
    private var simpleCache: SimpleCache? = null
    private const val CACHE_SIZE: Long = 1024 * 1024 * 1024 // 100 MB

    fun getInstance(context: Context): SimpleCache {
        return simpleCache ?: synchronized(this) {
            simpleCache ?: run {
                val cacheDir = File(context.cacheDir, "media_cache")
                val evictor = LeastRecentlyUsedCacheEvictor(CACHE_SIZE)
                val databaseProvider = ExoDatabaseProvider(context)
                SimpleCache(cacheDir, evictor, databaseProvider).also { cacheInstance ->
                    simpleCache = cacheInstance
                }
            }
        }
    }

    fun releaseCache() {
        simpleCache?.release()
        simpleCache = null
    }
}
