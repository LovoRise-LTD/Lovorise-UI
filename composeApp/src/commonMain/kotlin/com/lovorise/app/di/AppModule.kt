package com.lovorise.app.di

import com.lovorise.app.getDatabaseModule
import org.koin.core.context.startKoin

fun appModule() = listOf(getDatabaseModule(),viewModelsModule, mainModule, accountsModule, profileModule, reelsModule, swipeModule, purchaseModule, chatModule)

fun initializeKoin() {
    startKoin {
        modules(appModule())
    }
}