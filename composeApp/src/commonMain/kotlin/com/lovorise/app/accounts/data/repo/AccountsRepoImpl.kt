package com.lovorise.app.accounts.data.repo

import com.lovorise.app.accounts.data.mapper.toControlProfile
import com.lovorise.app.accounts.data.mapper.toImagesResponse
import com.lovorise.app.accounts.data.mapper.toNotificationData
import com.lovorise.app.accounts.data.mapper.toUserResponse
import com.lovorise.app.accounts.data.sources.remote.AccountsApiService
import com.lovorise.app.accounts.data.sources.remote.dto.AddVerifyRecoveryEmailRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.AppSettingsDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.ChangeEmailRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.ChangePasswordRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.ControlMyViewDto
import com.lovorise.app.accounts.data.sources.remote.dto.ForgotPasswordDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.HelpSupportRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.NotificationSettingsDto
import com.lovorise.app.accounts.data.sources.remote.dto.OAuthLoginRequest
import com.lovorise.app.accounts.data.sources.remote.dto.ReorderMediaRequestDto
import com.lovorise.app.accounts.data.sources.remote.dto.SignInDataDto
import com.lovorise.app.accounts.data.sources.remote.dto.SignedMediaUrlRequest
import com.lovorise.app.accounts.data.sources.remote.dto.SignupData
import com.lovorise.app.accounts.data.sources.remote.dto.SignupPages
import com.lovorise.app.accounts.data.sources.remote.dto.VerifyEmail
import com.lovorise.app.accounts.domain.AccountsRepo
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
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class AccountsRepoImpl(
    private val accountsApiService: AccountsApiService
) : AccountsRepo {


    override suspend fun verifyApplePurchase(token: String, data: AppleReceiptData): Boolean {
        return accountsApiService.verifyApplePurchase(token,data)
    }

    override suspend fun verifyGooglePurchase(token: String, data: GoogleReceiptData): Boolean {
        return accountsApiService.verifyGooglePurchase(token,data)
    }

    override suspend fun createTravelTicket(token: String, data: AppSettingsData.TravelTicketStatus): Boolean {
        return accountsApiService.createTravelTicket(token, AppSettingsDataDto.TravelTicketStatusDto(
            enabled = true,
            destination = AppSettingsDataDto.TravelTicketStatusDto.DestinationDto(country = data.destination.country, city = data.destination.city, longitude = data.destination.longitude, latitude = data.destination.latitude),
            returnDate = data.returnDate,
            departDate = data.departDate
        ))
    }

    override suspend fun getFileSizeFromUrl(url: String): Long? {
        return accountsApiService.getFileSizeFromUrl(url)
    }

    override suspend fun saveSignupProgress(screen: SignupFlowPages, token: String): Flow<Resource<Boolean>> {
        return flow {
            println("set signup page response  $screen")
            //emit(Resource.Loading(true))
            val response  = accountsApiService.saveSignupProgress(screen.name,token)

            println("set signup page response  $response")
           // emit(Resource.Loading(false))
            emit(Resource.Success(response))
        }
    }

    override suspend fun updateReadReceipt(token: String, value: Boolean): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = if(value) accountsApiService.readReceiptEnabled(token) else accountsApiService.readReceiptDisabled(token)
            emit(Resource.Loading(false))
            if(response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Operation failed"))
            }
        }
    }

    override suspend fun updateControlProfile(token: String, data: AppSettingsData.ControlProfile): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.updateControlProfile(token,ControlMyViewDto(hideMyAge = data.hideAge, onlineStatus = data.onlineStatus, hideLocation = data.hideLocation))
            emit(Resource.Loading(false))
            if(response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Operation failed"))
            }
        }
    }

    override suspend fun updateNotificationSettings(token: String, data: AppSettingsData.NotificationSettings): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.updateNotificationSettings(token,NotificationSettingsDto(messages = data.messages, likes = data.likes, secretCrush = data.secretCrush, matches = data.matches, reels = data.reels, visitors = data.visitors, offerAndPromotion = data.offerAndPromotions))
            emit(Resource.Loading(false))
            if(response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Operation failed"))
            }
        }
    }

    override suspend fun getAppSettings(token: String): Flow<Resource<AppSettingsData>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.getAppSettings(token)
            emit(Resource.Loading(false))
            val travelTicketStatus = response?.travelTicketStatus
            val data = AppSettingsData(
                language = response?.userAppLanguage?.languages?.firstOrNull() ?: "English",
                anonymousModeExpiry = if ((response?.anonymousStatus?.expiresAt ?: "").isBlank()) null else LocalDate.parse(response?.anonymousStatus?.expiresAt!!),
                notification = response?.notifications?.toNotificationData() ?: AppSettingsData. NotificationSettings(messages = true, likes = true, matches = true, visitors = true, secretCrush = true, reels = true, offerAndPromotions = true),
                controlProfile = response?.controlMyView?.toControlProfile() ?: AppSettingsData.ControlProfile(hideAge = false, onlineStatus = false,hideLocation = false),
                readReceipt = response?.readReceiptStatus?.enabled ?: false,
                travelTicketStatus = if (travelTicketStatus?.enabled != true || travelTicketStatus.destination == null) null else AppSettingsData.TravelTicketStatus(
                    enabled = travelTicketStatus.enabled,
                    destination = AppSettingsData.TravelTicketStatus.Destination(
                        country = travelTicketStatus.destination.country ?: "",
                        city = travelTicketStatus.destination.city ?: "",
                        longitude = travelTicketStatus.destination.longitude ?: 0.0,
                        latitude = travelTicketStatus.destination.latitude ?: 0.0
                    ),
                    departDate = travelTicketStatus.departDate ?: "",
                    returnDate = travelTicketStatus.returnDate ?: ""
                )

            )
            emit(Resource.Success(data))
        }
    }

    override suspend fun deleteAccount(token: String, reasons: List<String>): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.deleteAccount(token,reasons)
            emit(Resource.Loading(false))
            if(response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Operation failed"))
            }
        }
    }

    override suspend fun deleteAccountPasswordVerification(token: String, password: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.deleteAccountPasswordVerification(token=token,password=password)
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Incorrect password"))
            }
        }
    }

    override suspend fun passwordStatus(token: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.isPasswordCreated(token)
            emit(Resource.Loading(false))
            emit(Resource.Success(response))
        }
    }

    override suspend fun getBlockedUsers(token: String): Flow<Resource<List<BlockedUser>>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.getBlockedUsers(token)
            emit(Resource.Loading(false))
            emit(Resource.Success(response?.blockedUsers?.filter { !it?.blockedAuthID.isNullOrBlank() && !it?.name.isNullOrBlank() && !it?.imageUrl.isNullOrBlank()}?.map { BlockedUser(name = it?.name!!, id = it.blockedAuthID!!, imageUrl = it.imageUrl!!) } ?: emptyList()))
        }
    }

    override suspend fun blockUser(token: String, id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.blockUser(token, userId = id)
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Error("Something went wrong"))
            }else{
                emit(Resource.Success(true))
            }
        }
    }

    override suspend fun unblockUser(token: String, id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.unblockUser(token, userId = id)
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Error("Something went wrong"))
            }else{
                emit(Resource.Success(true))
            }
        }
    }

    override suspend fun changeEmail(token: String, data: ChangeEmailRequest): Flow<Resource<ChangeEmailResponse>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.changeEmail(token, ChangeEmailRequestDto(page = data.page, changeToken = data.changeToken, verifier = data.verifier, email = data.email, code = data.code))
            emit(Resource.Loading(false))
            if (response?.error == true){
                emit(Resource.Error(response.message ?: "Something went wrong"))
            }else{
                emit(Resource.Success(ChangeEmailResponse(changeToken = response?.changeToken, message = null)))
            }
        }
    }

    override suspend fun verifyAddRecoveryEmail(token: String, data: AddVerifyRecoveryEmailRequest): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.verifyAddRecoveryEmail(token, AddVerifyRecoveryEmailRequestDto(page = data.page, email = data.email, code = data.code))
            emit(Resource.Loading(false))
            if (response?.error != true){
                emit(Resource.Success(true))
            }else{
                emit(Resource.Error(response.message ?: "Some error occurred"))
            }
        }
    }

    override suspend fun changePassword(token: String, data: ChangePasswordRequest): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.changePassword(token, ChangePasswordRequestDto(password = data.password, newPassword = data.newPassword, confirmPassword = data.newPassword))
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Incorrect password"))
            }
        }
    }

    override suspend fun pauseAccount(token: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.pauseAccount(token)
            emit(Resource.Loading(false))
            if(response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Operation failed"))
            }
        }
    }

    override suspend fun resumeAccount(token: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.resumeAccount(token)
            emit(Resource.Loading(false))
            if(response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Operation failed"))
            }
        }
    }

    override suspend fun getImages(token: String): Flow<Resource<GetMediaResponse>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.getImages(token)
            emit(Resource.Loading(false))
            if (response != null) {
                emit(Resource.Success(response.toImagesResponse()))
            }else{
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun deleteImage(token: String, mediaId:String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.deleteImage(token,mediaId)
            emit(Resource.Loading(false))
            if (response) {
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun insertImage(token: String, mediaId: String,index:Int): String? {
        return accountsApiService.insertImage(token,mediaId,index)
    }

    override suspend fun reorderImage(token: String, data: List<ReorderImageItem>): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.reorderImage(token, ReorderMediaRequestDto(medias = data.map { ReorderMediaRequestDto.MediaItem(mediaId = it.mediaId, orderNum = it.orderNum) }))
            emit(Resource.Loading(false))
            if (response) {
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun getUser(token: String): Flow<Resource<UserResponse>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.getUser(token)
            emit(Resource.Loading(false))
            if (response?.user != null) {
                emit(Resource.Success(response.toUserResponse()))
            }else{
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun resendOtp(token: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response  = accountsApiService.resendOtp(token)
            println("the response for resend otp is $response")
            emit(Resource.Loading(false))
            if(response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Operation failed"))
            }
        }
    }

    override suspend fun getEmailStatus(email: String): Flow<Resource<SignupWithEmailResponse>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.checkEmailStatus(email)
            val token = response?.token
            emit(Resource.Loading(false))
            val nextStatus = response?.nextStatus
            if (!nextStatus.isNullOrBlank()){
                emit(Resource.Success(SignupWithEmailResponse(token = token, nextScreen = nextStatus, refreshToken = response.refreshToken)))
            }else{
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun signup(email: String): Flow<Resource<SignupWithEmailResponse>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.signup(SignupData(signup = SignupData.Signup(page = SignupPages.EMAIL.pageNumber, email = email)))
            val token = response.second?.token
            emit(Resource.Loading(false))
            val nextStatus = response.second?.nextStatus ?: "VERIFY_EMAIL_SCREEN"
            if (!response.first) {
                emit(Resource.Error("Something went wrong"))
            }else{
                //emit(Resource.Success(SignupWithEmailResponse(token,nextStatus)))
                emit(Resource.Success(SignupWithEmailResponse(token,nextStatus, refreshToken = response.second?.refreshToken)))
            }
        }

    }

    override suspend fun loginWithGoogle(token: String): Flow<Resource<OAuthLoginResponse>> {
        return flow {
            emit(Resource.Loading(true))
            val response  = accountsApiService.googleLogin(OAuthLoginRequest(token))
            println("the google login response is $response")
            val accessToken = response?.token
            val refreshToken = response?.refreshToken
            val isRegistrationCompleted = response?.registrationCompleted
            emit(Resource.Loading(false))
            if (accessToken == null || isRegistrationCompleted == null || refreshToken == null){
                emit(Resource.Error(response?.message ?: "Invalid"))
            }else {
                emit(Resource.Success(OAuthLoginResponse(accessToken,isRegistrationCompleted,response.nextStatus, refreshToken = refreshToken)))
            }
        }
    }

    override suspend fun loginWithApple(token: String): Flow<Resource<OAuthLoginResponse>> {
        return flow {
            emit(Resource.Loading(true))
            val response  = accountsApiService.appleLogin(OAuthLoginRequest(token))
            println("the apple login response is $response")
            val accessToken = response?.token
            val refreshToken = response?.refreshToken
            val isRegistrationCompleted = response?.registrationCompleted
            emit(Resource.Loading(false))
            if (accessToken == null || isRegistrationCompleted == null || refreshToken == null){
                emit(Resource.Error(response?.message ?: "Invalid"))
            }else {
                emit(Resource.Success(OAuthLoginResponse(accessToken,isRegistrationCompleted,response.nextStatus, refreshToken = refreshToken)))
            }
        }
    }

    override suspend fun forgotPassword(data: ForgotPasswordData): Flow<Resource<ForgotPasswordResponse>> {
        return flow {
            emit(Resource.Loading(true))
            val result = accountsApiService.forgotPassword(ForgotPasswordDataDto(page = data.page, password = data.password, resetToken = data.resetToken, code = data.code, email = data.email))
            emit(Resource.Loading(false))
            if (result?.error == true){
                emit(Resource.Error(result.message ?: "Invalid"))
            }else {
                emit(Resource.Success(ForgotPasswordResponse(resetToken = result?.resetToken, token = result?.token, redirectToRegistration = result?.redirectToRegistration, nextStatus = result?.nextStatus)))
            }
        }
    }

    override suspend fun signin(email: String, password: String): Flow<Resource<Tokens>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.signin(SignInDataDto(signin = SignInDataDto.SignIn(email,password)))
            val token = response?.accessToken
            val refreshToken = response?.refreshToken
            emit(Resource.Loading(false))
            if (token == null) {
                emit(Resource.Error("Enter valid email address or password"))
            }else{
                emit(Resource.Success(Tokens(token,refreshToken)))
            }
        }
    }

    override suspend fun getPresignedUrl(files:List<SignedUrlMediaItem>, token: String): List<SignedImageUrlResponse> {
        val response = accountsApiService.getPresignedUrl(SignedMediaUrlRequest(files = files.map { SignedMediaUrlRequest.FileData(it.fileName,it.type.name.lowercase()) }),token)
        return response?.mapNotNull {
            if (it?.presignUrl != null && it.mediaId != null) SignedImageUrlResponse(
                presignUrl = it.presignUrl,
                hash = it.mediaId
            ) else null
        } ?: emptyList()
    }

    override suspend fun uploadImage(url: String,filePath:String,type: SignedUrlMediaItem.Type): Boolean {
        return accountsApiService.uploadImage(url=url, filePath = filePath, type = type)
    }

    override suspend fun createPasswordSignupFlow(password: String, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(password = password, page = SignupPages.PASSWORD.pageNumber)),token)
    }

    override suspend fun confirmUploadedImages(hashes: List<String>,token: String,inviteCode:String?): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(page = SignupPages.IMAGE_UPLOAD_CONFIRMATION.pageNumber, mediaIds = hashes, inviteCode = inviteCode)),token)
    }

    override suspend fun addNameSignupFlow(name: String, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(page = SignupPages.NAME.pageNumber,name = name)),token)
    }

    override suspend fun addAgeSignupFlow(age: Int, birthDate:String, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(age = age, birthDate = birthDate, page = SignupPages.AGE.pageNumber)),token)
    }

    override suspend fun addGenderSignupFlow(gender: String, moreInfo:String, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(gender = gender, anotherGender = moreInfo, page = SignupPages.GENDER.pageNumber)),token)
    }

    override suspend fun addWhoDoYouLikeToMeetSignupFlow(genders: List<String>, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(whoWouldYouLikeToMeet = genders, page = SignupPages.WHO_WOULD_YOU_LIKE_TO_MEET.pageNumber)),token)
    }

    override suspend fun addDatingPrefsSignupFlow(prefs: List<String>, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(page = SignupPages.DATING_PREFS.pageNumber, typeOfRelation = prefs)),token)
    }

    override suspend fun addInterestsSignupFlow(interests: Map<String, List<String>>, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(page = SignupPages.INTEREST.pageNumber, interest = interests)),token)
    }

    override suspend fun addHeightSignupFlow(height: Double, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(page = SignupPages.HEIGHT.pageNumber, height = height)),token)
    }

    override suspend fun addEducationLevelSignupFlow(education: String, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(page = SignupPages.EDUCATION.pageNumber,education = education)),token)
    }

    override suspend fun addFamilyPlanSignupFlow(family: String, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(page = SignupPages.FAMILY_PLAN.pageNumber, family = family)),token)
    }

    override suspend fun addDrinkPrefSignupFlow(drink: String, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(page = SignupPages.DRINK.pageNumber, drinking = drink)),token)
    }

    override suspend fun addSmokePrefSignupFlow(smoke: String, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(smoking = smoke, page = SignupPages.SMOKE.pageNumber)),token)
    }

    override suspend fun addReligionPrefSignupFlow(religion: String, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(religion = religion, page = SignupPages.RELIGION.pageNumber)),token)
    }

    override suspend fun addLanguagesSignupFlow(languages: List<String>, token: String): Flow<Resource<Boolean>> {
        return signupFlowData(SignupData(signup = SignupData.Signup(language = languages, page = SignupPages.LANGUAGE.pageNumber)),token)
    }

    private fun signupFlowData(data: SignupData, token: String):Flow<Resource<Boolean>>{
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.signup(data,token = token)
            emit(Resource.Loading(false))
            if (!response) {
                emit(Resource.Error("Something went wrong"))
            }else{
                emit(Resource.Success(response))
            }
        }
    }

    override suspend fun verifyEmail(email: String, otp: String, token:String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.verify(VerifyEmail(email, code = otp), token)
            emit(Resource.Loading(false))
            if (!response) {
                emit(Resource.Error("Something went wrong"))
            }else{
                emit(Resource.Success(response))
            }
        }
    }

    override suspend fun getAppLanguage(token: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.getAppLanguage(token =  token)
            emit(Resource.Loading(false))
            if (response.isNullOrBlank()) {
                emit(Resource.Error("Something went wrong"))
            }else{
                emit(Resource.Success(response))
            }
        }
    }

    override suspend fun setAppLanguage(token: String, language: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.setAppLanguage(language = language, token =  token)
            emit(Resource.Loading(false))
            if (!response) {
                emit(Resource.Error("Something went wrong"))
            }else{
                emit(Resource.Success(response))
            }
        }
    }

    override suspend fun sendSupportRequest(token: String, data: HelpSupportRequestData): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val presignUrls = accountsApiService.getPresignedUrl(token = token, data = SignedMediaUrlRequest(files = List(data.images.size) { index-> SignedMediaUrlRequest.FileData("img_$index.png","image") }))
            if (presignUrls?.filter { !it?.presignUrl.isNullOrBlank() && !it?.mediaId.isNullOrBlank() }.isNullOrEmpty()){
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
                return@flow
            }
            val uploadIds = presignUrls!!.mapIndexedNotNull{ index,urlData ->
                val currentPath = data.images[index]
                if (!urlData?.presignUrl.isNullOrBlank() && !urlData?.mediaId.isNullOrBlank()){
                    val response =
                        accountsApiService.uploadImage(url = urlData?.presignUrl!!, filePath = currentPath, type = SignedUrlMediaItem.Type.IMAGE)
                    if (response) urlData.mediaId else null
                } else null
            }
            if (uploadIds.isEmpty()){
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
                return@flow
            }

            val response = accountsApiService.sendSupportRequest(token, data = HelpSupportRequestDto(type = data.type, description = data.description, uploads = uploadIds))
            emit(Resource.Loading(false))
            if (!response) {
                emit(Resource.Error("Something went wrong"))
            }else{
                emit(Resource.Success(response))
            }
        }
    }

    override suspend fun getAnonymousStatus(token: String): Flow<Resource<LocalDate>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.getAnonymousStatus(token =  token)
            emit(Resource.Loading(false))
            if (response.isNullOrBlank()) {
                emit(Resource.Error("Something went wrong"))
            }else{
                val date = LocalDate.parse(response)
                emit(Resource.Success(date))
            }
        }
    }

    override suspend fun setAnonymousMode(token: String): Flow<Resource<LocalDate>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.setAnonymousMode(token =  token)
            emit(Resource.Loading(false))
            if (!response) {
                emit(Resource.Error("Something went wrong"))
            }else{
                val currentMoment = Clock.System.now()
                val currentDate = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault()).date
                val nextDay = currentDate.plus(1, DateTimeUnit.DAY)
                emit(Resource.Success(nextDay))
            }
        }
    }

    override suspend fun removeAnonymousMode(token: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.removeAnonymousMode(token =  token)
            emit(Resource.Loading(false))
            if (!response) {
                emit(Resource.Error("Something went wrong"))
            }else{
                emit(Resource.Success(response))
            }
        }
    }
}