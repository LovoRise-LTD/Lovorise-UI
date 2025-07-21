package com.lovorise.app.libs.download_manager

import coil3.PlatformContext

expect suspend fun downloadFile(context: PlatformContext, url: String, fileName: String,fromNetwork:Boolean,onSuccess:()->Unit = {},onError:()->Unit = {})
