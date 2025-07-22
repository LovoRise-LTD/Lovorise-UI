package com.lovorise.app.di

import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.gender.GenderScreenModel
import com.lovorise.app.accounts.presentation.signup.profile_upload.ProfileUploadScreenViewModel
import com.lovorise.app.chat.presentation.ChatScreenModel
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.invite.InviteScreenModel
import com.lovorise.app.libs.connectivity.ConnectivityViewModel
import com.lovorise.app.lovorise_hearts.presentation.PurchaseScreenModel
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenModel
import com.lovorise.app.profile.presentation.verification.IDProfileVerificationScreenModel
import com.lovorise.app.reels.presentation.viewModels.CaptureRecordScreenModel
import com.lovorise.app.reels.presentation.viewModels.ReelsScreenModel
import com.lovorise.app.settings.presentation.screens.privacy_and_security.PrivacyAndSecurityScreenModel
import com.lovorise.app.settings.presentation.screens.travel_ticket.TravelTicketScreenModel
import com.lovorise.app.swipe.presentation.SwipeScreenModel
import com.lovorise.app.ui.ThemeViewModel
import org.koin.dsl.module

val viewModelsModule = module {
//    viewModelOf(::GenderScreenViewModel)
   // factory { GenderScreenViewModel() }
//    viewModelOf(::AccountsViewModel)

    single { AccountsViewModel(get()) }
    factory { ReelsScreenModel(get()) }
    factory { GenderScreenModel() }
    factory { ChatScreenModel(get()) }
    factory { CaptureRecordScreenModel() }
    factory { TabsScreenModel(get()) }
    factory { ProfileUploadScreenViewModel(get()) }
    single { EditProfileScreenModel(get(),get()) }
    factory { IDProfileVerificationScreenModel() }
    factory { ProfileScreenModel(get()) }
    factory { SwipeScreenModel(get()) }
    factory { PurchaseScreenModel(get()) }
    factory { TravelTicketScreenModel(get()) }
    factory { InviteScreenModel(get()) }
    factory { PrivacyAndSecurityScreenModel() }
    single { ConnectivityViewModel() }
    single { ThemeViewModel() }

}