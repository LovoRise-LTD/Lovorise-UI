package com.lovorise.app.di

import com.lovorise.app.profile.data.repo.ProfileRepoImpl
import com.lovorise.app.profile.data.source.remote.UserProfileApiService
import com.lovorise.app.profile.data.source.remote.UserProfileApiServiceImplementation
import com.lovorise.app.profile.domain.repo.ProfileRepo
import org.koin.dsl.module

val profileModule = module {

    single<UserProfileApiService>{
        UserProfileApiServiceImplementation(get())
    }

    single<ProfileRepo>{
        ProfileRepoImpl(get())
    }

}