package com.lovorise.app.accounts.data.sources.remote

import com.lovorise.app.accounts.data.sources.remote.dto.AddVerifyRecoveryEmailRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.AddVerifyRecoveryEmailResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.AnonymousStatusResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.AppSettingsDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.AppleReceiptDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.BlockActionRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.BlockedUsersResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.ChangeEmailRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.ChangeEmailResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.ChangePasswordRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.ControlMyViewDto
import com.lovorise.app.accounts.data.sources.remote.dto.DeleteAccountRequest
import com.lovorise.app.accounts.data.sources.remote.dto.DeleteMediaRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.ForgotPasswordDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.ForgotPasswordResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.GetLanguagesResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.GetMediasResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.GoogleReceiptDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.HelpSupportRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.InsertMediaRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.InsertMediaResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.NotificationSettingsDto
import com.lovorise.app.accounts.data.sources.remote.dto.OAuthLoginRequest
import com.lovorise.app.accounts.data.sources.remote.dto.PasswordResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.ReorderMediaRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.SetLanguagesRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.SignInDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.SignUpFlowPageRequest
import com.lovorise.app.accounts.data.sources.remote.dto.SignedMediaUrlRequest
import com.lovorise.app.accounts.data.sources.remote.dto.SignedMediaUrlResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.SignupData
import com.lovorise.app.accounts.data.sources.remote.dto.TokenResponse
import com.lovorise.app.accounts.data.sources.remote.dto.UserResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.VerifyEmail
import com.lovorise.app.accounts.data.sources.remote.dto.VerifyPasswordDelete
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.bodyFromFile
import com.lovorise.app.chat.data.sources.remote.DefaultChatApiServiceImpl.Companion.GET_PROFILES
import com.lovorise.app.libs.iap.AppleReceiptData
import com.lovorise.app.libs.iap.GoogleReceiptData
import com.lovorise.app.util.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AccountsApiServiceImpl(
    private val httpClient: HttpClient
) : AccountsApiService {

    override suspend fun getFileSizeFromUrl(url: String): Long? {
        return try {
            val response = httpClient.request(url) {
                method = HttpMethod.Head
            }
            response.headers[HttpHeaders.ContentLength]?.toLongOrNull()
        }catch (e:Exception){
            null
        }
    }

    override suspend fun signup(data: SignupData): Pair<Boolean,TokenResponse?> {
        val response = try {
            val httpRes = httpClient.post(SIGNUP_URL){
                header("Content-Type","application/json")
                setBody(data)
            }
            Pair(httpRes.status.value in 200..300,httpRes.body() as TokenResponse?)
        }catch (e:Exception){
            Pair(false,null)
        }
        return response
    }

    override suspend fun verifyAddRecoveryEmail(token: String, data: AddVerifyRecoveryEmailRequestDto): AddVerifyRecoveryEmailResponseDto? {
        val response : AddVerifyRecoveryEmailResponseDto? = try {
            httpClient.post(VERIFY_THIRD_PARTY_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
                setBody(data)
            }.body()
        } catch (e: Exception) {
            null
        }
        return response
    }

    override suspend fun getBlockedUsers(token: String): BlockedUsersResponseDto? {
        val response : BlockedUsersResponseDto? = try {
            httpClient.get(GET_BLOCKED_USERS_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            null
        }
        return response
    }

    override suspend fun blockUser(token: String, userId: String): Boolean {
        val response = try {
            httpClient.post(BLOCK_USER_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
                setBody(BlockActionRequestDto(blockedAuthID = userId))
            }
        } catch (e: Exception) {
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun unblockUser(token: String, userId: String): Boolean {
        val response = try {
            httpClient.delete(BLOCK_USER_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
                setBody(BlockActionRequestDto(blockedAuthID = userId))
            }
        } catch (e: Exception) {
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun deleteAccount(token: String, reasons: List<String>): Boolean {
        val response = try {
            httpClient.delete(DELETE_USER_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
                setBody(DeleteAccountRequest(reasons = reasons, page = 1))
            }
        } catch (e: Exception) {
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun deleteAccountPasswordVerification(token: String, password: String): Boolean {
        val response = try {
            httpClient.delete(DELETE_USER_URL){
            header("Content-Type","application/json")
            header("Authorization", "Bearer $token")
            setBody(VerifyPasswordDelete(password = password, page = 2).apply { println(Json.encodeToString(this)) })
        }}catch (e:Exception){
            println(e.message)
            null
        }

        return response?.status?.value in 200..300
    }

    override suspend fun pauseAccount(token: String): Boolean {
        val response = try {
            httpClient.post(PAUSE_USER_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }
        } catch (e: Exception) {
            null
        }

        return response?.status?.value in 200..300
    }

    override suspend fun readReceiptDisabled(token: String): Boolean {
        val response = try {
            httpClient.delete(READ_RECEIPT_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }
        } catch (e: Exception) {
            null
        }

        return response?.status?.value in 200..300
    }

    override suspend fun readReceiptEnabled(token: String): Boolean {
        val response = try {
            httpClient.post(READ_RECEIPT_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }
        } catch (e: Exception) {
            null
        }

        return response?.status?.value in 200..300
    }

    override suspend fun updateControlProfile(token: String, data: ControlMyViewDto): Boolean {
        val response = try{httpClient.post(CONTROL_MY_VIEW_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }} catch (e: Exception) {
            null
        }


        return response?.status?.value in 200..300
    }

    override suspend fun updateNotificationSettings(token: String, data: NotificationSettingsDto): Boolean {
        val response = try { httpClient.post(NOTIFICATION_SETTINGS_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        } }catch (e: Exception) {
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun getAppSettings(token: String): AppSettingsDataDto? {
        val response:AppSettingsDataDto? = try {
            httpClient.get(APP_SETTINGS_URL){
                header("Content-Type","application/json")
                header("Authorization", "Bearer $token")
            }.body()}catch (e:Exception){
            null
        }
        return response
    }

    override suspend fun resumeAccount(token: String): Boolean {
        val response = try{httpClient.post(RESUME_USER_URL){
            header("Content-Type","application/json")
            header("Authorization", "Bearer $token")
        }}catch (e:Exception){
        null
    }

        return response?.status?.value in 200..300
    }

    override suspend fun getPresignedUrl(data: SignedMediaUrlRequest, token: String): List<SignedMediaUrlResponseDto?>? {
        val response:List<SignedMediaUrlResponseDto?>? = try {
            httpClient.post(MEDIA_UPLOAD_URL){
            header("Content-Type","application/json")
            header("Authorization", "Bearer $token")
            setBody(data)
        }.body()}catch (e:Exception){
            null
        }
        return response
    }

    override suspend fun signup(data: SignupData, token: String): Boolean {
        val success = try {
            val response = httpClient.post(SIGNUP_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
                setBody(data)
            }
            response.status.value in 200..300
        } catch (e: ClientRequestException) {
            false
        } catch (e: ServerResponseException) {
            false
        } catch (e: Exception) {
            false
        }

      //  println("the response = ${response.status} ${response} ${response.request.content}")

        return success
    }

    override suspend fun verify(data: VerifyEmail, token: String): Boolean {
        val response = try{httpClient.post(VERIFY_EMAIL_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }} catch (e: Exception) {
            null
        }


        return response?.status?.value in 200..300
    }

    override suspend fun forgotPassword(data: ForgotPasswordDataDto): ForgotPasswordResponseDto? {
        val response:ForgotPasswordResponseDto? = try{httpClient.post(FORGOT_PASSWORD_URL) {
            header("Content-Type","application/json")
            setBody(data)
        }.body()}catch (e:Exception){null}
        return response
    }

    override suspend fun googleLogin(data: OAuthLoginRequest): TokenResponse? {
        val response:TokenResponse? = try{httpClient.post(GOOGLE_LOGIN_URL) {
            header("Content-Type","application/json")
            setBody(data)
        }.body()}catch (e:Exception){null}
        return response
    }

    override suspend fun appleLogin(data: OAuthLoginRequest): TokenResponse? {
        val response:TokenResponse? = try{httpClient.post(APPLE_LOGIN_URL) {
            header("Content-Type","application/json")
            setBody(data)
        }.body()}catch (e:Exception){null}
        return response
    }

    override suspend fun signin(data: SignInDataDto): TokenResponse? {
        val response:TokenResponse? = try{httpClient.post(SIGNIN_URL) {
            header("Content-Type","application/json")
            setBody(data)
        }.body()}catch (e:Exception){null}
        return response
    }

    override suspend fun getAppLanguage(token: String): String? {
        val response: GetLanguagesResponseDto? = try{httpClient.get(APP_LANG_URL) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
        }.body()}catch (e:Exception){null}
        return response?.languages?.firstOrNull()
    }

    override suspend fun setAppLanguage(token: String, language: String): Boolean {
        val response = try{httpClient.post(APP_LANG_URL) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
            setBody(SetLanguagesRequestDto(listOf(language)))
        }}catch (e:Exception){null}
        return response?.status?.value in 200..300
    }

    override suspend fun sendSupportRequest(token: String, data: HelpSupportRequestDto): Boolean {
        val response = try{httpClient.post(APP_SUPPORT_URL) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
            setBody(data)
        }}catch (e:Exception){null}
        return response?.status?.value in 200..300
    }

    override suspend fun getAnonymousStatus(token: String): String? {
        val response: AnonymousStatusResponseDto? = try{httpClient.get(GET_ANONYMOUS_STATUS_URL) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
        }.body()}catch (e:Exception){null}
        return response?.expiresAt
    }

    override suspend fun setAnonymousMode(token: String): Boolean {
        val response = try{ httpClient.post(UPDATE_ANONYMOUS_STATUS_URL) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
        }}catch (e:Exception){null}
        return response?.status?.value in 200..300
    }


    override suspend fun verifyApplePurchase(token: String, data: AppleReceiptData): Boolean {
        val response = try{ httpClient.post(APPLE_VERIFY_PURCHASE_URL) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
            setBody(AppleReceiptDataDto(data))
        }}catch (e:Exception){null}
        return response?.status?.value in 200..300
    }

    override suspend fun createTravelTicket(token: String, data: AppSettingsDataDto.TravelTicketStatusDto): Boolean {

        val response = try{ httpClient.post(CREATE_TRAVEL_TICKET) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
            setBody(data)
        }}catch (e:Exception){null}
        return response?.status?.value in 200..300
    }

    override suspend fun verifyGooglePurchase(token: String, data: GoogleReceiptData): Boolean {
        val response = try{ httpClient.post(GOOGLE_VERIFY_PURCHASE_URL) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
            setBody(GoogleReceiptDataDto(data))
        }}catch (e:Exception){null}
        return response?.status?.value in 200..300
    }

    override suspend fun removeAnonymousMode(token: String): Boolean {
        val response = try{httpClient.delete(UPDATE_ANONYMOUS_STATUS_URL) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
        }}catch (e:Exception){null}
        return response?.status?.value in 200..300
    }

    override suspend fun uploadImage(url: String, filePath:String, type: SignedUrlMediaItem.Type): Boolean {

        val response = try{httpClient.put(url) {

            // Set the content type, for example, PNG or JPEG depending on the byte array's image type
            header(HttpHeaders.ContentType, if (type == SignedUrlMediaItem.Type.IMAGE) ContentType.Image.PNG else ContentType.Video.MP4)

            setBody(bodyFromFile(filePath.removePrefix("file://")))
        }}catch (e:Exception){null}

        return response?.status?.value in 200..300
    }

    override suspend fun getUser(token: String): UserResponseDto? {
        val response: UserResponseDto? = try {
            httpClient.get(USER_PROFILE_URL){
                header("Content-Type","application/json")
                header("Authorization","Bearer $token")
            }.body()
        }catch (e:ClientRequestException){
            if (e.response.status == HttpStatusCode.Unauthorized) {
                null
            }else{
//                UserResponseDto()
                throw e
            }
        }
        return response
    }

    override suspend fun isPasswordCreated(token: String): Boolean {
        val response:PasswordResponseDto? = try {
            httpClient.get(PASSWORD_STATUS_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }.body()}catch (e:Exception){
        null
    }
        return response?.isPasswordSet ?: false
    }

    override suspend fun changePassword(token: String, data: ChangePasswordRequestDto): Boolean {
        val response:PasswordResponseDto? = try{httpClient.post(PASSWORD_CHANGE_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }.body()}catch (e:Exception){
        null
    }
        return response?.isPasswordSet ?: false
    }

    override suspend fun changeEmail(token: String, data: ChangeEmailRequestDto): ChangeEmailResponseDto? {
        val response:ChangeEmailResponseDto? = try{httpClient.post(CHANGE_EMAIL_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }.body()}catch (e:Exception){
            null
        }
        return response
    }

    override suspend fun insertImage(token: String, mediaId: String, index:Int): String? {
        val response: InsertMediaResponseDto? = try {
            httpClient.post(MEDIA_BASE_URL){
                header("Content-Type","application/json")
                header("Authorization", "Bearer $token")
                setBody(InsertMediaRequestDto(mediaId,index))
            }.body()
        } catch (e:Exception){
            null
        }
        return response?.mediaUrl
    }

    override suspend fun reorderImage(token: String, data: ReorderMediaRequestDto): Boolean {
        val response = try {
            httpClient.post(UPDATE_IMAGE_ORDER_URL){
            header("Content-Type","application/json")
            header("Authorization", "Bearer $token")
            setBody(data)
        }}catch (e:Exception){
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun deleteImage(token: String,mediaId:String): Boolean {
        val response = try { httpClient.delete(MEDIA_BASE_URL){
            header("Content-Type","application/json")
            header("Authorization", "Bearer $token")
            setBody(DeleteMediaRequestDto(mediaId))
        }}catch (e:Exception){
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun getImages(token: String): GetMediasResponseDto? {
        val response: GetMediasResponseDto? = try{httpClient.get(GET_MEDIAS_URL){
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }.body()}catch (e:Exception){
        null
    }
        return response
    }

    override suspend fun saveSignupProgress(screen: String, token: String): Boolean {
        val response = try { httpClient.post(SAVE_SIGNUP_PAGE_URL){
            header("Content-Type","application/json")
            header("Authorization", "Bearer $token")
            setBody(SignUpFlowPageRequest(screen))
        }}catch (e:Exception){
            null
        }

        return response?.status?.value in 200..300
    }


    override suspend fun resendOtp(token: String): Boolean {
        val response = try{httpClient.get(RESEND_VERIFY_EMAIL_URL){
            header("Content-Type","application/json")
            header("Authorization", "Bearer $token")
        }}catch (e:Exception){
            null
        }

        return response?.status?.value in 200..300
    }

    override suspend fun checkEmailStatus(email: String): TokenResponse? {
        val response: TokenResponse? = try {
            httpClient.get(CHECK_EMAIL_STATUS_URL){
                header("Content-Type","application/json")
                parameter("email",email)
            }.body()
        } catch (e:Exception){
            null
        }
        return response
    }

    override suspend fun getAllProfiles(token: String): List<UserResponseDto> {
        val response: List<UserResponseDto>? = try{httpClient.get(GET_PROFILES) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }.body()}catch (e:Exception){null}
        return response ?: emptyList()
    }

    override suspend fun getProfileDetails(token: String,id: String): UserResponseDto? {
        val response: UserResponseDto? = try {
            httpClient.get("${USER_DETAILS}/$id"){
                header("Content-Type","application/json")
                header("Authorization","Bearer $token")
            }.body()
        } catch (e:Exception){
            null
        }
        return response
    }

    companion object{
        private const val SIGNUP_URL = "${AppConstants.BASE_AUTH_URL}/sign-up"
        private const val CHECK_EMAIL_STATUS_URL = "${AppConstants.BASE_AUTH_URL}/email-status"
        private const val SAVE_SIGNUP_PAGE_URL = "${AppConstants.BASE_AUTH_URL}/save-flow-page"
        private const val SIGNIN_URL = "${AppConstants.BASE_AUTH_URL}/sign-in"
        private const val FORGOT_PASSWORD_URL = "${AppConstants.BASE_AUTH_URL}/forgot-password"
        private const val MEDIA_BASE_URL = "${AppConstants.BASE_AUTH_URL}/media"
        private const val MEDIA_UPLOAD_URL = "$MEDIA_BASE_URL/signed-url"
        private const val UPDATE_IMAGE_ORDER_URL = "$MEDIA_BASE_URL/update-orders"
        private const val GET_MEDIAS_URL = "$MEDIA_BASE_URL/get"
        private const val VERIFY_URL = "${AppConstants.BASE_AUTH_URL}/verify"
        private const val RESEND_VERIFY_EMAIL_URL = "$VERIFY_URL/resend-email"
        private const val VERIFY_EMAIL_URL = "$VERIFY_URL/email"
        private const val GOOGLE_LOGIN_URL = "$VERIFY_URL/google"
        private const val APPLE_LOGIN_URL = "$VERIFY_URL/apple"
        private const val DELETE_USER_URL = "${AppConstants.BASE_AUTH_URL}/user"
        private const val APP_LANG_URL = "${AppConstants.BASE_AUTH_URL}/user/app/lang"
        private const val APP_SETTINGS_URL = "${AppConstants.BASE_AUTH_URL}/user/app/settings"
        private const val NOTIFICATION_SETTINGS_URL = "${AppConstants.BASE_AUTH_URL}/user/app/notifications"
        private const val APP_SUPPORT_URL = "${AppConstants.BASE_AUTH_URL}/user/app/support"
        private const val UPDATE_ANONYMOUS_STATUS_URL = "${AppConstants.BASE_AUTH_URL}/user/anonymous"
        private const val GET_ANONYMOUS_STATUS_URL = "${AppConstants.BASE_AUTH_URL}/user/anonymous-status"
        private const val PASSWORD_STATUS_URL = "${AppConstants.BASE_AUTH_URL}/user/password-status"
        private const val PASSWORD_CHANGE_URL = "${AppConstants.BASE_AUTH_URL}/user/password-change"
        private const val CHANGE_EMAIL_URL = "${AppConstants.BASE_AUTH_URL}/user/change-email"
        private const val USER_DETAILS = "${AppConstants.BASE_AUTH_URL}/swipe/user"

        private const val PAUSE_USER_URL = "${AppConstants.BASE_AUTH_URL}/user/pause"
        private const val READ_RECEIPT_URL = "${AppConstants.BASE_AUTH_URL}/user/read-receipts"
        private const val CONTROL_MY_VIEW_URL = "${AppConstants.BASE_AUTH_URL}/user/control-view"
        private const val RESUME_USER_URL = "${AppConstants.BASE_AUTH_URL}/user/resume"
        private const val USER_PROFILE_URL = "${AppConstants.BASE_AUTH_URL}/user/profile"
        private const val VERIFY_THIRD_PARTY_URL = "${AppConstants.BASE_AUTH_URL}/verify/thirty-party"
        private const val BLOCK_USER_URL = "${AppConstants.BASE_AUTH_URL}/block"
        private const val GET_BLOCKED_USERS_URL = "$BLOCK_USER_URL/get-all"
        private const val GOOGLE_VERIFY_PURCHASE_URL = "${AppConstants.BASE_PURCHASE_URL}/payments/google/verify"
        private const val APPLE_VERIFY_PURCHASE_URL = "${AppConstants.BASE_PURCHASE_URL}/payments/apple/verify"
        private const val CREATE_TRAVEL_TICKET = "${AppConstants.BASE_AUTH_URL}/user/travel-ticket"
    }
}