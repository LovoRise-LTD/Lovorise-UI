package com.lovorise.app.chat.data.sources.remote

import com.lovorise.app.chat.domain.model.SendMessage
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class WebSocketClient {

    private val client = HttpClient(CIO) {
        install(WebSockets){
            pingIntervalMillis = 20_000
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    private var sendChannel: SendChannel<SendMessage>? = null
    private var sessionJob: Job? = null

    fun connect(
        url: String,
        onMessage: (String) -> Unit,
        onError: (Throwable) -> Unit,
        onConnected:() ->Unit,
        onDisconnected:() ->Unit
    ): Job {
        val channel = Channel<SendMessage>(Channel.UNLIMITED)
        sendChannel = channel

        sessionJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                client.webSocket(urlString = url) {

                    onConnected()
                    // Sender coroutine
                    val sender = launch {
                        for (message in channel) {
                            send(Frame.Text(Json.encodeToString(message)))
                        }
                    }

                    // Receiver loop
                    try {
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                onMessage(frame.readText())
                            }
                        }
                    } finally {
                        sender.cancel()
                        onDisconnected()
                    }
                }
            } catch (e: Throwable) {
                onError(e)
                onDisconnected()
            }
        }

        return sessionJob!!
    }

    fun send(data: SendMessage) {
        sendChannel?.trySend(data)
    }

    fun close() {
        sendChannel?.close()
        sessionJob?.cancel()
        client.close()
    }
}
