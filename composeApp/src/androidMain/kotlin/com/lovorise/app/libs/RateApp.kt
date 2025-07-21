package com.lovorise.app.libs

import android.app.Activity
import coil3.PlatformContext
import com.google.android.play.core.review.ReviewManagerFactory

actual fun rateApp(context: PlatformContext,onSuccess:()->Unit,onFailed:()->Unit){
    val manager = ReviewManagerFactory.create(context)

    val request = manager.requestReviewFlow()
    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // We got the ReviewInfo object
            val reviewInfo = task.result
            val flow = manager.launchReviewFlow(context as Activity,  reviewInfo)
            flow.addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    onSuccess()
                } else {
                    onFailed()
                }
            }
        } else {
            onFailed()
        }
    }




}