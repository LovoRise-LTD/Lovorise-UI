package com.lovorise.app.accounts.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import coil3.PlatformContext
import com.lovorise.app.accounts.domain.AccountsRepo
import com.lovorise.app.accounts.domain.model.AddVerifyRecoveryEmailRequest
import com.lovorise.app.accounts.domain.model.AppSettingsData
import com.lovorise.app.accounts.domain.model.BlockedUser
import com.lovorise.app.accounts.domain.model.ChangeEmailRequest
import com.lovorise.app.accounts.domain.model.ChangePasswordRequest
import com.lovorise.app.accounts.domain.model.ForgotPasswordData
import com.lovorise.app.accounts.domain.model.GetMediaResponse
import com.lovorise.app.accounts.domain.model.HelpSupportRequestData
import com.lovorise.app.accounts.domain.model.ReorderImageItem
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.accounts.domain.model.SignupWithEmailResponse
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.accounts.presentation.onboarding.OnboardingScreen
import com.lovorise.app.accounts.presentation.restrictions.Over18OnlyScreen
import com.lovorise.app.accounts.presentation.signin.SignInPasswordScreen
import com.lovorise.app.accounts.presentation.signup.age.AgeScreen
import com.lovorise.app.accounts.presentation.signup.choose_interests.ChooseInterestsScreen
import com.lovorise.app.accounts.presentation.signup.dating_prefs.DatingPreferenceScreen
import com.lovorise.app.accounts.presentation.signup.drink.DrinkScreen
import com.lovorise.app.accounts.presentation.signup.education_level.EducationLevelScreen
import com.lovorise.app.accounts.presentation.signup.email.EmailScreen
import com.lovorise.app.accounts.presentation.signup.email.VerifyEmailScreen
import com.lovorise.app.accounts.presentation.signup.family_planning.FamilyPlanningScreen
import com.lovorise.app.accounts.presentation.signup.gender.GenderScreen
import com.lovorise.app.accounts.presentation.signup.guidelines.GuidelinesScreen
import com.lovorise.app.accounts.presentation.signup.height.HeightScreen
import com.lovorise.app.accounts.presentation.signup.language.LanguageScreen
import com.lovorise.app.accounts.presentation.signup.location.LocationScreen
import com.lovorise.app.accounts.presentation.signup.meeting_prefs.WhoDoYouLikeToMeetScreen
import com.lovorise.app.accounts.presentation.signup.name.NameScreen
import com.lovorise.app.accounts.presentation.signup.notification.NotificationPermissionScreen
import com.lovorise.app.accounts.presentation.signup.password.CreatePasswordScreen
import com.lovorise.app.accounts.presentation.signup.privacy.PrivacyScreen
import com.lovorise.app.accounts.presentation.signup.profile_upload.ProfileUploadScreen
import com.lovorise.app.accounts.presentation.signup.religion.ReligionScreen
import com.lovorise.app.accounts.presentation.signup.smoke.SmokeScreen
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.libs.auth.AppleAuthManager
import com.lovorise.app.libs.auth.GoogleOauth
import com.lovorise.app.libs.cache.MediaCacheManager
import com.lovorise.app.libs.iap.AppleReceiptData
import com.lovorise.app.libs.iap.GoogleReceiptData
import com.lovorise.app.libs.location.LocationData
import com.lovorise.app.libs.scheduler.refreshToken
import com.lovorise.app.libs.scheduler.scheduleTokenRefreshTask
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class AccountsViewModel(
    private val accountsRepo: AccountsRepo
) : KoinComponent, ScreenModel {

    private var prefs:SharedPrefs? = null

    private val _state = MutableStateFlow(AccountsApiCallState())
    val state = _state.asStateFlow()

    private var mediaCacheManager:MediaCacheManager? = null


    private suspend fun getCachedMedias(medias:List<GetMediaResponse.MediaData>,context: PlatformContext):List<GetMediaResponse.MediaData>{
        if (mediaCacheManager == null){
            mediaCacheManager = MediaCacheManager(context)
        }
        val newMedias = mutableListOf<GetMediaResponse.MediaData>()
        return withContext(Dispatchers.IO){
            medias.forEach { media->
                val localPath = mediaCacheManager!!.getMediaPathIfExists(media.url) ?: mediaCacheManager!!.downloadAndCache(media.url)
                newMedias.add(media.copy(localPath = localPath))
            }
            newMedias
        }
    }


    private fun getUser(token: String,navigateToSignIn:()->Unit,context: PlatformContext){
        screenModelScope.launch {
            try {
                accountsRepo.getUser(token).collectLatest { res ->
                    if (res is Resource.Success &&  res.data != null){
                        val medias = getCachedMedias (res.data.medias?.toMutableList() ?: mutableListOf(), context = context)
                        _state.update {
                            it.copy(
                                user = res.data.copy(medias = medias),
                                currentLocation = LocationData(
                                    city = res.data.locationData?.city,
                                    longitude = res.data.locationData?.longitude ?: 0.0,
                                    latitude = res.data.locationData?.latitude ?: 0.0,
                                    country = res.data.locationData?.country
                                )
                            )
                        }
                    }
                    if (res is Resource.Error){
                        val refresh = refreshToken(context)
                        val newToken = getToken(context) ?: return@collectLatest
                        if (refresh){
                            getUser(newToken,navigateToSignIn, context)
                        }else{
                            navigateToSignIn()
                        }
                    }
                }
                //fetch password status
                if (state.value.isPasswordCreated == null) {
                    withContext(Dispatchers.IO) {
                        accountsRepo.passwordStatus(token).collectLatest { res->
                            if (res is Resource.Success && res.data != null){
                                _state.update {
                                    it.copy(
                                        isPasswordCreated = res.data
                                    )
                                }
                            }
                        }
                    }
                }


            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun updateLastLoginType(context: PlatformContext, type:LastLoginType?){
//        _state.update {
//            it.copy(
//                lastLoginType = type
//            )
//        }
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        prefs!!.setString(PreferencesKeys.LAST_LOGIN_TYPE,type?.name)
    }

    fun loadLastLoginType(context: PlatformContext){
        screenModelScope.launch {
            if (prefs == null){
                prefs = SharedPrefsImpl(context)
            }

            val lastLoginType = prefs!!.getString(PreferencesKeys.LAST_LOGIN_TYPE,null)

//            if (lastLoginType == null){
//                if (GoogleOauth(context).isLastLoggedIn()){
//                    updateLastLoginType(context,LastLoginType.GOOGLE)
//                }
//            }

            if (lastLoginType != null){
                _state.update {
                    it.copy(
                        lastLoginType = LastLoginType.valueOf(lastLoginType)
                    )
                }
            }
        }
    }

    fun setDeletedAccountName(name:String?){
        _state.update{
            it.copy(
                deletedAccountName = name
            )
        }
    }

    suspend fun verifyApplePurchase(data:AppleReceiptData, context: PlatformContext) : Boolean{
        val token = getToken(context) ?: return false
        return accountsRepo.verifyApplePurchase(token, data)
    }

    suspend fun verifyGooglePurchase(data:GoogleReceiptData, context: PlatformContext) : Boolean{
        val token = getToken(context) ?: return false
        return accountsRepo.verifyGooglePurchase(token, data)
    }

    fun getAppLanguage(context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        val appLanguage = prefs!!.getString(PreferencesKeys.APP_LANGUAGE)
        if (!appLanguage.isNullOrBlank()) {
            _state.update {
                it.copy(language = appLanguage)
            }
        }else{
            val token = getToken(context) ?: return
            screenModelScope.launch {
                accountsRepo.getAppLanguage(token).collectLatest { res ->
                    if (res is Resource.Success && !res.data.isNullOrBlank()){
                        _state.update {
                            it.copy(
                                language = res.data
                            )
                        }
                    }
                }
            }
        }
    }

    fun setAppLanguage(context: PlatformContext,language:String){
        val token = getToken(context) ?: return

        prefs!!.setString(PreferencesKeys.APP_LANGUAGE,language)

        _state.update {
            it.copy(language = language)
        }

        screenModelScope.launch {
            accountsRepo.setAppLanguage(token,language).collectLatest { res ->
                handleSignupFlowResponse(res)
            }
        }

    }

    fun sendSupportRequest(context: PlatformContext,data: HelpSupportRequestData,onSubmitted:()->Unit){
        val token = getToken(context) ?: return
        screenModelScope.launch {
            accountsRepo.sendSupportRequest(token, data).collectLatest {
                if (it is Resource.Success && it.data == true){
                    onSubmitted()
                }
                handleSignupFlowResponse(it)
            }
        }
    }

    fun getAppSettings(context: PlatformContext){
        if (state.value.appSettingsData != null && state.value.isSettingsApiCallMade) return
        val token = getToken(context) ?: return
        screenModelScope.launch {
            accountsRepo.getAppSettings(token).collectLatest {res ->
                if (res is Resource.Success){
                    _state.update {
                        it.copy(
                            appSettingsData = res.data,
                            isSettingsApiCallMade = true
                        )
                    }
                }
                handleSignupFlowResponse(res)
            }
        }
    }

    fun updateReadReceipt(context: PlatformContext,value: Boolean){
        val token = getToken(context) ?: return
        screenModelScope.launch {
            accountsRepo.updateReadReceipt(token, value = value).collectLatest {res ->
                if (res is Resource.Success){
                    _state.update {
                        it.copy(
                            appSettingsData = state.value.appSettingsData?.copy(
                                readReceipt = value
                            )
                        )
                    }
                }
                handleSignupFlowResponse(res)
            }
        }
    }

    suspend fun createTravelTicket(context: PlatformContext,data:AppSettingsData.TravelTicketStatus):Boolean{
        val token = getToken(context) ?: return false
        val success = accountsRepo.createTravelTicket(token,data)
        if (success){
            _state.update {
                it.copy(
                    appSettingsData = state.value.appSettingsData?.copy(
                        travelTicketStatus = data.copy(
                            enabled = true
                        )
                    )
                )
            }
        }
        return success
    }

    fun updateNotificationSettings(context: PlatformContext,data: AppSettingsData.NotificationSettings,locallyOnly:Boolean = true){
        if (locallyOnly) {
            _state.update {
                it.copy(
                    appSettingsData = state.value.appSettingsData?.copy(
                        notification = data,
                        isNotificationDataUpdated = true
                    )
                )
            }
        } else {
            if (state.value.appSettingsData?.isNotificationDataUpdated != true) return
            val token = getToken(context) ?: return
            screenModelScope.launch {
                accountsRepo.updateNotificationSettings(token, data = data).collectLatest {res ->
                     if (res is Resource.Success){
                         _state.update{
                             it.copy(
                                 appSettingsData = state.value.appSettingsData?.copy(
                                     isNotificationDataUpdated = false
                                 )
                             )
                         }
                     }
                    handleSignupFlowResponse(res)
                }
            }
        }
    }

    suspend fun getFileSizeFromUrl(url: String): Long?{
        return accountsRepo.getFileSizeFromUrl(url)
    }

    fun updateControlProfile(context: PlatformContext,data:AppSettingsData.ControlProfile){
        val token = getToken(context) ?: return
        screenModelScope.launch {
            accountsRepo.updateControlProfile(token, data = data).collectLatest {res ->
                if (res is Resource.Success){
                    _state.update {
                        it.copy(
                            appSettingsData = state.value.appSettingsData?.copy(
                                controlProfile = data
                            )
                        )
                    }
                }
                handleSignupFlowResponse(res)
            }
        }
    }

    fun setAnonymousStatus(context: PlatformContext,onSuccess: () -> Unit){
        val token = getToken(context) ?: return
        screenModelScope.launch {
            accountsRepo.setAnonymousMode(token).collectLatest {res ->
                if (res is Resource.Success){
                    onSuccess()
                    _state.update {
                        it.copy(
                            appSettingsData = state.value.appSettingsData?.copy(
                                anonymousModeExpiry = res.data
                            )
                        )
                    }
                }
                handleSignupFlowResponse(res)
            }
        }
    }

    fun removeAnonymousMode(context: PlatformContext,onSuccess: () -> Unit){
        val token = getToken(context) ?: return
        screenModelScope.launch {
            accountsRepo.removeAnonymousMode(token).collectLatest {res ->
                if (res is Resource.Success && res.data == true){
                    onSuccess()
                    _state.update {
                        it.copy(
                            appSettingsData = state.value.appSettingsData?.copy(
                                anonymousModeExpiry = null
                            )
                        )
                    }
                }
                handleSignupFlowResponse(res)
            }
        }
    }

    fun updateLoadingState(value: Boolean){
        _state.update {
            it.copy(
                isLoading = value
            )
        }
    }



    fun changeOrSetPassword(data:ChangePasswordRequest,ctx: PlatformContext){
        val token = getToken(ctx) ?: return
        screenModelScope.launch {
            accountsRepo.changePassword(token, data).collectLatest { res ->
                if(res is Resource.Success && res.data == true){
                    _state.update {
                        it.copy(
                            isPasswordCreated = true
                        )
                    }
                }
                handleSignupFlowResponse(res)
            }
        }
    }

    fun changeEmail(data:ChangeEmailRequest,ctx: PlatformContext,email: String? = null){
        val token = getToken(ctx) ?: return
        screenModelScope.launch {
            accountsRepo.changeEmail(token, data).collectLatest { res ->
                if (res is Resource.Success && data.page == 4 && email != null ){
                    updateUserRes(state.value.user!!.copy(email = email))
                }
                if (res.data?.changeToken != null) {
                    _state.update {
                        it.copy(
                            changeEmailToken = res.data.changeToken,
                        )
                    }
                }
                handleSignupFlowResponse(res)
            }
        }
    }

    fun updateEmailVerificationState(value: Boolean){
        val newUser = state.value.user?.copy(isEmailVerified = value) ?: return
        updateUserRes(newUser)
    }



    fun addVerifyRecoveryEmail(data:AddVerifyRecoveryEmailRequest,ctx: PlatformContext){
        val token = getToken(ctx) ?: return
        screenModelScope.launch {
            accountsRepo.verifyAddRecoveryEmail(token, data).collectLatest { res ->
                handleSignupFlowResponse(res)
            }
        }
    }

    fun blockUser(userId:String,ctx: PlatformContext){
        val token = getToken(ctx) ?: return
        screenModelScope.launch {
            accountsRepo.blockUser(token, id = userId).collectLatest { res ->
                handleSignupFlowResponse(res)
            }
        }
    }

    fun unblockUser(userId:String,ctx: PlatformContext){
        val token = getToken(ctx) ?: return
        screenModelScope.launch {
            accountsRepo.unblockUser(token, id = userId).collectLatest { res ->
                handleSignupFlowResponse(res)
            }
        }
    }

    fun getBlockedUsers(ctx: PlatformContext,onSuccess:(List<BlockedUser>)->Unit){
        val token = getToken(ctx) ?: return
        screenModelScope.launch {
            accountsRepo.getBlockedUsers(token).collectLatest { res ->
                handleSignupFlowResponse(res, skipSuccess = true)
                if (res is Resource.Success){
                    onSuccess(res.data ?: emptyList())
                }
            }
        }
    }

    fun getCurrentUser():UserResponse?{
        return state.value.user
    }

    fun updateUserRes(user: UserResponse){
        _state.update {
            it.copy(
                user = user
            )
        }
    }



    suspend fun reorderImages(ctx: PlatformContext,data:List<ReorderImageItem>,refreshUser:Boolean,updateOnlyLocally:Boolean){
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        val token = prefs?.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        if (refreshUser){
            val userData = state.value.user
            val currentImages = userData?.medias ?: emptyList()
            val resultData = mutableListOf<GetMediaResponse.MediaData>()
            data.sortedBy { it.orderNum }.forEach { i->
                val filteredData = currentImages.firstOrNull{it.id == i.mediaId}
                println("the filteredData is $filteredData")
                if (filteredData != null){
                    resultData.add(filteredData.copy(id = i.mediaId, orderNum = i.orderNum))
                }
            }
            _state.update {
                it.copy(
                    user = userData?.copy(medias = resultData)
                )
            }
        }
        if (!updateOnlyLocally) {
            accountsRepo.reorderImage(token, data).collectLatest { res ->
                println(res.data)
            }
        }
    }

    private fun getToken(ctx:PlatformContext):String?{
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        return prefs?.getString(PreferencesKeys.AUTH_TOKEN)
    }

    fun getUser(ctx: PlatformContext,navigateToSignIn:()->Unit){
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        val token = prefs?.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        val refreshToken = prefs?.getString(PreferencesKeys.REFRESH_TOKEN)
        getUser(token,navigateToSignIn, context = ctx)
    }

    fun deleteImage(imageId:String?,ctx: PlatformContext,onDeleted:()->Unit ={}){
        if (imageId.isNullOrBlank()) return
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        val token = prefs?.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            accountsRepo.deleteImage(token,imageId).collectLatest { res->
                onDeleted()
                handleSignupFlowResponse(res)
                println(res.data)
            }
        }
    }


    fun resetSuccessState(){
        _state.update {
            it.copy(
                success = false,
                error = null
            )
        }
    }

    fun signup(email:String,ctx: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        screenModelScope.launch {
            accountsRepo.signup(email).collectLatest { res ->
                if (res.data?.nextScreen != null){

                    updateNextScreenState(res.data.nextScreen)
                }
                handleSignupFlowResponse(res, storeToken = true, context = ctx)
            }
        }
    }

    fun checkEmailAndNavigate(email:String,ctx: PlatformContext,navigateTo:(Screen)->Unit){
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        screenModelScope.launch {
            accountsRepo.getEmailStatus(email).collectLatest { res ->

                when(res){
                    is Resource.Loading ->{
                        _state.update {
                            it.copy(
                                isLoading = res.isLoading,
                            )
                        }
                    }
                    is Resource.Error ->{
                        _state.update {
                            it.copy(
                                error = res.message,
                            )
                        }
                    }
                    is Resource.Success -> {
                        if (!res.data?.token.isNullOrBlank()){
                            val data = res.data
                            if (res.data?.nextScreen != null){
                                updateNextScreenState(res.data.nextScreen)
                            }
                        //    handleSignupFlowResponse(res, storeToken = true, context = ctx)

                            setAuthToken(data?.token!!,data.refreshToken,ctx)
                            res.data.nextScreen?.let{navigateTo(getScreenFromString(it))}
                        }else{
                            if (res.data?.nextScreen == "LOGIN_PASSWORD"){
                                navigateTo(SignInPasswordScreen(email))
                            }
                        }
                    }
                }


            }
        }
    }

    private fun updateNextScreenState(screen:String?){
        _state.update {
            it.copy(
                nextScreen = screen
            )
        }
    }

    fun reloadState(){
        _state.update {
            AccountsApiCallState()
        }
    }

    fun signInWithGoogle(context: PlatformContext){
        screenModelScope.launch {
            val googleAuthManager = GoogleOauth(context)
            val data = googleAuthManager.login()

            _state.update {
                it.copy(
                    currentEmail = data?.email
                )
            }


            data?.token?.let { token ->
                accountsRepo.loginWithGoogle(token).collectLatest { res ->
                    if (res is Resource.Success && res.data != null) {

                        updateLastLoginType(context,LastLoginType.GOOGLE)

                        if (res.data.nextScreen != null){
                            updateNextScreenState(res.data.nextScreen)
                        }

                        if (prefs == null) {
                            prefs = SharedPrefsImpl(context)
                        }
                        if (res.data.isRegistrationCompleted) {
                            setLoginStatus()
                        }
                        setAuthToken(res.data.token,res.data.refreshToken,context)
                        _state.update {
                            it.copy(
                                oAuthLoginResponse = res.data
                            )
                        }
                    }
                    handleSignupFlowResponse(res, context = context)
                }
            }
        }
    }

    fun signInWithApple(context: PlatformContext){
        screenModelScope.launch {
            val appleAuthManager = AppleAuthManager()

            val data = appleAuthManager.login()

            _state.update {
                it.copy(
                    currentEmail = data?.email
                )
            }

            data?.token?.let { token ->
                _state.update {
                    it.copy(
                        testCode = token
                    )
                }
                accountsRepo.loginWithApple(token).collectLatest { res ->
                    if (res is Resource.Success && res.data != null) {
                        if (prefs == null) {
                            prefs = SharedPrefsImpl(context)
                        }

                        updateLastLoginType(context,LastLoginType.APPLE)

                        if (res.data.nextScreen != null){
                            updateNextScreenState(res.data.nextScreen)
                        }

                        if (res.data.isRegistrationCompleted) {
                            setLoginStatus()
                        }
                        setAuthToken(res.data.token,res.data.refreshToken,context)
                        _state.update {
                            it.copy(
                                oAuthLoginResponse = res.data
                            )
                        }
                    }
                    handleSignupFlowResponse(res, context = context)
                }
            }
        }

    }

    fun signin(email:String,password:String,ctx:PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        screenModelScope.launch {
            accountsRepo.signin(email,password).collectLatest { res ->
                if (res is Resource.Success && res.data != null){
                    updateLastLoginType(ctx,LastLoginType.EMAIL)
                    setLoginStatus()
                }
                handleSignupFlowResponse(res, storeToken = true, context = ctx)
            }
        }
    }

    fun forgotPassword(data: ForgotPasswordData,ctx:PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        screenModelScope.launch {
            accountsRepo.forgotPassword(data).collectLatest { res ->
                if (res is Resource.Success && res.data != null){

                    if (!res.data.token.isNullOrBlank()){
                        //to do use actual refresh token
                        setAuthToken(res.data.token,null,ctx)
                    }

                    _state.update {
                        it.copy(
                            forgotPasswordResponse = res.data
                        )
                    }
                }
                handleSignupFlowResponse(res, context = ctx)
            }
        }
    }

    fun updateErrorValue(error:String){
        _state.update {
            it.copy(
                error = error
            )
        }
    }

    fun addName(name:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addNameSignupFlow(name, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addAge(age:Int,birthDate:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addAgeSignupFlow(age = age, birthDate = birthDate, token =  token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addGender(gender:String,moreInfo:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addGenderSignupFlow(gender=gender, moreInfo = moreInfo,token=token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addWhoDoYouLikeToMeet(prefs:List<String>){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addWhoDoYouLikeToMeetSignupFlow(prefs, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addDatingPref(prefs:List<String>){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addDatingPrefsSignupFlow(prefs, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addInterests(interests:Map<String,List<String>>){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addInterestsSignupFlow(interests, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun resendEmailOtp(context: PlatformContext){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }

        if (!state.value.isLoading){
            val token = getAuthToken() ?: return
            screenModelScope.launch {
                accountsRepo.resendOtp(token).collectLatest {
                    handleSignupFlowResponse(it, skipSuccess = true)
                }
            }
        }

    }

    fun deleteAccount(context: PlatformContext,reasons:List<String>){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        if (!state.value.isLoading){
            val token = getAuthToken() ?: return
            screenModelScope.launch {
                accountsRepo.deleteAccount(token,reasons).collectLatest {
                    handleSignupFlowResponse(it)
                }
            }
        }
    }

    fun confirmPasswordForDelete(password:String,ctx: PlatformContext,name: String,onSuccess: () -> Unit){
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        if (!state.value.isLoading){
            val token = getAuthToken() ?: return
            screenModelScope.launch {
                accountsRepo.deleteAccountPasswordVerification(token,password).collectLatest {
                    if (it is Resource.Success) {
                        if (it.data == true) {
                            logout(ctx, deletedName = name, onSuccess = onSuccess)
                           // onSuccess()
                        }
                    }
                 //   handleSignupFlowResponse(it)

                }
            }
        }
    }

    fun pauseAccount(context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        if (!state.value.isLoading){
            val token = getAuthToken() ?: return
            screenModelScope.launch {
                accountsRepo.pauseAccount(token).collectLatest {
                    handleSignupFlowResponse(it)
                    if (it is Resource.Success && it.data == true) {
                        getUser(context) {}
                    }
                }
            }
        }
    }

    fun resumeAccount(context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        if (!state.value.isLoading){
            val token = getAuthToken() ?: return
            screenModelScope.launch {
                accountsRepo.resumeAccount(token).collectLatest {
                    handleSignupFlowResponse(it)
                    if (it is Resource.Success && it.data == true) {
                        getUser(context){}
                    }
                }
            }
        }
    }

    fun addHeight(height:Double){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addHeightSignupFlow(height, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addEducationLevel(educationLevel:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addEducationLevelSignupFlow(educationLevel, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addFamilyPlan(family:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addFamilyPlanSignupFlow(family, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addDrink(drink:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addDrinkPrefSignupFlow(drink, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addSmoke(smoke:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addSmokePrefSignupFlow(smoke, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addReligion(religion:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addReligionPrefSignupFlow(religion, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun addLanguages(languages:List<String>){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.addLanguagesSignupFlow(languages, token).collectLatest { handleSignupFlowResponse(it) }
        }
    }

    fun createPassword(password:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.createPasswordSignupFlow(password,token).collectLatest { res ->
                handleSignupFlowResponse(res)
            }
        }
    }

    fun verifyEmail(email:String,otp:String){
        val token = getAuthToken() ?: return
        screenModelScope.launch {
            accountsRepo.verifyEmail(email,otp,token).collectLatest { res ->
                when(res){
                    is Resource.Success ->{
                        _state.update {
                            it.copy(
                                success = true
                            )
                        }
                    }
                    is Resource.Loading ->{
                        _state.update {
                            it.copy(
                                isLoading = res.isLoading,
                            )
                        }
                    }
                    is Resource.Error ->{
                        _state.update {
                            it.copy(
                                error = "Verification code is invalid",
                            )
                        }
                    }
                }
            }
        }
    }

    private fun <T> handleSignupFlowResponse(res:Resource<T>,storeToken:Boolean=false,skipSuccess:Boolean=false,context: PlatformContext? = null){
        when(res){
            is Resource.Success ->{
                if (!skipSuccess) {
                    if (storeToken && res.data != null) {
                        val data = res.data
                        if (data is Tokens && data.accessToken != null && context != null) {
                            setAuthToken(data.accessToken,data.refreshToken, context)
                        } else if (data is SignupWithEmailResponse && data.token != null && context != null) {
                            setAuthToken(data.token,data.refreshToken,context)
                        }
                    }
                    _state.update {
                        it.copy(
                            success = true
                        )
                    }
                }
            }
            is Resource.Loading ->{
                _state.update {
                    it.copy(
                        isLoading = res.isLoading,
                    )
                }
            }
            is Resource.Error ->{
                _state.update {
                    it.copy(
                        error = res.message,
                    )
                }
            }
        }
    }

    private fun setAuthToken(token:String,refreshToken:String?,context: PlatformContext){
        prefs?.setString(PreferencesKeys.AUTH_TOKEN,token)
        prefs?.setString(PreferencesKeys.REFRESH_TOKEN,refreshToken)
        scheduleTokenRefreshTask(context)
    }

    private fun getAuthToken():String?{
        return prefs?.getString(PreferencesKeys.AUTH_TOKEN)
    }

    fun uploadConfirmation(uploadIds:List<String>){
        if (uploadIds.isEmpty()) return

        val token = getAuthToken() ?: return
        val inviteCode = prefs?.getString(PreferencesKeys.REFERRAL_CODE)

        screenModelScope.launch {
            accountsRepo.confirmUploadedImages(hashes = uploadIds, token = token, inviteCode = inviteCode).collectLatest {res ->
                if (res is Resource.Success){
                    _state.update {
                        it.copy(
                            uploadedHashes = emptyList(),
                            isUploadConfirmed = true
                        )
                    }
                }
                handleSignupFlowResponse(res)
            }
        }
    }

    fun updateCurrentLocation(data: LocationData){
       // if (state.value.currentLocation != null) return
        _state.update {
            it.copy(
                currentLocation = data
            )
        }

    }

    fun uploadMedias(images:List<ImageUploadData>, context: PlatformContext, totalImagesLength:Int, onComplete: () -> Unit){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        val token = getAuthToken() ?: return

        screenModelScope.launch(Dispatchers.IO) {
            val preSignedUrls = accountsRepo.getPresignedUrl(
                files = images.map { SignedUrlMediaItem(type = it.type, fileName = it.fileName) },
                token
            )



            if (preSignedUrls.isNotEmpty()) {

                val initialMedias = preSignedUrls.mapNotNull { data ->
                    val currentData = images.firstOrNull { data.presignUrl.contains(it.fileName) }
                    if (currentData != null) {
                        GetMediaResponse.MediaData(
                            id = data.hash, name = currentData.fileName, url = "", type = currentData.type,
                            localPath = currentData.path, orderNum = currentData.orderNum
                        )
                    }else null
                }
                val existingMedias1 = state.value.user?.medias?.toMutableList() ?: mutableListOf()
                initialMedias.forEach { mediaData->
                    val index = existingMedias1.indexOfFirst { it.orderNum == mediaData.orderNum }
                    if (index != -1) {
                        existingMedias1.add(index, existingMedias1.removeAt(index).copy(
                            id = mediaData.id, type = mediaData.type, url = mediaData.url,
                            orderNum = mediaData.orderNum, localPath = mediaData.localPath, name = mediaData.name
                        ))
                    } else {
                        existingMedias1.add(mediaData)
                    }
                }



               // CoroutineScope(Dispatchers.Main).launch{
                _state.update {
                    it.copy(
                        presignedUrls = preSignedUrls,
                        totalImagesLength = totalImagesLength,
                        user = state.value.user?.copy(medias = existingMedias1.sortedBy { m -> m.orderNum })
                    )
                }

                println("the medias are ${state.value.user?.medias}")

                    onComplete()
              //  }

            //    withContext(Dispatchers.IO){
                    val medias = preSignedUrls.mapNotNull { data ->

                        val currentData = images.firstOrNull { data.presignUrl.contains(it.fileName) }


                        if (currentData != null) {

                            val isUploaded = accountsRepo.uploadImage(
                                url = data.presignUrl,
                                filePath = currentData.path,
                                type = currentData.type
                            )

                            if (isUploaded) {
                                val url = accountsRepo.insertImage(
                                    token,
                                    data.hash,
                                    index = currentData.orderNum
                                )
                                if (!url.isNullOrBlank()) GetMediaResponse.MediaData(
                                    id = data.hash,
                                    name = currentData.fileName,
                                    url = url,
                                    type = currentData.type,
                                    localPath = currentData.path,
                                    orderNum = currentData.orderNum
                                ) else null
                            } else null
                        } else null
                    }

                    val existingMedias = state.value.user?.medias?.toMutableList() ?: mutableListOf()

                    medias.forEach { mediaData ->
                        val index = existingMedias.indexOfFirst { it.orderNum == mediaData.orderNum }
                        if (index != -1) {
                            existingMedias[index] = existingMedias[index].copy(
                                id = mediaData.id, type = mediaData.type, url = mediaData.url,
                                orderNum = mediaData.orderNum, localPath = mediaData.localPath, name = mediaData.name
                            )
                        } else {
                            existingMedias.add(mediaData)
                        }
                    }


                    _state.update {
                        it.copy(user = state.value.user?.copy(medias = existingMedias.sortedBy { m -> m.orderNum }))
                    }
                    println("the medias are 1:  ${state.value.user?.medias}")
               // }



            }
        }

    }

    fun showExitConfirmationDialog(){
        _state.update {
            it.copy(showExitConfirmationDialog = true)
        }
    }

    fun hideExitConfirmationDialog(){
        _state.update {
            it.copy(showExitConfirmationDialog = false)
        }
    }

    fun clearAll(context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        prefs!!.apply {
            setString(PreferencesKeys.AUTH_TOKEN,null)
            setString(PreferencesKeys.REFRESH_TOKEN,null)
            setString(PreferencesKeys.CURRENT_EMAIL,null)
            setString(PreferencesKeys.CURRENT_SIGNUP_PAGE,null)
            setBoolean(PreferencesKeys.IS_LOGGED_IN,false)
            setBoolean(PreferencesKeys.SIGN_UP_COMPLETED,false)
        }
        prefs = null
        _state.update {
            AccountsApiCallState()
        }
    }

    fun logout(context: PlatformContext,deletedName:String?=null,onSuccess: () -> Unit){
        screenModelScope.launch {
            if (prefs == null){
                prefs = SharedPrefsImpl(context)
            }
            prefs!!.apply {
                setString(PreferencesKeys.AUTH_TOKEN,null)
                setString(PreferencesKeys.REFRESH_TOKEN,null)
                setString(PreferencesKeys.CURRENT_EMAIL,null)
                setString(PreferencesKeys.CURRENT_SIGNUP_PAGE,null)
                setBoolean(PreferencesKeys.IS_LOGGED_IN,false)
                setBoolean(PreferencesKeys.SIGN_UP_COMPLETED,false)
            }
            val googleAuthManager = GoogleOauth(context)
            googleAuthManager.logout()
            prefs = null
            _state.update {
                AccountsApiCallState().copy(deletedAccountName = deletedName)
            }
            onSuccess()
        }

    }

    fun setCurrentEmail(email:String){
        prefs?.setString(PreferencesKeys.CURRENT_EMAIL,email)
        _state.update {
            it.copy(
                currentEmail = email.ifBlank { null }
            )
        }
    }

    private fun getCurrentEmail():String?{
        return prefs?.getString(PreferencesKeys.CURRENT_EMAIL)
    }

    private fun isSignupCompleted():Boolean{
        return prefs?.getBoolean(PreferencesKeys.SIGN_UP_COMPLETED,false) ?: false
    }

    fun setSignUpCompleted(context: PlatformContext){
        updateLastLoginType(context,LastLoginType.EMAIL)
        prefs?.setBoolean(PreferencesKeys.SIGN_UP_COMPLETED,true)
    }

    fun setLoginStatus(){
        prefs?.setBoolean(PreferencesKeys.IS_LOGGED_IN,true)
    }

    private fun isLoggedIn():Boolean{
        val token = getAuthToken() ?: return false
        val isSignedIn = prefs?.getBoolean(PreferencesKeys.IS_LOGGED_IN,false) ?: false
        return token.isNotBlank() && isSignedIn
    }

    fun setSignupPage(page: SignupFlowPages,context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        _state.update { AccountsApiCallState() }

        screenModelScope.launch {
            prefs?.setString(PreferencesKeys.CURRENT_SIGNUP_PAGE,page.name)
        }

        val token = getAuthToken() ?: return


        screenModelScope.launch {
            accountsRepo.saveSignupProgress(page,token).collectLatest {
                println(it.data)
            }
        }
    }


    fun getSignUpFlowScreen(context: PlatformContext):Screen{
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }

        val currentPage = state.value.nextScreen ?: prefs?.getString(PreferencesKeys.CURRENT_SIGNUP_PAGE) ?: return OnboardingScreen()

        return getScreenFromString(currentPage)
    }

    fun getScreenFromString(value:String):Screen{
        return when(SignupFlowPages.valueOf(value)){
            SignupFlowPages.EMAIL_SCREEN -> EmailScreen()
            SignupFlowPages.AGE_SCREEN -> AgeScreen()
            SignupFlowPages.CHOOSE_INTEREST_SCREEN -> ChooseInterestsScreen()
            SignupFlowPages.DATING_PREFS_SCREEN -> DatingPreferenceScreen()
            SignupFlowPages.DRINK_SCREEN -> DrinkScreen()
            SignupFlowPages.EDUCATION_LEVEL_SCREEN -> EducationLevelScreen()
            SignupFlowPages.VERIFY_EMAIL_SCREEN -> { val currentEmail = getCurrentEmail(); if (currentEmail != null) VerifyEmailScreen(currentEmail) else EmailScreen() }
            SignupFlowPages.FAMILY_PLANNING_SCREEN -> FamilyPlanningScreen()
            SignupFlowPages.GENDER_SCREEN -> GenderScreen()
            SignupFlowPages.GUIDELINES_SCREEN -> GuidelinesScreen()
            SignupFlowPages.HEIGHT_SCREEN -> HeightScreen()
            SignupFlowPages.LANGUAGE_SCREEN -> LanguageScreen()
            SignupFlowPages.LOCATION_SCREEN -> LocationScreen()
            SignupFlowPages.WHO_DO_YOU_LIKE_TO_MEET_SCREEN -> WhoDoYouLikeToMeetScreen()
            SignupFlowPages.NAME_SCREEN -> NameScreen()
            SignupFlowPages.CREATE_PASSWORD_SCREEN -> CreatePasswordScreen()
            SignupFlowPages.PROFILE_UPLOAD_SCREEN -> ProfileUploadScreen()
            SignupFlowPages.RELIGION_SCREEN -> ReligionScreen()
            SignupFlowPages.SMOKE_SCREEN -> SmokeScreen()
            SignupFlowPages.AGE_RESTRICTION_SCREEN -> Over18OnlyScreen()
            SignupFlowPages.NOTIFICATION_PERMISSION_SCREEN -> NotificationPermissionScreen()
            SignupFlowPages.PRIVACY_SCREEN -> PrivacyScreen()
        }
    }


    fun getStartingScreen(context: PlatformContext): Screen{
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        return if (isSignupCompleted() || isLoggedIn())  HomeScreen() else getSignUpFlowScreen(context)
    }

}

data class Tokens(
    val accessToken:String?,
    val refreshToken:String?
)

data class ImageUploadData(
   // val byteArray : ByteArray,
    val fileName : String,
    val orderNum:Int,
    val path:String,
    val type:SignedUrlMediaItem.Type
)

enum class SignupFlowPages{
    EMAIL_SCREEN,
    AGE_SCREEN,
    CHOOSE_INTEREST_SCREEN,
    DATING_PREFS_SCREEN,
    DRINK_SCREEN,
    EDUCATION_LEVEL_SCREEN,
    VERIFY_EMAIL_SCREEN,
    FAMILY_PLANNING_SCREEN,
    GENDER_SCREEN,
    GUIDELINES_SCREEN,
    HEIGHT_SCREEN,
    LANGUAGE_SCREEN,
    LOCATION_SCREEN,
    WHO_DO_YOU_LIKE_TO_MEET_SCREEN,
    NAME_SCREEN,
    CREATE_PASSWORD_SCREEN,
    PROFILE_UPLOAD_SCREEN,
    RELIGION_SCREEN,
    SMOKE_SCREEN,
    AGE_RESTRICTION_SCREEN,
    NOTIFICATION_PERMISSION_SCREEN,
    PRIVACY_SCREEN
}