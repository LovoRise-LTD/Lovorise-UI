package com.lovorise.app.di

import com.lovorise.app.reels.data.repo.ReelsRepoImpl
import com.lovorise.app.reels.data.source.remote.DefaultReelsApiService
import com.lovorise.app.reels.data.source.remote.ReelsApiService
import com.lovorise.app.reels.domain.repo.ReelsRepo
import io.ktor.client.HttpClient
import org.koin.dsl.module

val reelsModule = module {

    single<ReelsApiService>{
        DefaultReelsApiService(get<HttpClient>())
    }

    single<ReelsRepo> {
        ReelsRepoImpl(get<ReelsApiService>())
    }
}