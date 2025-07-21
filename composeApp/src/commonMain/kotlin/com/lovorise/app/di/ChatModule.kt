package com.lovorise.app.di

import com.lovorise.app.accounts.data.sources.remote.AccountsApiService
import com.lovorise.app.chat.data.ChatRepoImpl
import com.lovorise.app.chat.data.sources.remote.ChatApiService
import com.lovorise.app.chat.data.sources.remote.DefaultChatApiServiceImpl
import com.lovorise.app.chat.data.sources.remote.WebSocketClient
import com.lovorise.app.chat.domain.ChatRepo
import org.koin.dsl.module

val chatModule = module {

    single<WebSocketClient>{
        WebSocketClient()
    }

    single<ChatApiService> {
        DefaultChatApiServiceImpl(get(),get())
    }

    single<ChatRepo> {
        ChatRepoImpl(chatApiService = get(), accountsApiService = get<AccountsApiService>())
    }
}