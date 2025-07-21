package com.lovorise.app.di

import com.lovorise.app.accounts.data.repo.AccountsRepoImpl
import com.lovorise.app.accounts.data.sources.remote.AccountsApiService
import com.lovorise.app.accounts.data.sources.remote.AccountsApiServiceImpl
import com.lovorise.app.accounts.domain.AccountsRepo
import org.koin.dsl.module


val accountsModule = module {

    single<AccountsApiService>{
        AccountsApiServiceImpl(get())
    }

    single<AccountsRepo>{
        AccountsRepoImpl(get())
    }
}