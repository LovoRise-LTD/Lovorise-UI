package com.lovorise.app.util

interface CacheUtil {
    fun cacheImage(image:Any,key:String)
    fun getCachedImage(key:String) : Any?
}