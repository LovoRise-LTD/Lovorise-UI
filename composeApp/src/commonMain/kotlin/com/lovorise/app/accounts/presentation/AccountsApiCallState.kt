package com.lovorise.app.accounts.presentation

import com.lovorise.app.accounts.domain.model.AppSettingsData
import com.lovorise.app.accounts.domain.model.ForgotPasswordResponse
import com.lovorise.app.accounts.domain.model.OAuthLoginResponse
import com.lovorise.app.accounts.domain.model.SignedImageUrlResponse
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.libs.location.LocationData

data class AccountsApiCallState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val success:Boolean = false,
    val presignedUrls:List<SignedImageUrlResponse> = emptyList(),
    val uploadedHashes:List<String> = emptyList(),
    val forgotPasswordResponse: ForgotPasswordResponse? = null,
    val showExitConfirmationDialog:Boolean = false,
    val oAuthLoginResponse: OAuthLoginResponse? = null,
    val testCode:String? = null,
    val currentEmail:String?=null,
    val nextScreen:String? = null,
    val user:UserResponse? = null,
    val isUploadConfirmed:Boolean = false,
    val currentLocation:LocationData? = null,
    val totalImagesLength:Int = 0,
    val isPasswordCreated:Boolean? = null,
    val changeEmailToken:String? = null,
    val language:String? = null,
    val appSettingsData: AppSettingsData? = null,
    val isSettingsApiCallMade:Boolean = false,
    val deletedAccountName:String? = null,
    val lastLoginType:LastLoginType? = null
    //val images:GetImagesResponse? = null
)

enum class LastLoginType{
    EMAIL,GOOGLE,APPLE
}
