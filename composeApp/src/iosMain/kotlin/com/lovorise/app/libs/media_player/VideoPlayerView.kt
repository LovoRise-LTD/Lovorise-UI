package com.lovorise.app.libs.media_player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import coil3.PlatformContext
import com.lovorise.app.MediaPlayerWorkerInstance
import com.lovorise.app.libs.camera.ImageAspect
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelsLoader
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import platform.AVFoundation.AVAssetExportPresetHighestQuality
import platform.AVFoundation.AVAssetExportSession
import platform.AVFoundation.AVAssetExportSessionStatusCancelled
import platform.AVFoundation.AVAssetExportSessionStatusCompleted
import platform.AVFoundation.AVAssetExportSessionStatusFailed
import platform.AVFoundation.AVFileTypeMPEG4
import platform.AVFoundation.AVLayerVideoGravityResizeAspect
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.AVQueuePlayer
import platform.AVFoundation.AVURLAsset
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.muted
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.playbackLikelyToKeepUp
import platform.AVFoundation.rate
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeRange
import platform.AVKit.AVPlayerViewController
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.CoreMedia.CMTimeRangeMake
import platform.Foundation.NSFileManager
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.darwin.Float64
import platform.darwin.NSObject

/*
   val reels = listOf(
        "https://videos.pexels.com/video-files/5540220/5540220-sd_360_640_30fps.mp4",
        "https://videos.pexels.com/video-files/9382073/9382073-sd_360_640_30fps.mp4",

        "https://videos.pexels.com/video-files/27831553/12237746_360_640_30fps.mp4",
        "https://videos.pexels.com/video-files/15283155/15283155-sd_360_640_30fps.mp4",
        "https://videos.pexels.com/video-files/15000517/15000517-sd_360_640_30fps.mp4",
    )
* */



@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayerView(modifier: Modifier,url: String,thumbnailUrl:String,isVideoPausedInitially:Boolean,isPlaying:Boolean,onContentDuration:(Long)->Unit,onCurrentPosition:(Long)->Unit,clipRange: ClosedFloatingPointRange<Float>,enableAdvancedSettings:Boolean,isMuted:Boolean,onLoadingState:(Boolean)->Unit,aspect: ImageAspect,enableLoader:Boolean,slowVideo:Boolean){
    val playerItem = remember { mutableStateOf<AVPlayerItem?>(null) }

    val player: AVQueuePlayer by remember { mutableStateOf(AVQueuePlayer(playerItem.value)) }
    val playerLayer by remember { mutableStateOf(AVPlayerLayer()) }
    val avPlayerViewController = remember { AVPlayerViewController() }
    val isPause by remember { mutableStateOf(false) }
    avPlayerViewController.player = player
    avPlayerViewController.showsPlaybackControls = false
    avPlayerViewController.videoGravity = if (aspect == ImageAspect.ASPECT_FIT) AVLayerVideoGravityResizeAspect else AVLayerVideoGravityResizeAspectFill
    val playerContainer =UIView(frame = UIScreen.mainScreen.bounds).apply {
        layer.addSublayer(playerLayer)
    }
    player.muted = isMuted

    var isLoading by remember { mutableStateOf(true) }
    var currentTime by remember { mutableStateOf(0.0) }

    LaunchedEffect(isLoading){
        delay(1000L)
        onLoadingState(isLoading)
    }

//    fun setPlayerRate(speed: PlayerSpeed) {
//        player.rate = when (speed) {
//            PlayerSpeed.X0_5 -> 0.5f
//            PlayerSpeed.X1 -> 1f
//            PlayerSpeed.X1_5 -> 1.5f
//            PlayerSpeed.X2 -> 2f
//        }
//    }

    LaunchedEffect(isMuted){
        player.muted = isMuted
    }

    LaunchedEffect(clipRange){
        if(enableAdvancedSettings) {
            val duration = player.currentItem?.duration?.let { CMTimeGetSeconds(it) } ?: 0.0
            player.seekToTime(
                CMTimeMakeWithSeconds(
                    getDurationForFloat(duration, clipRange.start),
                    1
                )
            )
            println("")
        }
    }

    val scope = rememberCoroutineScope()


    LaunchedEffect(currentTime){
        if (enableAdvancedSettings) {
            val duration = player.currentItem?.duration?.let { CMTimeGetSeconds(it) } ?: 0.0
            handleClipRange(duration, currentTime, player, clipRange)
        }
    }

    LaunchedEffect(url) {
      //  val urlObject = NSURL.URLWithString(url)
      //  val newItem = urlObject?.let { AVPlayerItem(uRL = it) }
        val item = if (url.startsWith("http")) MediaPlayerWorkerInstance.DEFAULT?.getMediaItem(url) else AVPlayerItem(NSURL.fileURLWithPath(url))

        playerItem.value = item as AVPlayerItem?

      //  playerItem.value = newItem
        playerItem.value?.let {
            player.replaceCurrentItemWithPlayerItem(it)
        }
        if (isPause) { player.pause() } else {
            if (!isVideoPausedInitially) {
                player.play()
            }
           // setPlayerRate(speed)
        }
    }

    LaunchedEffect(isPlaying){
        if (isPlaying){
            player.play()
        }else{
            player.pause()
        }
    }

    Box {
        key(url) {
            UIKitView(
                factory = {
                    playerContainer.addSubview(avPlayerViewController.view)
                    playerContainer
                },
                modifier = modifier,
                update = { _ ->
                    MainScope().launch {
                        if (isPause) {
                            player.pause()
                        } else {
                            if (!isVideoPausedInitially) {
                                player.play()
                            }
                        }
                        UIApplication.sharedApplication.idleTimerDisabled = isPause.not()
                    }
                },
                properties = UIKitInteropProperties(
                    isInteractive = false,
                    isNativeAccessibilityEnabled = true
                )
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                ReelsLoader(Modifier.align(Alignment.Center),thumbnailUrl, aspect = aspect,enableLoader=enableLoader)
            }
        }
    }

    DisposableEffect(Unit) {
        val observerObject = object : NSObject() {
            @ObjCAction
            fun onPlayerItemDidPlayToEndTime() {
                player.currentItem?.let { item ->
                    // Delay for 100ms before restarting
                    scope.launch {
                        delay(100)
                        player.seekToTime(CMTimeMakeWithSeconds(0.0, 600))
                        player.removeItem(item)
                        player.insertItem(item, afterItem = null)
                        player.play()
                    }
                }
            }
        }

        val timeObserver = player.addPeriodicTimeObserverForInterval(
            CMTimeMakeWithSeconds(0.1, 600),
            null
        ) { _ ->
            if (enableAdvancedSettings) {
                val current = CMTimeGetSeconds(player.currentTime())
                if (current <= 1.0) {
                    val duration = player.currentItem?.duration?.let { CMTimeGetSeconds(it) } ?: 0.0

                    println("the duration is $duration")

                    if (duration < 5.0){
                        player.rate = duration.toFloat()/5f
                    }

                    println("the duration is ")
                    onContentDuration((duration *1000).toLong())
                }
                currentTime = current
                onCurrentPosition((current*1000).toLong())
                println("the current time is $current")
            }
//                    currentTime(current.toInt())
//                    totalTime(duration.toInt())
            isLoading = player.currentItem?.playbackLikelyToKeepUp?.not() ?: false
//                }
//            }
        }


        NSNotificationCenter.defaultCenter().addObserver(
            observerObject,
            NSSelectorFromString("onPlayerItemDidPlayToEndTime"),
            AVPlayerItemDidPlayToEndTimeNotification,
            player.currentItem
        )

        onDispose {
            UIApplication.sharedApplication.idleTimerDisabled = false
            player.pause()
            player.replaceCurrentItemWithPlayerItem(null)
            NSNotificationCenter.defaultCenter().removeObserver(observerObject)
            player.removeTimeObserver(timeObserver)
        }
    }


}

@OptIn(ExperimentalForeignApi::class)
fun handleClipRange(duration:Double, currentPosition:Double, player: AVQueuePlayer, clipRange: ClosedFloatingPointRange<Float>){
    println("the clip-range is $clipRange")
    if (currentPosition >= getDurationForFloat(duration,clipRange.endInclusive)) {
        player.pause()
        player.seekToTime(CMTimeMakeWithSeconds(getDurationForFloat(duration,clipRange.start),1))
        player.play()
    }
}

fun getDurationForFloat(duration:Float64,value:Float):Float64{
    return ((duration) * (value/100))
}

@OptIn(ExperimentalForeignApi::class)
actual fun exportVideoSegment(context: PlatformContext,url: String, clipRange: ClosedFloatingPointRange<Float>, onExportComplete: (Boolean, String?) -> Unit) {
    if (!NSFileManager.defaultManager.fileExistsAtPath(url)) {
        println("Error: File does not exist at path: $url")
        onExportComplete(false, null)
        return
    }
    val asset = AVURLAsset(NSURL.fileURLWithPath(url),options = null)
    val duration = CMTimeGetSeconds(asset.duration)

    // Ensure clipRange is within the video duration
    val startSeconds = getDurationForFloat(duration,clipRange.start)
    val endSeconds = getDurationForFloat(duration,clipRange.endInclusive)

    val timeRange = CMTimeRangeMake(
        CMTimeMakeWithSeconds(startSeconds, 600),
        CMTimeMakeWithSeconds(endSeconds - startSeconds, 600)
    )

    // Set up output file path
    val fileManager = NSFileManager.defaultManager
    val tempDir = NSTemporaryDirectory().dropLastWhile { it == '/' }
    println("the temp dir is $tempDir")
    val outputUrl = NSURL.fileURLWithPath("$tempDir/ClippedVideo_${Clock.System.now().toEpochMilliseconds()}.mp4")

    if (fileManager.fileExistsAtPath(outputUrl.path!!)) {
        fileManager.removeItemAtURL(outputUrl, null)
    }

    val exportSession = AVAssetExportSession(asset, AVAssetExportPresetHighestQuality)
    exportSession?.let { session ->
        session.outputURL = outputUrl
        session.outputFileType = AVFileTypeMPEG4
        session.timeRange = timeRange

        session.exportAsynchronouslyWithCompletionHandler {
            when (session.status) {
                AVAssetExportSessionStatusCompleted -> {
                    onExportComplete(true, outputUrl.path)
                }
                AVAssetExportSessionStatusFailed,
                AVAssetExportSessionStatusCancelled -> {
                    println("Export failed: ${session.error?.localizedDescription}")
                    onExportComplete(false, null)
                }
                else -> {
                    println("Export status: ${session.status}")
                    onExportComplete(false, null)
                }
            }
        }
    }
}


@OptIn(ExperimentalForeignApi::class)
fun getVideoDuration(url: String): Double {
    val nsUrl = NSURL.URLWithString(url) ?: return 0.0
    val asset = AVURLAsset(uRL = nsUrl, options = null)
    return CMTimeGetSeconds(asset.duration)
}

actual class VideoPlayer actual constructor(context: PlatformContext){

}

actual fun preloadVideoListInBackground(urls:List<String>,context: PlatformContext) {
}


/*
@OptIn(ExperimentalForeignApi::class)
@Composable
fun VideoPlayerView1(modifier: Modifier,url:String){
    println("hello $url")
    val player = remember { IOSVideoPlayer(url) }

    val playerReady by player.isPlayerReady.collectAsState()
    val isLoading by player.isLoading.collectAsState()


    DisposableEffect(url){
       // player.playVideo(url)
        onDispose {
            println("VideoPlayer for ${url.substring(38..url.lastIndex)} disposed")
            player.stopVideo()
        }
    }


    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        player.let {

            if (playerReady) {
                UIKitView(
                    modifier = Modifier.fillMaxSize().background(Color.Red),
                    factory = {

                        val playerViewController = AVPlayerViewController()
                        val containerView =
                            UIView(frame = UIScreen.mainScreen.bounds) // Create a container UIView

                        playerViewController.player = it.playerQueue!!
                        //  player.player?.play()
                        playerViewController.showsPlaybackControls = false
                        playerViewController.setWantsFullScreenLayout(true)
                        playerViewController.setAllowsVideoFrameAnalysis(false)
                        playerViewController.entersFullScreenWhenPlaybackBegins = true


                        playerViewController.setVideoGravity(AVLayerVideoGravityResizeAspectFill)
                        playerViewController.setRestoresFocusAfterTransition(true)

                        containerView.addSubview(playerViewController.view)
                        containerView

                    },
                    update = {},
                    onRelease = {
                        println("on release")
                        //   player.stopVideo()
                    }
                )

            }
        }
        if(isLoading) {
            CircularProgressIndicator(color = Color.White)
        }
    }
} */


/*
* @OptIn(ExperimentalForeignApi::class)
class IOSVideoPlayer(private val url:String):AutoCloseable{

    private val videoUrl = NSURL.URLWithString(url)
    private val asset = AVAsset.assetWithURL(videoUrl!!)

    var player:AVPlayer? = AVPlayer(AVPlayerItem(asset))
    val isLoading = MutableStateFlow(false)
    val isPlayerReady = MutableStateFlow(false)

    private var playerLooper: AVPlayerLooper? = null
     var playerQueue: AVQueuePlayer? = null

    private var playbackStalledObserver: NSObjectProtocol? = null
    private var bufferObserver: NSObjectProtocol? = null

    private var endPlaybackObserver: NSObjectProtocol? = null

    // private var listener: MediaPlayerListener? = null

    init {
       // stopVideo()

        playerQueue  = AVQueuePlayer()

        // Define the loop range to cover the entire duration of the video
        // Define the loop range to cover the entire duration of the video
        val duration = asset.duration
        println("the duration is ${duration.size}")
        val start = CMTimeMake(0, 1)
        val timeRange = CMTimeRangeMake(start, duration)


        playerLooper = AVPlayerLooper(playerQueue!!, player?.currentItem!!, timeRange)

        println("on init")
        playVideo(url,playerQueue!!)
       // addObservers()
    }

    private fun addObservers(){

        println("on added observer $url")

      //  removeObservers()
       // stopVideo()
        endPlaybackObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            AVPlayerItemDidPlayToEndTimeNotification,
            `object` = player!!.currentItem!!,
            queue = NSOperationQueue.mainQueue()
        ) { notification ->

            println("on video ended $url")

            // Handle the video finishing here
          //  onVideoPlaybackEnded()
        }

        observeBufferingState(playerItem = player!!.currentItem!!)
    }


    // Ensure observers are removed properly
    private fun removeObservers() {
        endPlaybackObserver?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
            endPlaybackObserver = null
        }

        playbackStalledObserver?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
            playbackStalledObserver = null
        }

        bufferObserver?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
            bufferObserver = null
        }
    }

    private fun observeBufferingState(playerItem: AVPlayerItem) {

        println("on observeBufferingState ")
        // Observe playback stalled (buffering started)
        playbackStalledObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            AVPlayerItemPlaybackStalledNotification,
            `object` = playerItem,
            queue = NSOperationQueue.mainQueue()
        ) { notification ->
            isLoading.update { true }
            println("Playback stalled: Buffering started.")
        }

        // Optionally: observe access log entries for detailed playback info
        bufferObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            AVPlayerItemNewAccessLogEntryNotification,
            `object` = playerItem,
            queue = NSOperationQueue.mainQueue()
        ) { notification ->
            if(playerItem.isPlaybackLikelyToKeepUp()) {
                isLoading.update { false }
            }else {
                isLoading.update { true }
                println("Still buffering.")
            }
        }
    }


    private fun playVideo(url: String,playerQ: AVQueuePlayer) {
        println("play video called $url")

        playerQueue?.muted = false

        playerQueue?.play()

        isPlayerReady.update { true }

    }


    @OptIn(ExperimentalForeignApi::class)
    private fun onVideoPlaybackEnded() {
        println("onVideoPlaybackEnded ${player?.currentItem} $url")
        isPlayerReady.update { false }
        isPlayerReady.update { true }
        player?.pause()

        // Instead of seeking immediately, try to preserve the last frame
        player?.actionAtItemEnd = AVPlayerActionAtItemEndNone

        player?.seekToTime(CMTimeMake(value = 0, timescale = 1))
        player?.play()

    }


    fun stopVideo() {
        println("on cleanup started $url")
     //   removeObservers()

        playerQueue?.pause()
      //  player?.replaceCurrentItemWithPlayerItem(null)
        playerQueue = null
        //player = null
    }

    override fun close() {
        stopVideo()
    }


}*/

/*
@OptIn(ExperimentalForeignApi::class)
class IOSVideoPlayer1(private val url:String):AutoCloseable{

    private val videoUrl = NSURL.URLWithString(url)
    private val asset = AVAsset.assetWithURL(videoUrl!!)

    var player:AVPlayer? = AVPlayer(AVPlayerItem(asset))
    val isLoading = MutableStateFlow(false)
    val isPlayerReady = MutableStateFlow(false)

    private var playerLooper: AVPlayerLooper? = null
    var playerQueue: AVQueuePlayer? = null

    private var playbackStalledObserver: NSObjectProtocol? = null
    private var bufferObserver: NSObjectProtocol? = null

    private var endPlaybackObserver: NSObjectProtocol? = null

    // private var listener: MediaPlayerListener? = null

    init {
        // stopVideo()

        playerQueue  = AVQueuePlayer()

        // Define the loop range to cover the entire duration of the video
        // Define the loop range to cover the entire duration of the video
        val duration = asset.duration
        println("the duration is ${duration.size}")
        val start = CMTimeMake(0, 1)
        val timeRange = CMTimeRangeMake(start, duration)


        playerLooper = AVPlayerLooper(playerQueue!!, player?.currentItem!!, timeRange)

        println("on init")
        playVideo(url,playerQueue!!)
        // addObservers()
    }

    private fun addObservers(){

        println("on added observer $url")

        //  removeObservers()
        // stopVideo()
        endPlaybackObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            AVPlayerItemDidPlayToEndTimeNotification,
            `object` = player!!.currentItem!!,
            queue = NSOperationQueue.mainQueue()
        ) { notification ->

            println("on video ended $url")

            // Handle the video finishing here
            //  onVideoPlaybackEnded()
        }

        observeBufferingState(playerItem = player!!.currentItem!!)
    }


    // Ensure observers are removed properly
    private fun removeObservers() {
        endPlaybackObserver?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
            endPlaybackObserver = null
        }

        playbackStalledObserver?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
            playbackStalledObserver = null
        }

        bufferObserver?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
            bufferObserver = null
        }
    }

    private fun observeBufferingState(playerItem: AVPlayerItem) {

        println("on observeBufferingState ")
        // Observe playback stalled (buffering started)
        playbackStalledObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            AVPlayerItemPlaybackStalledNotification,
            `object` = playerItem,
            queue = NSOperationQueue.mainQueue()
        ) { notification ->
            isLoading.update { true }
            println("Playback stalled: Buffering started.")
        }

        // Optionally: observe access log entries for detailed playback info
        bufferObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            AVPlayerItemNewAccessLogEntryNotification,
            `object` = playerItem,
            queue = NSOperationQueue.mainQueue()
        ) { notification ->
            if(playerItem.isPlaybackLikelyToKeepUp()) {
                isLoading.update { false }
            }else {
                isLoading.update { true }
                println("Still buffering.")
            }
        }
    }


    private fun playVideo(url: String,playerQ: AVQueuePlayer) {
        println("play video called $url")

        playerQueue?.muted = false

        playerQueue?.play()

        isPlayerReady.update { true }

    }


    @OptIn(ExperimentalForeignApi::class)
    private fun onVideoPlaybackEnded() {
        println("onVideoPlaybackEnded ${player?.currentItem} $url")
        isPlayerReady.update { false }
        isPlayerReady.update { true }
        player?.pause()

        // Instead of seeking immediately, try to preserve the last frame
        player?.actionAtItemEnd = AVPlayerActionAtItemEndNone

        player?.seekToTime(CMTimeMake(value = 0, timescale = 1))
        player?.play()

    }


    fun stopVideo() {
        println("on cleanup started $url")
        //   removeObservers()

        player?.pause()
        //  player?.replaceCurrentItemWithPlayerItem(null)
        player = null
        //player = null
    }

    override fun close() {
        // stopVideo()
    }


}
 */