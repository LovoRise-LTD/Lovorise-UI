package com.lovorise.app.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val mainModule = module {

    single <HttpClient>{
        HttpClient {
            install(ContentNegotiation){
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging){
                logger = DebugKtorLogger()
                level = LogLevel.HEADERS
            }
        }
    }
}

class DebugKtorLogger : Logger {
    override fun log(message: String) {
        println("Ktor: $message")
    }
}