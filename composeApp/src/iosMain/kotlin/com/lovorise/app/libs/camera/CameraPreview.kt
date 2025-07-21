package com.lovorise.app.libs.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import androidx.compose.ui.viewinterop.UIKitViewController
import com.lovorise.app.CacheUtilInstance
import com.lovorise.app.libs.camera.CustomCameraController.CameraException
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceDiscoverySession
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDevicePositionFront
import platform.AVFoundation.AVCaptureDevicePositionUnspecified
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVCaptureFileOutput
import platform.AVFoundation.AVCaptureFileOutputRecordingDelegateProtocol
import platform.AVFoundation.AVCaptureFlashModeOff
import platform.AVFoundation.AVCaptureFlashModeOn
import platform.AVFoundation.AVCaptureMovieFileOutput
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureTorchModeOff
import platform.AVFoundation.AVCaptureTorchModeOn
import platform.AVFoundation.AVCaptureVideoOrientation
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeLeft
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeRight
import platform.AVFoundation.AVCaptureVideoOrientationPortrait
import platform.AVFoundation.AVCaptureVideoOrientationPortraitUpsideDown
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.fileDataRepresentation
import platform.AVFoundation.hasTorch
import platform.AVFoundation.isTorchAvailable
import platform.AVFoundation.position
import platform.AVFoundation.torchMode
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.stringWithString
import platform.Foundation.temporaryDirectory
import platform.Photos.PHAsset
import platform.Photos.PHImageContentModeAspectFill
import platform.Photos.PHImageManager
import platform.Photos.PHImageRequestOptions
import platform.Photos.PHImageRequestOptionsDeliveryModeHighQualityFormat
import platform.Photos.PHImageRequestOptionsResizeModeFast
import platform.UIKit.UIColor.Companion.blackColor
import platform.UIKit.UIColor.Companion.redColor
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.UIKit.UIViewContentMode
import platform.UIKit.UIViewController
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue


@Composable
actual fun IosImageView(imgData: Any, modifier: Modifier,onImageLoaded:(Any)->Unit,size:IntSize?,aspect: ImageAspect?,refreshCache:Boolean) {
    var image by remember { mutableStateOf<UIImage?>(null) }
   // val imageCache = remember { NSCache().apply{ countLimit = 400u } }
    val imageCache = remember { CacheUtilInstance.DEFAULT }

    LaunchedEffect(true) {
        if (imgData is String){
            withContext(Dispatchers.IO){
            val cacheKey = NSString.stringWithString(imgData)

           // val cachedImage = imageCache.objectForKey(cacheKey) as? UIImage
            val cachedImage = imageCache?.getCachedImage(imgData) as? UIImage
            println("cache for id: $imgData ${cachedImage != null}")
            if (cachedImage != null && !refreshCache){
                image = cachedImage
            }
            else{
                println("fetching the image for id: $imgData")
                image = cachedImage

                    val assets = PHAsset.fetchAssetsWithLocalIdentifiers(listOf(imgData as NSString), null)
                    assets.firstObject?.let {
                        image = getVideoThumbnail(it as PHAsset, size = size)
                        image?.let { it1 -> onImageLoaded(it1) }

                        println("the image data for $imgData is ${image != null}")
                    //    println("the imageCache is ${imageCache.countLimit}")
                        imageCache?.cacheImage(image!!, imgData)
                    }
                }

            }
        }
        println("IosImageView called for $imgData")
    }
//    Box(modifier){
    key(imgData,image) {
        UIKitView(
            modifier = modifier, factory = {
                val imageView = UIImageView()

                when (aspect) {
                    ImageAspect.ASPECT_FIT -> {
                        imageView.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
                    }
                    ImageAspect.ASPECT_FILL -> {
                        imageView.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFill
                    }
                    ImageAspect.FILL -> {
                        imageView.contentMode = UIViewContentMode.UIViewContentModeScaleToFill
                    }
                    null -> {}
                }

                // Convert imgData to UIImage and set it to imageView
                val uiImage = when (imgData) {
                    is String -> image  // Load from file path
                    is NSData -> UIImage.imageWithData(imgData)            // Load from NSData
                    is UIImage -> imgData                                  // UIImage directly
                    else -> null
                }
                println("the uiImage $uiImage")

                // Assign the UIImage to the UIImageView
                imageView.image = uiImage
                imageView
            }, properties = UIKitInteropProperties(
                isNativeAccessibilityEnabled = true,
                isInteractive = true
            ),

        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private suspend fun getVideoThumbnail(asset: PHAsset,size:IntSize?): UIImage? = withContext(Dispatchers.IO) {
    suspendCancellableCoroutine { continuation ->
    val imageManager = PHImageManager.defaultManager()
    val requestOptions = PHImageRequestOptions().apply {
        synchronous = false
        networkAccessAllowed = true
       // allowSecondaryDegradedImage = true
        resizeMode = PHImageRequestOptionsResizeModeFast
        deliveryMode = PHImageRequestOptionsDeliveryModeHighQualityFormat
    }
    val width = size?.width?.toDouble() ?: asset.pixelWidth.toDouble()
    val height = size?.height?.toDouble() ?: asset.pixelHeight.toDouble()
    val aspectRatio = (width/height)*0.5

    imageManager.requestImageForAsset(
        asset = asset,
        targetSize = CGSizeMake(width,height),
        contentMode = PHImageContentModeAspectFill,
        options = requestOptions
    ) { image, _ ->
        if (image != null) {
            continuation.resume(
                image
            ) {}
        } else {
            continuation.resume(null,{})
        }
    }
}
}




@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
actual fun CameraPreview(
    modifier: Modifier,
    cameraFacingMode: CameraFacingMode,
    flashMode: FlashMode,
    captureMode: CaptureMode,
    capturePhoto: Boolean,
    startRecording: Boolean,
    onCaptureComplete: (String) -> Unit,
    onRecordingComplete: (String) -> Unit,
    resolution:IntSize
) {




    var view by remember{ mutableStateOf(UIView().apply { this.backgroundColor = redColor }) }
    var shouldRecompose by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val cameraManager = remember {
        CameraManager(
//            cameraFacingMode = cameraFacingMode,
//            flashMode = flashMode,
//            captureMode = captureMode,
         //   view = view,
            onUpdatedView = {
//
//                view = it
//               // view = UIView().apply { this.backgroundColor = greenColor }
//                shouldRecompose = !shouldRecompose
              //  println("the updated ui view bg color is ${updatedView?.backgroundColor}")
            }
        )
    }

    LaunchedEffect(captureMode){
        cameraManager.setCaptureMode(captureMode)
    }

    LaunchedEffect(flashMode){
        cameraManager.setFlashMode(flashMode)
    }

    LaunchedEffect(cameraFacingMode){
        cameraManager.setCameraFacingMode(cameraFacingMode)
    }

    LaunchedEffect(capturePhoto){
        if (capturePhoto) {
            cameraManager.capturePhoto(onCaptureComplete)
        }
    }

    LaunchedEffect(startRecording){
        if (startRecording) {
            cameraManager.startRecording(onRecordingComplete)
        } else {
            cameraManager.stopRecording()
        }
    }


//    DisposableEffect(Unit) {
//        onDispose {
//            println("on dispose called")
//
//            cameraManager.captureSession?.stopRunning()
//            cameraManager.captureSession = null
//            cameraManager.previewLayer?.removeFromSuperlayer()
//        }
//    }

    LaunchedEffect(true){
//        try {
//            cameraManager.startSession()
//            println("Is session running: ${cameraManager.captureSession?.isRunning()}")
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

  //  key(shouldRecompose){
       // println("the view is updated and recomposing preview")
        UIKitViewController(
            modifier = modifier.fillMaxSize().background(Color.White),
            factory = {
                println("called from factory the view is updated and recomposing preview")
              //  println("called from factory the view bounds is ${view.frame.useContents { this.size.width }}  ${view.frame.useContents { this.size.height }}")
                cameraManager
//                view.apply {
//                    // Force layout updates for testing
//                    setNeedsLayout()
//                    layoutIfNeeded()
//                }

            },
//            update = {
////                // Update the view's layout when necessary
//                it.setNeedsLayout()
//                it.layoutIfNeeded()
//            },

        )
   // }



//    onStartCapture = { cameraManager.capturePhoto(onCaptureComplete) }
//    onStartRecording = { isRecording ->
//        if (isRecording) {
//            cameraManager.stopRecording(onRecordingComplete)
//        } else {
//            cameraManager.startRecording(onRecordingComplete)
//        }
//    }


}


class CameraManager(
  //  private val view: UIView,
    private val onUpdatedView: (UIView)->Unit
) : UIViewController(null,null){
    private var captureSession: AVCaptureSession? = null
    private var photoOutput: AVCapturePhotoOutput? = null
    private var movieFileOutput: AVCaptureMovieFileOutput? = null
    private var previewLayer: AVCaptureVideoPreviewLayer? = null
    private var isRecording = false
    private var currentDevice: AVCaptureDevice? = null
    private var cameraFacingMode: CameraFacingMode = CameraFacingMode.FRONT
    private var flashMode: FlashMode = FlashMode.OFF // make flashMode mutable
    private var captureMode: CaptureMode = CaptureMode.PHOTO
    private var recordingDelegate: RecordingDelegate? = null
   // private var metadataOutput = AVCaptureMetadataOutput()

//    private var audioDevice: AVCaptureDevice? = null


    override fun viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = blackColor
        startSession()
    }


    fun setCameraFacingMode(mode: CameraFacingMode){
        cameraFacingMode = mode
    }

    fun setFlashMode(mode: FlashMode){
        updateFlashMode(mode)
    }

    fun setCaptureMode(mode: CaptureMode){
        captureMode = mode
    }

    @OptIn(ExperimentalForeignApi::class)
    fun startSession() {
       // val view = UIView()
        captureSession = AVCaptureSession().apply {
            //sessionPreset = AVCaptureSessionPresetPhoto

            // Select camera based on facing mode
            // Select camera based on facing mode
            currentDevice = AVCaptureDevice.devicesWithMediaType(AVMediaTypeVideo)
                .filterIsInstance<AVCaptureDevice>()
                .firstOrNull { device ->
                    device.position == when (cameraFacingMode) {
                        CameraFacingMode.FRONT -> AVCaptureDevicePositionFront
                        CameraFacingMode.BACK -> AVCaptureDevicePositionBack
                    }
                }


            println("the devices is ${currentDevice != null}")

            currentDevice?.let { device ->
                val deviceInput = AVCaptureDeviceInput(device, null)
                if (canAddInput(deviceInput)) addInput(deviceInput)
                // Apply the initial flash mode
                applyFlashMode(device)
            }

//            val deviceOutput = AVCaptureVideoDataOutput()
//            if(this.canAddOutput(deviceOutput)){
//                this.addOutput(deviceOutput)
//            }

            // Setup output for photo or video capture
            if (captureMode == CaptureMode.PHOTO) {
                photoOutput = AVCapturePhotoOutput().apply {
                    if (canAddOutput(this)) addOutput(this)
                }
            } else {
                movieFileOutput = AVCaptureMovieFileOutput().apply {
                    if (canAddOutput(this)) addOutput(this)
                }

            }


//            view.setNeedsLayout()
//            view.layoutIfNeeded()
//            view.backgroundColor = blueColor
            println("the view bounds is ${UIScreen.mainScreen.bounds.useContents { this.size.width }}  ${UIScreen.mainScreen.bounds.useContents { this.size.height }}")

            previewLayer = AVCaptureVideoPreviewLayer(session = this).apply {
                videoGravity = AVLayerVideoGravityResizeAspectFill
                setFrame(view.bounds)
                connection?.videoOrientation = currentVideoOrientation()
            }

            println("the view bounds is ${UIScreen.mainScreen.bounds.useContents { this.size.width }}  ${UIScreen.mainScreen.bounds.useContents { this.size.height }}")

            view.layer.addSublayer(previewLayer!!)
         //   onUpdatedView(view)

            // Start running the capture session on a background thread
            dispatch_async(
                dispatch_get_global_queue(
                    DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(),
                    0u
                )
            ) {
                println("Starting capture session on background thread...")
                captureSession?.startRunning()
            }
        }
    }

    fun currentVideoOrientation(): AVCaptureVideoOrientation {
        val orientation = UIDevice.currentDevice.orientation
        return when (orientation) {
            UIDeviceOrientation.UIDeviceOrientationPortrait -> AVCaptureVideoOrientationPortrait
            UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown -> AVCaptureVideoOrientationPortraitUpsideDown
            UIDeviceOrientation.UIDeviceOrientationLandscapeLeft -> AVCaptureVideoOrientationLandscapeRight
            UIDeviceOrientation.UIDeviceOrientationLandscapeRight -> AVCaptureVideoOrientationLandscapeLeft
            else -> AVCaptureVideoOrientationPortrait
        }
    }

    // Start video recording function
    fun startRecording(onRecordingComplete: (String) -> Unit) {
        println("started recording")

        val outputUrl = getTempFileUrl("video_${Clock.System.now().toEpochMilliseconds()}.mp4")

        recordingDelegate = RecordingDelegate(onRecordingComplete = { path ->
            println("completed recording $outputUrl")
            onRecordingComplete(path)
        })

        if (outputUrl != null) {

            movieFileOutput = AVCaptureMovieFileOutput().apply {
                if (captureSession!!.canAddOutput(this)) captureSession!!.addOutput(this)
            }


            movieFileOutput!!.startRecordingToOutputFileURL(outputUrl,recordingDelegate!!)
            isRecording = true
            println("started recording $outputUrl")

        }
    }

    // Stop video recording function
    fun stopRecording() {
        if (isRecording) {
            println("stopping recording")
            movieFileOutput?.stopRecording()
            isRecording = false
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setupInputs(): Boolean {
        val availableDevices = AVCaptureDeviceDiscoverySession.discoverySessionWithDeviceTypes(
            listOf(AVCaptureDeviceTypeBuiltInWideAngleCamera),
            AVMediaTypeVideo,
            AVCaptureDevicePositionUnspecified
        ).devices

        if (availableDevices.isEmpty()) return false

        for (device in availableDevices) {
            when ((device as AVCaptureDevice).position) {
                AVCaptureDevicePositionBack -> currentDevice = device
                AVCaptureDevicePositionFront -> currentDevice = device
            }
        }

       // currentCamera = backCamera ?: frontCamera ?: return false

        try {
            val input = AVCaptureDeviceInput.deviceInputWithDevice(
                currentDevice!!,
                null
            ) ?: return false

            if (captureSession?.canAddInput(input) == true) {
                captureSession?.addInput(input)
                return true
            }
        } catch (e: Exception) {
            throw CameraException.ConfigurationError(e.message ?: "Unknown error")
        }
        return false
    }



    // Function to apply the flash mode to the current device
    @OptIn(ExperimentalForeignApi::class)
    private fun applyFlashMode(device: AVCaptureDevice) {
        if (device.hasTorch && device.isTorchAvailable()) {
            try {
                device.lockForConfiguration(outError = null)
                device.torchMode = when (flashMode) {
                    FlashMode.ON -> AVCaptureTorchModeOn
                    FlashMode.OFF -> AVCaptureTorchModeOff

                }
                device.unlockForConfiguration()
            } catch (e: Exception) {
                println("Error setting flash mode: ${e.printStackTrace()}")
            }
        }
    }

    // Method to update flash mode dynamically
    private fun updateFlashMode(newFlashMode: FlashMode) {
        flashMode = newFlashMode
        currentDevice?.let { applyFlashMode(it) }
    }

    // Capture photo function with flash mode set in photo settings
    fun capturePhoto(onCaptureComplete: (String) -> Unit) {
        println("started capturing")

        val photoSettings = AVCapturePhotoSettings()

        // Set flash mode for capturing photos (for front/back photos)
        photoSettings.flashMode = when (flashMode) {
            FlashMode.ON -> AVCaptureFlashModeOn
            FlashMode.OFF -> AVCaptureFlashModeOff
         //   FlashMode.AUTO -> AVCaptureFlashModeAuto
        }

        photoOutput?.capturePhotoWithSettings(photoSettings, object : AVCapturePhotoCaptureDelegateProtocol, NSObject() {
            override fun captureOutput(
                output: AVCapturePhotoOutput,
                didFinishProcessingPhoto: AVCapturePhoto,
                error: NSError?
            ) {
                error?.let {
                    println("Error capturing photo: ${error.localizedDescription}")
                    return
                }

                val imageData = didFinishProcessingPhoto.fileDataRepresentation()
                val imagePath = savePhotoToCache(imageData!!)
                onCaptureComplete(imagePath)
                println("captured photo path $imagePath")

            }
        })
    }


    private fun savePhotoToCache(imageData: NSData): String {
        val fileManager = NSFileManager.defaultManager
        val fileName = "photo_${NSUUID.UUID().UUIDString}.jpg"
        val fileUrl = fileManager.temporaryDirectory.URLByAppendingPathComponent(fileName)

        if (fileUrl?.path == null) throw Exception("Failed to create file url")
        fileManager.createFileAtPath(fileUrl?.path!!, imageData, null)
        return fileUrl.path!!
    }

    private fun getTempFileUrl(fileName: String): NSURL? {
        val tempDir = NSFileManager.defaultManager.temporaryDirectory
        return tempDir.URLByAppendingPathComponent(fileName)
    }
}






class RecordingDelegate(
    private val onRecordingComplete: (String) -> Unit
) : AVCaptureFileOutputRecordingDelegateProtocol , NSObject(){

//    override fun fileOutput(
//        output: AVCaptureMovieFileOutput,
//        didFinishRecordingToOutputFileAtURL: NS-URL,
//        fromConnections: List<*> // You can specify the type if needed
//    ) {
//        // Call the completion handler with the output URL path
//        onRecordingComplete(didFinishRecordingToOutputFileAtURL.path!!)
//    }

    override fun captureOutput(
        output: AVCaptureFileOutput,
        didFinishRecordingToOutputFileAtURL: NSURL,
        fromConnections: List<*>,
        error: NSError?
    ) {
        println("the capture output is done ${didFinishRecordingToOutputFileAtURL.path}")

        onRecordingComplete(didFinishRecordingToOutputFileAtURL.path ?: "")
    }

    // You can implement other delegate methods as needed
}



