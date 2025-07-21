package com.lovorise.app.libs.connectivity

import coil3.PlatformContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class ConnectivityManager actual constructor(context: PlatformContext): NetworkObserver{


//    private val pathMonitor = NWPathMonitor()
//    private val pathMonitorQueue = dispatch_queue_create("PathMonitorQueue", null)

    override val isConnected: Flow<Boolean>
        get() = callbackFlow {
//            val handler: (NWPath) -> Unit = { path ->
//                val connected = path.status == NWPathStatusSatisfied
//                trySend(connected)
//            }
//
//            pathMonitor.setPathUpdateHandler(handler)
//            pathMonitor.start(queue = pathMonitorQueue)
//
            awaitClose {
//                pathMonitor.cancel()
            }
        }
}
