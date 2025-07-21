package com.lovorise.app.libs.copy_share

import coil3.PlatformContext

expect fun copyToClipboard(text: String,context: PlatformContext)

expect fun shareText(text: String,context:PlatformContext)