package com.lovorise.app.di

import com.lovorise.app.lovorise_hearts.data.remote.PurchaseApiService
import com.lovorise.app.lovorise_hearts.data.remote.PurchaseApiServiceImpl
import com.lovorise.app.lovorise_hearts.data.repo.PurchaseRepoImpl
import com.lovorise.app.lovorise_hearts.domain.PurchaseRepo
import io.ktor.client.HttpClient
import org.koin.dsl.module

val purchaseModule = module{
    single<PurchaseApiService>{
        PurchaseApiServiceImpl(get<HttpClient>())
    }

    single<PurchaseRepo> {
        PurchaseRepoImpl(get<PurchaseApiService>())
    }
}