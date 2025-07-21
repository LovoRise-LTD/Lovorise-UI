package com.lovorise.app.libs.scheduler

import coil3.PlatformContext

expect fun scheduleTokenRefreshTask(context: PlatformContext?)

const val REFRESH_TASK_NAME = "TokenRefreshWork"
const val TASK_IDENTIFIER = "com.lovorise.refreshToken"
const val REFRESH_TIME_IN_HOUR = 6