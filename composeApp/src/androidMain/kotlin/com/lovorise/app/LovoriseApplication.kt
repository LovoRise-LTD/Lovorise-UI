package com.lovorise.app

import android.app.Application
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.lovorise.app.di.initializeKoin
import org.koin.core.component.KoinComponent

class LovoriseApplication : Application() , KoinComponent{

    override fun onCreate() {
        super.onCreate()
        initializeKoin()
        FirebaseApp.initializeApp(this)
    }
}

/*
actual fun getImagesFromDevice(context: PlatformContext): Flow<List<String>> = callbackFlow {
    val images = mutableListOf<String>()
    val fetchOptions = PHFetchOptions().apply {
        sortDescriptors = listOf(NSSortDescriptor("creationDate", false))
    }

    val assets = PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeImage, fetchOptions)
    val imageManager = PHImageManager.defaultManager()
    val imageRequestOptions = PHImageRequestOptions()

    val assetCount = assets.count.toInt()
    val batchSize = 50 // Number of assets to process in each batch
    val concurrentCoroutines = 10 // Limit the number of concurrent coroutines
    val delayMillis = 100L // Delay between batches to prevent overwhelming the system

    // Semaphore to limit the number of concurrent coroutines
    val semaphore = Semaphore(concurrentCoroutines)

    suspend fun processBatch(startIndex: Int, endIndex: Int) {
        val jobs = mutableListOf<Job>()

        for (index in startIndex until endIndex) {
            val asset = assets.objectAtIndex(index.toULong()) as PHAsset

            // Acquire a permit
            semaphore.acquire()

            val job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    imageManager.requestImageDataForAsset(asset, imageRequestOptions) { data, _, _, _ ->
                        if (data != null) {
                            val resources = PHAssetResource.assetResourcesForAsset(asset)
                            val resource = resources.firstOrNull() as? PHAssetResource
                            val fileName = resource?.originalFilename ?: "temp_image_$index.png"

                            val localUrl = if (fileName.lowercase().endsWith("png") || fileName.lowercase().endsWith("jpg") || fileName.lowercase().endsWith("jpeg")) {
                                getLocalUrlForAssetStandardImages(asset, fileName = fileName, result = {
                                    it?.let {
                                        images.add(it)
                                        trySend(images.toList())
                                    }
                                })
                            } else {
                                getLocalUrlForAssetNonStandardImage(asset, fileName = fileName, result = {
                                    it?.let {
                                        images.add(it)
                                        trySend(images.toList())
                                    }
                                })
                            }

                        }
                    }
                } finally {
                    // Release the permit
                    semaphore.release()
                }
            }
            jobs.add(job)
        }

        // Wait for all coroutines in the batch to complete
        jobs.forEach { it.join() }
    }

    // Process assets in batches
    for (i in 0 until assetCount step batchSize) {
        val endIndex = min(i + batchSize, assetCount)
        processBatch(i, endIndex)

        // Introduce a delay between batches to reduce system load
        delay(delayMillis)
    }

    awaitClose()
}
* */