package com.lovorise.app.libs.camera


import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceDiscoverySession
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDevicePositionFront
import platform.AVFoundation.AVCaptureDevicePositionUnspecified
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVCaptureFlashMode
import platform.AVFoundation.AVCaptureFlashModeAuto
import platform.AVFoundation.AVCaptureInput
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureTorchMode
import platform.AVFoundation.AVCaptureTorchModeAuto
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
import platform.AVFoundation.position
import platform.AVFoundation.torchMode
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.UIKit.UIView
import platform.darwin.NSObject


class CustomCameraController : NSObject() , AVCapturePhotoCaptureDelegateProtocol{
    var captureSession: AVCaptureSession? = null
    private var backCamera: AVCaptureDevice? = null
    private var frontCamera: AVCaptureDevice? = null
    private var currentCamera: AVCaptureDevice? = null
    private var photoOutput: AVCapturePhotoOutput? = null
    var cameraPreviewLayer: AVCaptureVideoPreviewLayer? = null
    private var isUsingFrontCamera = false


    var onPhotoCapture: ((NSData?) -> Unit)? = null
    var onError: ((CameraException) -> Unit)? = null

    var flashMode: AVCaptureFlashMode = AVCaptureFlashModeAuto
    var torchMode: AVCaptureTorchMode = AVCaptureTorchModeAuto

    sealed class CameraException : Exception() {
        class DeviceNotAvailable : CameraException()
        class ConfigurationError(message: String) : CameraException()
        class CaptureError(message: String) : CameraException()
    }

    fun setupSession() {
        try {
            captureSession = AVCaptureSession()
            captureSession?.beginConfiguration()

            if (!setupInputs()) {
                throw CameraException.DeviceNotAvailable()
            }

            setupPhotoOutput()
            captureSession?.commitConfiguration()
        } catch (e: CameraException) {
            cleanupSession()
            onError?.invoke(e)
        }
    }

    private fun setupPhotoOutput() {
        photoOutput = AVCapturePhotoOutput()
      //  photoOutput?.setHighResolutionCaptureEnabled(true)
        if (captureSession?.canAddOutput(photoOutput!!) == true) {
            captureSession?.addOutput(photoOutput!!)
        } else {
            throw CameraException.ConfigurationError("Cannot add photo output")
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
                AVCaptureDevicePositionBack -> backCamera = device
                AVCaptureDevicePositionFront -> frontCamera = device
            }
        }

        currentCamera = backCamera ?: frontCamera ?: return false

        try {
            val input = AVCaptureDeviceInput.deviceInputWithDevice(
                currentCamera!!,
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

    suspend fun startSession() {

       // CoroutineScope(Dispatchers.Default).launch{
            withContext(Dispatchers.Default){
                captureSession?.startRunning()
            }
        println("Is session running1 : ${captureSession?.isRunning()}")

        //  }
//        if (captureSession?.isRunning() == false) {
//            dispatch_async(
//                dispatch_get_global_queue(
//                    DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(),
//                    0u
//                )
//            ) {
//                captureSession?.startRunning()
//            }
//        }
    }

    fun stopSession() {
        if (captureSession?.isRunning() == true) {
            println("stopping the capture session")
            captureSession?.stopRunning()
        }
    }

    fun cleanupSession() {
        stopSession()
        cameraPreviewLayer?.removeFromSuperlayer()
        cameraPreviewLayer = null
        captureSession = null
        photoOutput = null
        currentCamera = null
        backCamera = null
        frontCamera = null
    }


    @OptIn(ExperimentalForeignApi::class)
    fun setupPreviewLayer(view: UIView) {
        captureSession?.let { session ->
            val newPreviewLayer = AVCaptureVideoPreviewLayer(session = session).apply {
                videoGravity = AVLayerVideoGravityResizeAspectFill
                setFrame(view.bounds)

                connection?.videoOrientation = currentVideoOrientation()
            }

            view.layer.addSublayer(newPreviewLayer)
            cameraPreviewLayer = newPreviewLayer
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

    fun setFlashMode(mode: AVCaptureFlashMode) {
        flashMode = mode
    }

    @OptIn(ExperimentalForeignApi::class)
    fun setTorchMode(mode: AVCaptureTorchMode) {
        torchMode = mode
        currentCamera?.let { camera ->
            if (camera.hasTorch) {
                try {
                    camera.lockForConfiguration(null)
                    camera.torchMode = mode
                    camera.unlockForConfiguration()
                } catch (e: Exception) {
                    onError?.invoke(CameraException.ConfigurationError("Failed to set torch mode"))
                }
            }
        }
    }

    fun captureImage() {
        println("started capturing")

        println("Started capturing... photoOutput: $photoOutput & captureSession?.isRunning() : ${captureSession?.isRunning()}")

        if (photoOutput == null) {
            println("photoOutput is null, capture cannot proceed!")
            return
        }

        if (captureSession?.isRunning() == false) {
            println("Capture session is not running, starting now...")
            captureSession?.startRunning()
        }

        val settings = AVCapturePhotoSettings()
        settings.flashMode = flashMode
         //settings.setHighResolutionPhotoEnabled(true)
        photoOutput?.capturePhotoWithSettings(settings, delegate = this)

        /*
        override fun captureOutput(
                output: AVCapturePhotoOutput,
                didFinishProcessingPhoto: AVCapturePhoto,
                error: NSError?
            ) {
                if (error != null) {
                    println("the error while capturing is ${error.localizedDescription}")
                    onError?.invoke(CameraException.CaptureError(error.localizedDescription))
                    return
                }

                val imageData = didFinishProcessingPhoto.fileDataRepresentation()
                println("the image data is $imageData")
                onPhotoCapture?.invoke(imageData)
            }

            override fun captureOutput(
                output: AVCapturePhotoOutput,
                didFinishCapturingDeferredPhotoProxy: AVCaptureDeferredPhotoProxy?,
                error: NSError?
            ) {
                println("the output is $output")
                println("the error is $error")
            }
        * */

        println("the photoOutput is $photoOutput")
    }

    @OptIn(ExperimentalForeignApi::class)
    fun switchCamera() {
        guard(captureSession != null) { return@guard }

        captureSession?.beginConfiguration()

        try {

            captureSession?.inputs?.firstOrNull()?.let { input ->
                captureSession?.removeInput(input as AVCaptureInput)
            }


            isUsingFrontCamera = !isUsingFrontCamera
            currentCamera = if (isUsingFrontCamera) frontCamera else backCamera


            val newCamera = currentCamera ?: throw CameraException.DeviceNotAvailable()


            val newInput = AVCaptureDeviceInput.deviceInputWithDevice(
                newCamera,
                null
            ) ?: throw CameraException.ConfigurationError("Failed to create input")

            if (captureSession?.canAddInput(newInput) == true) {
                captureSession?.addInput(newInput)
            } else {
                throw CameraException.ConfigurationError("Cannot add input")
            }


            cameraPreviewLayer?.connection?.let { connection ->
                if (connection.isVideoMirroringSupported()) {
                    connection.automaticallyAdjustsVideoMirroring = false
                    connection.setVideoMirrored(isUsingFrontCamera)
                }
            }

            captureSession?.commitConfiguration()
        } catch (e: CameraException) {
            captureSession?.commitConfiguration()
            onError?.invoke(e)
        } catch (e: Exception) {
            captureSession?.commitConfiguration()
            onError?.invoke(CameraException.ConfigurationError(e.message ?: "Unknown error"))
        }
    }

    override fun captureOutput(
        output: AVCapturePhotoOutput,
        didFinishProcessingPhoto: AVCapturePhoto,
        error: NSError?
    ) {
        println("the output is invoked!!")
        if (error != null) {
            onError?.invoke(CameraException.CaptureError(error.localizedDescription))
            return
        }

        val imageData = didFinishProcessingPhoto.fileDataRepresentation()
        println("the image data is $imageData")
        onPhotoCapture?.invoke(imageData)
    }

    private inline fun guard(condition: Boolean, crossinline block: () -> Unit) {
        if (!condition) block()
    }
}


/*
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
//            view = view,
            onCapturedImage = {
                onCaptureComplete(it)
            },
            onUpdatedView = {

                view = it
               // view = UIView().apply { this.backgroundColor = greenColor }
                shouldRecompose = !shouldRecompose
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
            cameraManager.capturePhoto()
        }
    }

    LaunchedEffect(startRecording){
        if (startRecording) {
            cameraManager.startRecording(onRecordingComplete)
        } else {
            cameraManager.stopRecording()
        }
    }


    DisposableEffect(Unit) {
        coroutineScope.launch {
            try {
                cameraManager.startSession()
                println("Is session running: ${cameraManager.captureSession?.isRunning()}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        onDispose {
            println("on dispose called")
            cameraManager.captureSession?.stopRunning()
            cameraManager.captureSession = null
            cameraManager.previewLayer?.removeFromSuperlayer()
        }
    }
//
//    key(shouldRecompose){
//        println("the view is updated and recomposing preview")
//
////        UIKitViewController(
////            factory = { cameraController },
////            modifier = modifier,
////        )
//
//    }

    UIKitViewController(
        modifier = modifier.fillMaxSize().background(Color.White),
        factory = {
            println("called from factory the view is updated and recomposing preview")
            println("called from factory the view bounds is ${view.frame.useContents { this.size.width }}  ${view.frame.useContents { this.size.height }}")
//
//                view.apply {
//                    // Force layout updates for testing
//                    setNeedsLayout()
//                    layoutIfNeeded()
//                }
            cameraManager

        },
        properties = UIKitInteropProperties()
//            update = {
////                // Update the view's layout when necessary
//                it.setNeedsLayout()
//                it.layoutIfNeeded()
//            },

    )



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
    private val onUpdatedView: (UIView)->Unit,
    private val onCapturedImage:(String)->Unit
) : UIViewController(null,null)
{

    private val customCameraController = CustomCameraController()
    var captureSession: AVCaptureSession? = null
    private var photoOutput: AVCapturePhotoOutput? = null
    private var movieFileOutput: AVCaptureMovieFileOutput? = null
    var previewLayer: AVCaptureVideoPreviewLayer? = null
    private var isRecording = false
    private var currentDevice: AVCaptureDevice? = null
    private var cameraFacingMode: CameraFacingMode = CameraFacingMode.FRONT
    private var flashMode: FlashMode = FlashMode.OFF // make flashMode mutable
    private var captureMode: CaptureMode = CaptureMode.PHOTO
    private var recordingDelegate: RecordingDelegate? = null
    private var metadataOutput = AVCaptureMetadataOutput()

//    private var audioDevice: AVCaptureDevice? = null


    override fun viewDidLoad() {
        super.viewDidLoad()
       // setupCamera()
//        startSession()
    }

    suspend fun setupCamera() {
        customCameraController.setupSession()
        customCameraController.setupPreviewLayer(view)


        if (customCameraController.captureSession?.canAddOutput(metadataOutput) == true) {
            customCameraController.captureSession?.addOutput(metadataOutput)
        }
            //  startSession()
        customCameraController.startSession()
        customCameraController.onPhotoCapture = { image ->
            println("the capture completed ")
            image?.let {
                val imagePath = savePhotoToCache(it)
                onCapturedImage(imagePath)
                println("the capturedPath = $imagePath")
//                val data = it.toByteArray()
//                imageCaptureListeners.forEach { it(data) }
            }
        }

        customCameraController.onError = { error ->
            println("Camera Error: $error")
        }

    }

    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        customCameraController.cameraPreviewLayer?.setFrame(view.bounds)
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
    suspend fun startSession() {
      //  val view = UIView()
        captureSession = AVCaptureSession().apply {
//            beginConfiguration()
            sessionPreset = AVCaptureSessionPresetPhoto

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


            view.setNeedsLayout()
            view.layoutIfNeeded()
            view.backgroundColor = blueColor
            println("the view bounds is ${UIScreen.mainScreen.bounds.useContents { this.size.width }}  ${UIScreen.mainScreen.bounds.useContents { this.size.height }}")
            previewLayer = AVCaptureVideoPreviewLayer().apply {
                session = captureSession
                videoGravity = AVLayerVideoGravityResizeAspectFill
                frame = view.bounds
                backgroundColor = grayColor.CGColor
            }
            println("the view bounds is ${UIScreen.mainScreen.bounds.useContents { this.size.width }}  ${UIScreen.mainScreen.bounds.useContents { this.size.height }}")

            view.layer.addSublayer(previewLayer!!)
            onUpdatedView(view)

            // Start running the capture session on a background thread
            withContext(Dispatchers.Default) {
                startRunning()
                println("started ")
                // delay(3000L)
                onUpdatedView(view)
            }
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
    fun capturePhoto() {

        customCameraController.captureImage()

//        val photoSettings = AVCapturePhotoSettings()
//
//        // Set flash mode for capturing photos (for front/back photos)
//        photoSettings.flashMode = when (flashMode) {
//            FlashMode.ON -> AVCaptureFlashModeOn
//            FlashMode.OFF -> AVCaptureFlashModeOff
//         //   FlashMode.AUTO -> AVCaptureFlashModeAuto
//        }
//
//        photoOutput?.capturePhotoWithSettings(photoSettings, object : AVCapturePhotoCaptureDelegateProtocol, NSObject() {
//            override fun captureOutput(
//                output: AVCapturePhotoOutput,
//                didFinishProcessingPhoto: AVCapturePhoto,
//                error: NSError?
//            ) {
//                error?.let {
//                    println("Error capturing photo: ${error.localizedDescription}")
//                    return
//                }
//
//                val imageData = didFinishProcessingPhoto.fileDataRepresentation()
//                val imagePath = savePhotoToCache(imageData!!)
//                onCaptureComplete(imagePath)
//                println("captured photo path $imagePath")
//
//            }
//        })
    }


    private fun savePhotoToCache(imageData: NSData): String {
        val fileManager = NSFileManager.defaultManager
        val fileName = "photo_${NSUUID.UUID().UUIDString}.jpg"
        val fileUrl = fileManager.temporaryDirectory.URLByAppendingPathComponent(fileName)

        fileManager.createFileAtPath(fileUrl?.path!!, imageData, null)
        return fileUrl.path!!
    }

    private fun getTempFileUrl(fileName: String): NSURL? {
        val tempDir = NSFileManager.defaultManager.temporaryDirectory
        return tempDir.URLByAppendingPathComponent(fileName)
    }
}

* */
