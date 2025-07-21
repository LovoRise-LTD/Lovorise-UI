package com.lovorise.app.di

import com.lovorise.app.swipe.data.repo.SwipeRepoImpl
import com.lovorise.app.swipe.data.source.remote.SwipeApiService
import com.lovorise.app.swipe.data.source.remote.SwipeApiServiceImpl
import com.lovorise.app.swipe.domain.repo.SwipeRepo
import io.ktor.client.HttpClient
import org.koin.dsl.module

val swipeModule = module{
    single<SwipeApiService>{
        SwipeApiServiceImpl(get<HttpClient>())
    }

    single<SwipeRepo> {
        SwipeRepoImpl(get<SwipeApiService>())
    }
}