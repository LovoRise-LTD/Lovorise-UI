package com.lovorise.app.accounts.presentation.utils

import cafe.adriel.voyager.navigator.Navigator
import com.lovorise.app.accounts.presentation.onboarding.OnboardingScreen
import io.ktor.util.reflect.instanceOf


fun Navigator.navigateToOnBoarding(){

    this.apply {
        if (items.contains(OnboardingScreen())){
            popUntil { it.instanceOf(OnboardingScreen::class)}
        }else {
            popAll()
            push(OnboardingScreen())
            push(OnboardingScreen())
            pop()
        }
    }

}