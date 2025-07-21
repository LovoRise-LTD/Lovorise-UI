package com.lovorise.app.accounts.domain

import com.lovorise.app.accounts.domain.model.AddVerifyRecoveryEmailRequest
import com.lovorise.app.accounts.domain.model.AppSettingsData
import com.lovorise.app.accounts.domain.model.BlockedUser
import com.lovorise.app.accounts.domain.model.ChangeEmailRequest
import com.lovorise.app.accounts.domain.model.ChangeEmailResponse
import com.lovorise.app.accounts.domain.model.ChangePasswordRequest
import com.lovorise.app.accounts.domain.model.ForgotPasswordData
import com.lovorise.app.accounts.domain.model.ForgotPasswordResponse
import com.lovorise.app.accounts.domain.model.GetMediaResponse
import com.lovorise.app.accounts.domain.model.HelpSupportRequestData
import com.lovorise.app.accounts.domain.model.OAuthLoginResponse
import com.lovorise.app.accounts.domain.model.ReorderImageItem
import com.lovorise.app.accounts.domain.model.SignedImageUrlResponse
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.accounts.domain.model.SignupWithEmailResponse
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.Tokens
import com.lovorise.app.libs.iap.AppleReceiptData
import com.lovorise.app.libs.iap.GoogleReceiptData
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface AccountsRepo {

    suspend fun signup(email:String):Flow<Resource<SignupWithEmailResponse>>
    suspend fun getEmailStatus(email: String) : Flow<Resource<SignupWithEmailResponse>>
    suspend fun verifyEmail(email: String,otp:String,token:String):Flow<Resource<Boolean>>
    suspend fun createPasswordSignupFlow(password: String, token:String):Flow<Resource<Boolean>>
    suspend fun addNameSignupFlow(name: String, token:String):Flow<Resource<Boolean>>
    suspend fun addAgeSignupFlow(age: Int, birthDate:String, token:String):Flow<Resource<Boolean>>
    suspend fun addGenderSignupFlow(gender: String, moreInfo:String, token:String):Flow<Resource<Boolean>>
    suspend fun addWhoDoYouLikeToMeetSignupFlow(genders: List<String>, token:String):Flow<Resource<Boolean>>
    suspend fun addDatingPrefsSignupFlow(prefs: List<String>, token:String):Flow<Resource<Boolean>>
    suspend fun addInterestsSignupFlow(interests: Map<String,List<String>>, token:String):Flow<Resource<Boolean>>
    suspend fun addHeightSignupFlow(height: Double, token:String):Flow<Resource<Boolean>>
    suspend fun addEducationLevelSignupFlow(education: String, token:String):Flow<Resource<Boolean>>
    suspend fun addFamilyPlanSignupFlow(family: String, token:String):Flow<Resource<Boolean>>
    suspend fun addDrinkPrefSignupFlow(drink: String, token:String):Flow<Resource<Boolean>>
    suspend fun addSmokePrefSignupFlow(smoke: String, token:String):Flow<Resource<Boolean>>
    suspend fun addReligionPrefSignupFlow(religion: String, token:String):Flow<Resource<Boolean>>
    suspend fun addLanguagesSignupFlow(languages: List<String>, token:String):Flow<Resource<Boolean>>
    suspend fun getPresignedUrl(files:List<SignedUrlMediaItem>,token: String):List<SignedImageUrlResponse>
    suspend fun uploadImage(url:String,filePath:String,type: SignedUrlMediaItem.Type) : Boolean
    suspend fun confirmUploadedImages(hashes:List<String>,token: String,inviteCode:String?): Flow<Resource<Boolean>>
    suspend fun forgotPassword(data: ForgotPasswordData): Flow<Resource<ForgotPasswordResponse>>
    suspend fun signin(email:String,password:String): Flow<Resource<Tokens>>
    suspend fun loginWithGoogle(token: String): Flow<Resource<OAuthLoginResponse>>
    suspend fun loginWithApple(token: String): Flow<Resource<OAuthLoginResponse>>
    suspend fun saveSignupProgress(screen: SignupFlowPages, token:String): Flow<Resource<Boolean>>
    suspend fun deleteAccount(token:String,reasons:List<String>): Flow<Resource<Boolean>>
    suspend fun deleteAccountPasswordVerification(token:String,password:String): Flow<Resource<Boolean>>
    suspend fun pauseAccount(token:String): Flow<Resource<Boolean>>
    suspend fun resumeAccount(token:String): Flow<Resource<Boolean>>
    suspend fun resendOtp(token: String): Flow<Resource<Boolean>>
    suspend fun getImages(token:String): Flow<Resource<GetMediaResponse>>
    suspend fun deleteImage(token: String,mediaId:String): Flow<Resource<Boolean>>
    suspend fun insertImage(token: String,mediaId: String,index:Int): String?
    suspend fun getUser(token: String): Flow<Resource<UserResponse>>
    suspend fun reorderImage(token: String, data: List<ReorderImageItem>): Flow<Resource<Boolean>>
    suspend fun passwordStatus(token: String): Flow<Resource<Boolean>>
    suspend fun changeEmail(token: String,data:ChangeEmailRequest) : Flow<Resource<ChangeEmailResponse>>
    suspend fun changePassword(token: String,data:ChangePasswordRequest) : Flow<Resource<Boolean>>
    suspend fun verifyAddRecoveryEmail(token:String,data:AddVerifyRecoveryEmailRequest) : Flow<Resource<Boolean>>
    suspend fun getBlockedUsers(token:String) : Flow<Resource<List<BlockedUser>>>
    suspend fun blockUser(token:String,id:String) : Flow<Resource<Boolean>>
    suspend fun unblockUser(token:String,id:String) : Flow<Resource<Boolean>>

    suspend fun getAppLanguage(token: String) : Flow<Resource<String>>
    suspend fun setAppLanguage(token: String, language:String) : Flow<Resource<Boolean>>
    suspend fun sendSupportRequest(token: String, data: HelpSupportRequestData) : Flow<Resource<Boolean>>

    suspend fun getAnonymousStatus(token: String) : Flow<Resource<LocalDate>>
    suspend fun setAnonymousMode(token: String) : Flow<Resource<LocalDate>>
    suspend fun removeAnonymousMode(token: String) : Flow<Resource<Boolean>>

    suspend fun updateReadReceipt(token: String,value:Boolean) : Flow<Resource<Boolean>>
    suspend fun updateControlProfile(token: String,data:AppSettingsData.ControlProfile) : Flow<Resource<Boolean>>
    suspend fun updateNotificationSettings(token: String,data:AppSettingsData.NotificationSettings) : Flow<Resource<Boolean>>
    suspend fun getAppSettings(token: String) : Flow<Resource<AppSettingsData>>
    suspend fun getFileSizeFromUrl(url: String): Long?
    suspend fun verifyApplePurchase(token: String, data: AppleReceiptData): Boolean
    suspend fun verifyGooglePurchase(token: String, data: GoogleReceiptData): Boolean

    suspend fun createTravelTicket(token: String,data:AppSettingsData.TravelTicketStatus) : Boolean
}