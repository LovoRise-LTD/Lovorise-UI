package com.lovorise.app.reels.presentation.viewModels

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import coinui.composeapp.generated.resources.Res
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.reels.domain.models.CreateReelRequest
import com.lovorise.app.reels.domain.models.FilterReelsRequest
import com.lovorise.app.reels.domain.models.MarkReelRequest
import com.lovorise.app.reels.domain.models.MyReelsResponse
import com.lovorise.app.reels.domain.models.ReelSignedUrlRequest
import com.lovorise.app.reels.domain.models.ReelStatus
import com.lovorise.app.reels.domain.models.ReelsResponse
import com.lovorise.app.reels.domain.models.ReportReel
import com.lovorise.app.reels.domain.models.UpdateReelRequest
import com.lovorise.app.reels.domain.repo.ReelsRepo
import com.lovorise.app.reels.presentation.states.ReelsApiState
import com.lovorise.app.reels.presentation.states.ReelsScreenState
import com.lovorise.app.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.component.KoinComponent

class ReelsScreenModel(private val reelsRepo: ReelsRepo) : KoinComponent, ScreenModel {

    private val _state = MutableStateFlow(ReelsApiState())
    val state = _state.asStateFlow()

    private val _reelsScreenState = MutableStateFlow(ReelsScreenState())
    val reelsScreenState = _reelsScreenState.asStateFlow()

    private var prefs:SharedPrefs? = null

    fun updateUploadingState(value: Boolean){
        _state.update {
            it.copy(
                isUploading = value
            )
        }
    }




    fun toggleVideoMutedState(){
        _reelsScreenState.update{
            it.copy(
                isVideoMuted = !reelsScreenState.value.isVideoMuted
            )
        }
    }

    fun updatePauseButtonState(value: Boolean){
        _reelsScreenState.update{
            it.copy(
                showPauseButton = value
            )
        }
    }

    fun updateVideoPlayingState(value: Boolean){
        _reelsScreenState.update{
            it.copy(
                isVideoPlaying = value
            )
        }
    }




    fun updateCurrentPosition(value: Long){
        _reelsScreenState.update{
            it.copy(
                currentPosition = value
            )
        }
    }

    fun updateSliderCurrentPosition(value: Float){
        _reelsScreenState.update{
            it.copy(
                currentSliderPos = value
            )
        }
    }

    fun updateTotalDuration(value: Long){
         _reelsScreenState.update{
            it.copy(
                totalDuration = value
            )
        }
    }


    @OptIn(ExperimentalResourceApi::class)
    fun loadSwipeUpAnimation(){
        if (reelsScreenState.value.swipeUpAnimation.isNotBlank()) return
        screenModelScope.launch {
            _reelsScreenState.update {
                it.copy(
                    swipeUpAnimation = Res.readBytes("files/swipe_up_animation.json").decodeToString()
                )
            }
        }

    }


    fun updateShowShareReelBottomSheetState(value:Boolean){
        _reelsScreenState.update {
            it.copy(
                showShareReelBottomSheet = value
            )
        }
    }



    fun updateShowReportOptionsState(value:Boolean){
        _reelsScreenState.update {
            it.copy(
                showReportOptions = value
            )
        }
    }

    fun updateShowNotInterestedOptionsState(value:Boolean){
        _reelsScreenState.update {
            it.copy(
                showNotInterestedOptions = value
            )
        }
    }

    fun updateToastMessage(value:String){
        _reelsScreenState.update {
            it.copy(
                toastMessage = value
            )
        }
    }

    fun updateShowReportReelBottomSheetState(value:Boolean){
        _reelsScreenState.update {
            it.copy(
                showReportReelBottomSheet = value
            )
        }
    }

    fun updateShowSwipeUpAnimationState(value:Boolean){
        _reelsScreenState.update {
            it.copy(
                showSwipeUpAnimation = value
            )
        }
    }

    fun getMyReels(context:PlatformContext,page:Int,onSuccess: () -> Unit = {}){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            updateLoadingState(true)
            reelsRepo.getMyReels(token,page).collectLatest { res ->
                when(res){
                    is Resource.Success -> {
                        _state.update {
                            val newList = state.value.myReels.reels?.toMutableList() ?: mutableListOf()
                            res.data?.reels?.let {reels-> newList.addAll(reels) }

                            it.copy(
                                myReels = state.value.myReels.copy(
                                    reels = newList
                                ),
                                isLoading = false
                            )
                        }
                        onSuccess()
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
//                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    fun showLoader(){
        updateLoadingState(true)
    }

    fun hideLoader(){
        updateLoadingState(false)
    }

    fun getReelsForYou(context:PlatformContext,page:Int){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.getReelsForYou(token,page).collectLatest { res ->
                when(res){
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                reelsForYou = res.data
//                                reelsForYou = ReelsResponse(
//                                    reels = listOf(
//                                        ReelsResponse.ReelDataItem(
//                                            reelId = 1,
//                                            statusId = 1,
//                                            caption = "My reels caption",
//                                            mediaType = ReelContentType.VIDEO,
//                                            content = ReelsResponse.ReelDataItem.ReelContent(
//                                                quality = "high",
//                                                mediaUrl = "https://videos.pexels.com/video-files/19179595/19179595-sd_640_360_30fps.mp4",
//                                                thumbnailUrl = ""
//                                            ),
//                                            createdAt = "",
//                                            isFavorite = false,
//                                            originalAudio = true,
//                                            userData = ReelsResponse.ReelDataItem.UserData(
//                                                name = "Test",
//                                                age = 1,
//                                                distance = 123,
//                                                city = "London",
//                                                country = "United Kingdom",
//                                                imageUrl = "https://images.unsplash.com/photo-1579353977828-2a4eab540b9a?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8c2FtcGxlfGVufDB8fDB8fHww",
//                                                verified = true
//                                            )
//                                        )
//                                    )
//                                )
                            )
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    fun updateToast(message:String){
        _state.update {
            it.copy(
                toastMessage = message
            )
        }
    }

    fun getReelsFromMatches(context:PlatformContext,page:Int){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.getReelsFromMatches(token,page).collectLatest { res ->
                when(res){
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                reelsFromMatches = res.data
                            )
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    fun getFilteredReelsForYou(context:PlatformContext,page:Int,data: FilterReelsRequest){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.filterReels(token,data,page).collectLatest { res ->
                when(res){
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                reelsForYou = res.data
                            )
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    fun getFavoriteReels(context:PlatformContext,page:Int){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.getFavoriteReels(token,page).collectLatest { res ->
                when(res){
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                favoriteReels = res.data,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                     //   updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    fun updateProcessingReel(data:ReelsApiState.ProcessingReel?){
        _state.update {
            it.copy(
                processingReel = data
            )
        }
    }

    fun createReel(context: PlatformContext,file:String,uploadData:ReelSignedUrlRequest,createData:CreateReelRequest,onSuccess:()->Unit){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return


        if (state.value.myReels.reels?.none { it?.id == createData.contentId } == true) {
            val reelItem = MyReelsResponse.MyReelItem(
                id = createData.contentId,
                statusId = null,
                createdAt = Clock.System.now().toEpochMilliseconds().toString(),
                reelsContent = MyReelsResponse.MyReelItem.ReelsContent(
                    mediaType = uploadData.mediaType,
                    mediaUrl = file
                ),
                reelsContentProcessed = emptyList(),
                updatedAt = null,
                shareCounter = 0,
                watchCounter = 0,
                privacySetting = createData.privacySetting,
                favoriteCounter = 0,
                caption = createData.caption
            )
            _state.update {
                it.copy(
                    myReels = state.value.myReels.copy(
                        reels = state.value.myReels.reels?.toMutableList()?.apply {
                            add(0, reelItem)
                        }
                    )
                )
            }
        }


        screenModelScope.launch {
            withContext(Dispatchers.IO){
                reelsRepo.getPresignedUrl(token,uploadData).collectLatest {res->
                    when(res){
                        is Resource.Error -> {updateUploadingState(false)}
                        is Resource.Loading -> {updateUploadingState(true)}
                        is Resource.Success -> {
                            val contentId = res.data?.contentId
                            val url = res.data?.presignUrl
                            if (!url.isNullOrBlank() && contentId != null) {
                                reelsRepo.uploadFile(url,file,uploadData.mediaType).collectLatest { res1->
                                    if (res1.data == true){
                                        reelsRepo.createReel(token, createData.copy(contentId = contentId)).collectLatest { res2->
                                            if (res2 is Resource.Success && res2.data?.id != null){
                                                reelsRepo.postReel(token,res2.data.id).collectLatest { res3->
                                                    println("the res3 is $res3")
                                                    when(res3){
                                                        is Resource.Error -> {updateToast("Something went wrong")}
                                                        is Resource.Loading -> {
//                                                            updateLoadingState(res3.isLoading)
                                                        }
                                                        is Resource.Success -> {
                                                            onSuccess()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    fun updateReel(context:PlatformContext,data: UpdateReelRequest){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.updateReel(token,data).collectLatest { res ->
                when(res){
                    is Resource.Success -> {}
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    fun deleteReel(context:PlatformContext,reelId:Int,onSuccess: () -> Unit){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.deleteReel(token,reelId).collectLatest { res ->
                when(res){
                    is Resource.Success -> {onSuccess()}
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    fun reportReel(context: PlatformContext,data:ReportReel){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.reportReel(token,data).collectLatest { res ->
                when(res){
                    is Resource.Success -> {
                        //if (data.markType == ReelStatus.WATCHED){ _state.update { it.copy(watchedReelsId = state.value.watchedReelsId.toMutableList().apply { add(data.reelId) }) } }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    fun markReelFavoriteUnFavorite(context: PlatformContext,reelId: Int,value: Boolean,addedMsg:String,removedMsg:String){

        val reelsForYou = state.value.reelsForYou?.reels?.toMutableList() ?: return
        val index = reelsForYou.indexOfFirst { it?.reelId == reelId}

        if (value){
            markReelFavorite(context, reelId.toString(), onSuccess = {

                _state.update {
                    it.copy(
                        reelsForYou = ReelsResponse(
                            reels = reelsForYou.apply {
                                add(index,removeAt(index)?.copy(isFavorite = value))
                            }
                        )
                    )
                }
                println("the update toast func called")
                updateToast(addedMsg)
            })

        }else{
            markReelUnFavorite(context, reelId.toString(), onSuccess = {
                _state.update {
                    it.copy(
                        reelsForYou = ReelsResponse(
                            reels = reelsForYou.apply {
                                add(index,removeAt(index)?.copy(isFavorite = value))
                            }
                        )
                    )
                }
                updateToast(removedMsg)
            })

        }


    }

    fun markMyReelFavoriteUnFavorite(context: PlatformContext,reelId: Int,value: Boolean,addedMsg:String,removedMsg:String,onSuccess: () -> Unit){
        if (value){
            markReelFavorite(context, reelId.toString(), onSuccess = onSuccess)
            updateToast(addedMsg)
        }else{
            markReelUnFavorite(context, reelId.toString(), onSuccess = onSuccess)
            updateToast(removedMsg)
        }
    }


    fun markReel(context:PlatformContext,data: MarkReelRequest){
        if (data.markType == ReelStatus.WATCHED && state.value.watchedReelsId.contains(data.reelId)) return
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.markReel(token,data).collectLatest { res ->
                when(res){
                    is Resource.Success -> {
                        if (data.markType == ReelStatus.WATCHED){ _state.update { it.copy(watchedReelsId = state.value.watchedReelsId.toMutableList().apply { add(data.reelId) }) } }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    private fun markReelFavorite(context:PlatformContext,reelId: String,onSuccess: () -> Unit){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.markReelFavorite(token,reelId).collectLatest { res ->
                when(res){
                    is Resource.Success -> {
                        onSuccess()
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }

    private fun markReelUnFavorite(context:PlatformContext,reelId: String,onSuccess: () -> Unit){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs!!.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            reelsRepo.markReelUnFavorite(token,reelId).collectLatest { res ->
                when(res){
                    is Resource.Success -> {onSuccess()}
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        updateLoadingState(res.isLoading)
                    }
                }
            }
        }
    }


//    fun updateFavoriteReels(id:Int?){
//        if (id == null) return
//        val newList = if (state.value.favoriteReelIds.contains(id)){
//            state.value.favoriteReelIds.toMutableList().apply { remove(id) }
//        }else{
//            state.value.favoriteReelIds.toMutableList().apply { add(id) }
//        }
//        _state.update {
//            it.copy(
//                favoriteReelIds = newList
//            )
//        }
//    }

//    fun isReelFavorite(id:Int?) : Boolean{
//        if (id == null) return false
//        return state.value.favoriteReelIds.contains(id)
//    }


    private fun updateLoadingState(value:Boolean){
        _state.update {
            it.copy(
                isLoading = value
            )
        }
    }





}

