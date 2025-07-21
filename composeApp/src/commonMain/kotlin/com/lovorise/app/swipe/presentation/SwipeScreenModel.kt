package com.lovorise.app.swipe.presentation

import androidx.compose.ui.unit.Dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.registry.screenModule
import coil3.PlatformContext
import com.lovorise.app.libs.cache.MediaCacheManager
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.swipe.domain.SwipeProfileUser
import com.lovorise.app.swipe.domain.repo.SwipeRepo
import com.lovorise.app.swipe.presentation.components.SwipeDialogType
import com.lovorise.app.swipe.presentation.components.SwipeType
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SwipeScreenModel(private val swipeRepo: SwipeRepo) : ScreenModel {

    private val _state = MutableStateFlow(SwipeScreenState())

    val state = _state.asStateFlow()

    private var prefs:SharedPrefs? = null

    private var mediaCacheManager:MediaCacheManager? = null


    fun updateOffsetY(value:Float,id:String){

        val index = state.value.profiles.indexOfFirst { it.user.id == state.value.currentlyVisibleProfileId }
        println("the index is $index for id: $id ${state.value.currentlyVisibleProfileId}")
        if (index == -1) return
        val profiles = state.value.profiles.toMutableList().apply {
            add(index,removeAt(index).copy(offsetY = value))
        }

        _state.update {
            it.copy(profiles = profiles)
        }
    }

    fun updateOffsetX(value:Float,id:String){

        val index = state.value.profiles.indexOfFirst { it.user.id == state.value.currentlyVisibleProfileId }
        println("the index is $index for id: $id ${state.value.currentlyVisibleProfileId} value: $value")
        if (index == -1) return
        val profiles = state.value.profiles.toMutableList().apply {
            add(index,removeAt(index).copy(offsetX = value))
        }

        _state.update {
            it.copy(profiles = profiles)
        }
    }

    fun updateTempSkipState(value: Boolean){
        val id = if (value) state.value.profiles.lastOrNull()?.user?.id ?: "" else ""
        _state.update {
            it.copy(
                skipTemporarily = value,
                //currentlyUpdatingIndex = state.value.profiles.lastIndex,
                currentlyVisibleProfileId = id
            )
        }
    }

    fun updateLikeProfileState(value: Boolean){
        val id = if (value) state.value.profiles.lastOrNull()?.user?.id ?: "" else ""
        _state.update {
            it.copy(
                likeProfile = value,
                //currentlyUpdatingIndex = state.value.profiles.lastIndex,
                currentlyVisibleProfileId = id
            )
        }
    }


    fun updateDislikeProfileState(value: Boolean){
        val id = if (value) state.value.profiles.lastOrNull()?.user?.id ?: "" else ""
        _state.update {
            it.copy(
                dislikeProfile = value,
                //currentlyUpdatingIndex = state.value.profiles.lastIndex,
                currentlyVisibleProfileId = id
            )
        }
    }



    fun updateShowGuideState(context: PlatformContext){
        if (state.value.isPrefsDataLoaded) return
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }

        val isNotInterestedDialogPresented = prefs!!.getBoolean(PreferencesKeys.IS_NOT_INTERESTED_DIALOG_SHOWN,false)
        val isInterestedDialogPresented = prefs!!.getBoolean(PreferencesKeys.IS_INTERESTED_DIALOG_SHOWN,false)
        val isSwipeGuidelinesShown = prefs!!.getBoolean(PreferencesKeys.IS_SWIPE_GUIDELINES_SHOWN,false)
        val isSkipTemporaryDialogPresented = prefs!!.getBoolean(PreferencesKeys.IS_SKIP_TEMPORARY_DIALOG_SHOWN,false)

        _state.update {
            it.copy(
                isSwipeGuideShown = isSwipeGuidelinesShown,
                presentedDialogs = state.value.presentedDialogs.toMutableList().apply {
                    if (isNotInterestedDialogPresented) add(SwipeDialogType.LEFT_SWIPE_DIALOG)
                    if (isInterestedDialogPresented) add(SwipeDialogType.RIGHT_SWIPE_DIALOG)
                    if (isSkipTemporaryDialogPresented) add(SwipeDialogType.SKIP_TEMPORARY_DIALOG)
                },
                showSwipeGuide = !isSwipeGuidelinesShown,
                isPrefsDataLoaded = true
            )
        }

    }

    fun onSwipeGuidelinesShown(context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        prefs!!.setBoolean(PreferencesKeys.IS_SWIPE_GUIDELINES_SHOWN,true)
    }

    private fun onNotInterestedDialogShown(context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        prefs!!.setBoolean(PreferencesKeys.IS_NOT_INTERESTED_DIALOG_SHOWN,true)
    }

    private fun onSkipTemporaryDialogShown(context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        prefs!!.setBoolean(PreferencesKeys.IS_SKIP_TEMPORARY_DIALOG_SHOWN,true)
    }


    private fun onInterestedDialogShown(context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        prefs!!.setBoolean(PreferencesKeys.IS_INTERESTED_DIALOG_SHOWN,true)
    }

    fun updateSpacingValue(value:Dp){
        _state.update {
            it.copy(
                spacing = value
            )
        }
    }

    fun updateShowTopBarState(value:Boolean){
        _state.update {
            it.copy(
                showTopBar = value
            )
        }
    }

    fun updateShowSwipeGuide(value:Boolean){
        _state.update {
            it.copy(
                showSwipeGuide = value
            )
        }
    }

    fun updateShowMenuItem(value:Boolean){
        _state.update {
            it.copy(
                showMenuItem = value
            )
        }
    }

    fun updateShowReportBottomSheet(value:Boolean){
        _state.update {
            it.copy(
                showReportBottomSheet = value
            )
        }
    }

    fun updateIsSwipeGuideShown(value:Boolean){
        _state.update {
            it.copy(
                isSwipeGuideShown = value
            )
        }
    }

    fun updateShowBlockDialog(value:Boolean){
        _state.update {
            it.copy(
                showBlockDialog = value
            )
        }
    }

    fun updateShowToast(value:Boolean){
        _state.update {
            it.copy(
                showToast = value
            )
        }
    }

    fun updateReloadState(value:Boolean){
        _state.update {
            it.copy(
                shouldReloadProfiles = value
            )
        }
    }

    fun getUserProfiles(context: PlatformContext){
        if (!state.value.shouldReloadProfiles) return
        val token = getToken(context) ?: return
        if (mediaCacheManager == null){
            mediaCacheManager = MediaCacheManager(context)
        }
        screenModelScope.launch {
            swipeRepo.getUsers(token,mediaCacheManager!!).collectLatest { res ->
                println("the swipe users data is ${res.data}")
                when(res){
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                profiles = res.data?.map { profile ->
                                    val medias = profile.user.medias?.map { media->
                                        val url = mediaCacheManager!!.downloadAndCacheIfRequired(media.url)
                                        media.copy(
                                            url = url ?: media.url
                                        )
                                    }
                                    profile.copy(
                                        user = profile.user.copy(
                                            medias = medias
                                        )
                                    )
                                } ?: emptyList(),
                                shouldReloadProfiles = false
                            )
                        }
                        val id = state.value.profiles.lastOrNull()?.user?.id
                        if (!id.isNullOrBlank()){
                            _state.update {
                                it.copy(
                                    currentlyVisibleProfileId = id
                                )
                            }
                        }
                    }

                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        _state.update{
                            it.copy(
                                isLoading = res.isLoading
                            )
                        }
                    }
                }

            }
        }

    }

    private fun getToken(ctx:PlatformContext):String?{
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        return prefs?.getString(PreferencesKeys.AUTH_TOKEN)
    }



//    private fun reloadProfiles(){
//        screenModelScope.launch {
//            _state.update {
//                it.copy(isLoading = true)
//            }
//            delay(2100L)
//            _state.update {
//                it.copy(profiles = listOf(
//                    "https://images.unsplash.com/photo-1567207674109-3222a89c833b?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxjb2xsZWN0aW9uLXBhZ2V8MTV8MjM0MjA1NHx8ZW58MHx8fHx8",
//                    "https://img.freepik.com/free-photo/businesswoman-posing-office_144627-43728.jpg",
//                    "https://www.shutterstock.com/image-photo/young-smart-smiling-professional-business-600nw-2223351507.jpg",
//                    "https://static.vecteezy.com/system/browse_category/image/45/large_People_cb1.jpg",
//                    ).map {url -> Profile(url = url) } ,isLoading = false)
//            }
//        }
//    }

    fun updatePresentedDialogsType(type: SwipeDialogType, context: PlatformContext){

        if (state.value.presentedDialogs.contains(type)) return
        _state.update {
            it.copy(
                presentedDialogs = it.presentedDialogs.toMutableList().apply { add(type) }
            )
        }
        when (type) {
            SwipeDialogType.RIGHT_SWIPE_DIALOG -> {
                onInterestedDialogShown(context)
            }
            SwipeDialogType.LEFT_SWIPE_DIALOG -> {
                onNotInterestedDialogShown(context)
            }
            SwipeDialogType.SKIP_TEMPORARY_DIALOG -> {
                onSkipTemporaryDialogShown(context)
            }
        }

    }

    fun removeItem(profile: SwipeProfileUser,swipeType: SwipeType?,context: PlatformContext){
        val images = state.value.profiles.toMutableList()
        val index = images.indexOfFirst { it.user.id == profile.user.id }

        val token = getToken(context) ?: return

        if (index == -1) return
        screenModelScope.launch {
            if (swipeType != null) {
                val id = images[index].user.id
                val result = swipeRepo.updateSwipeInterest(type = swipeType, token = token, userId = id)

              //  if (result) {
                images.removeAt(index)
                _state.update {
                    it.copy(profiles = images, skipTemporarily = false)
                }
               // }
            }else{
                images.removeAt(index)
                _state.update {
                    it.copy(profiles = images, skipTemporarily = false)
                }
            }
        }


    }

}