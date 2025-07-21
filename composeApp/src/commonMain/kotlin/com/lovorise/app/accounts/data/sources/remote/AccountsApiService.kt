package com.lovorise.app.accounts.data.sources.remote

import com.lovorise.app.accounts.data.sources.remote.dto.AddVerifyRecoveryEmailRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.AddVerifyRecoveryEmailResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.AppSettingsDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.BlockedUsersResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.ChangeEmailRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.ChangeEmailResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.ChangePasswordRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.ControlMyViewDto
import com.lovorise.app.accounts.data.sources.remote.dto.ForgotPasswordDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.ForgotPasswordResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.GetMediasResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.HelpSupportRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.NotificationSettingsDto
import com.lovorise.app.accounts.data.sources.remote.dto.OAuthLoginRequest
import com.lovorise.app.accounts.data.sources.remote.dto.ReorderMediaRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.SignInDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.SignedMediaUrlRequest
import com.lovorise.app.accounts.data.sources.remote.dto.SignedMediaUrlResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.SignupData
import com.lovorise.app.accounts.data.sources.remote.dto.TokenResponse
import com.lovorise.app.accounts.data.sources.remote.dto.UserResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.VerifyEmail
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.libs.iap.AppleReceiptData
import com.lovorise.app.libs.iap.GoogleReceiptData

interface AccountsApiService {

    suspend fun signup(data: SignupData):Pair<Boolean,TokenResponse?>
    suspend fun signup(data: SignupData,token:String):Boolean
    suspend fun verify(data:VerifyEmail,token: String):Boolean
    suspend fun getPresignedUrl(data:SignedMediaUrlRequest, token: String):List<SignedMediaUrlResponseDto?>?
    suspend fun uploadImage(url:String,filePath:String,type: SignedUrlMediaItem.Type) : Boolean
    suspend fun forgotPassword(data: ForgotPasswordDataDto):ForgotPasswordResponseDto?
    suspend fun signin(data: SignInDataDto):TokenResponse?
    suspend fun googleLogin(data:OAuthLoginRequest):TokenResponse?
    suspend fun appleLogin(data:OAuthLoginRequest):TokenResponse?
    suspend fun saveSignupProgress(screen:String,token:String): Boolean
    suspend fun deleteAccount(token:String,reasons:List<String>): Boolean
    suspend fun deleteAccountPasswordVerification(token:String,password:String): Boolean
    suspend fun resendOtp(token: String): Boolean
    suspend fun pauseAccount(token: String): Boolean
    suspend fun resumeAccount(token: String): Boolean
    suspend fun getUser(token: String) : UserResponseDto?
    suspend fun deleteImage(token: String,mediaId:String) : Boolean
    suspend fun getImages(token: String): GetMediasResponseDto?
    suspend fun insertImage(token: String,mediaId:String,index:Int) : String?
    suspend fun reorderImage(token: String,data: ReorderMediaRequestDto) : Boolean
    suspend fun isPasswordCreated(token: String) : Boolean
    suspend fun changePassword(token: String,data: ChangePasswordRequestDto) : Boolean
    suspend fun changeEmail(token:String,data:ChangeEmailRequestDto) : ChangeEmailResponseDto?
    suspend fun verifyAddRecoveryEmail(token: String,data:AddVerifyRecoveryEmailRequestDto) : AddVerifyRecoveryEmailResponseDto?
    suspend fun getBlockedUsers(token: String) : BlockedUsersResponseDto?
    suspend fun blockUser(token: String,userId:String) : Boolean
    suspend fun unblockUser(token: String,userId:String) : Boolean

    suspend fun getAppLanguage(token: String) : String?
    suspend fun setAppLanguage(token: String, language:String) : Boolean
    suspend fun sendSupportRequest(token: String, data: HelpSupportRequestDto) : Boolean

    suspend fun getAnonymousStatus(token: String) : String?
    suspend fun setAnonymousMode(token: String) : Boolean
    suspend fun removeAnonymousMode(token: String) : Boolean

    suspend fun readReceiptDisabled(token: String) : Boolean
    suspend fun readReceiptEnabled(token: String) : Boolean
    suspend fun updateControlProfile(token: String,data: ControlMyViewDto) : Boolean
    suspend fun updateNotificationSettings(token: String,data: NotificationSettingsDto) : Boolean
    suspend fun getAppSettings(token: String) : AppSettingsDataDto?

    suspend fun verifyApplePurchase(token: String, data: AppleReceiptData): Boolean
    suspend fun verifyGooglePurchase(token: String, data: GoogleReceiptData): Boolean
    suspend fun createTravelTicket(token: String, data: AppSettingsDataDto.TravelTicketStatusDto): Boolean

    suspend fun getFileSizeFromUrl(url:String) : Long?

    suspend fun checkEmailStatus(email:String) : TokenResponse?

    suspend fun getAllProfiles(token: String):List<UserResponseDto>

    suspend fun getProfileDetails(token: String,id:String) : UserResponseDto?
}