package com.lovorise.app.libs.permissions

class RequestCanceledException(
    val permission: Permission,
    message: String? = null
) : Exception(message)
