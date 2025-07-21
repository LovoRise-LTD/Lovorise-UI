package com.lovorise.app.chat.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import com.lovorise.app.MediaItem
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.chat.domain.ChatRepo
import com.lovorise.app.chat.domain.model.Conversation
import com.lovorise.app.chat.domain.model.Message
import com.lovorise.app.chat.domain.model.SendMessage
import com.lovorise.app.chat.link_og_tag.LinkPreviewDataService
import com.lovorise.app.getImagesAndVideos
import com.lovorise.app.libs.permissions.DeniedAlwaysException
import com.lovorise.app.libs.permissions.DeniedException
import com.lovorise.app.libs.permissions.Permission
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions.PermissionsController
import com.lovorise.app.libs.permissions.RequestCanceledException
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.util.Resource
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

class ChatScreenModel(private val chatRepo:ChatRepo) : ScreenModel {

    private val _state = MutableStateFlow(ChatScreenState())
    val state = _state.asStateFlow()

    private val linkPreviewDataService by  lazy { LinkPreviewDataService()}

    private var controller: PermissionsController? = null
    private var context: PlatformContext? = null

    private var prefs:SharedPrefs? = null

    private val onMessage:(String)->Unit = {
        println("onMessage the message is $it")
    }

    fun toggleChatOptionVisibility(){
        _state.update {
            it.copy(
                isChatOptionsVisible = !state.value.isChatOptionsVisible
            )
        }
    }

    val paginationState = PaginationState<Int, MediaItem>(
        initialPageKey = 1,
        onRequestPage = { loadPage(it) }
    )

    fun onInit(controller: PermissionsController?, context: PlatformContext){
        if (this.controller != null) return
        if (controller != null) {
            this.controller = controller
        }
        this.context = context
        checkIfGalleryPermissionGranted()
    }

    fun connectToWebsocket(context: PlatformContext){
        val token = getToken(context) ?: return
        chatRepo.connect(
            token = token,
            onDisconnected = {
                println("On socket disconnected!!")
            },
            onConnected = {
                println("On socket connected!!")
            },
            onError = {
                println("On Error !!")
            },
            onMessage = onMessage
        )
    }

    private fun loadPage(page:Int){
        if (context == null) throw Exception("Without context page cannot be loaded")
        screenModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val items = getImagesAndVideos(context!!, pageNumber = page, pageSize = 20)
                    paginationState.appendPage(
                        items = items,
                        isLastPage = items.isEmpty(),
                        nextPageKey = page + 1
                    )
                }catch (e:Exception){
                    e.printStackTrace()
                    //LoadResult.Error(e)
                }

            }
        }
    }

    private fun checkIfGalleryPermissionGranted(){
        screenModelScope.launch {
            _state.update {
                it.copy(
                    galleryPermissionState = if (controller?.isPermissionGranted(Permission.GALLERY) == true) PermissionState.Granted else PermissionState.NotDetermined
                )
            }
        }

    }

    fun checkPermissionAndLoadGalleryItems(openSheet:()->Unit){
        if (state.value.galleryPermissionState == PermissionState.Granted){
            retryFetching()
            openSheet()
        }else{
            provideOrRequestGalleryPermission(openSheet)
        }
    }

    private fun retryFetching(){
        if (paginationState.allItems.isNullOrEmpty()){
            paginationState.refresh(1)
        }
    }


    private fun provideOrRequestGalleryPermission(openSheet: () -> Unit){
        if (state.value.galleryPermissionState != PermissionState.NotDetermined){
            _state.update {
                it.copy(galleryPermissionState = PermissionState.NotDetermined)
            }
        }
        screenModelScope.launch {
            val permissionState =  try {
                controller?.providePermission(Permission.GALLERY)
                PermissionState.Granted
            } catch(e: DeniedAlwaysException) {
                PermissionState.DeniedAlways
            } catch(e: DeniedException) {
                PermissionState.Denied
            } catch(e: RequestCanceledException) {
                PermissionState.NotDetermined
            }
            if (permissionState == PermissionState.Granted){
                openSheet()
            }
            _state.update {
                it.copy(
                    galleryPermissionState = permissionState
                )
            }


        }
    }


    fun updateMediaPickerState(value: Boolean){
        _state.update {
            it.copy(
                showMediaPicker = value
            )
        }
    }

    private fun resetMediaPickerState(){
        _state.update {
            it.copy(
                selectionCounter = 0,
                selectedMedias = emptyList(),
                showMediaPicker = false
            )
        }
    }


    fun onMediaGridItemClicked(index: Int){
        val media = (paginationState.allItems ?: emptyList()).getOrNull(index) ?: return

        val selectedMedia = state.value.selectedMedias.firstOrNull{ it.id == media.id}

        val selectedMedias = state.value.selectedMedias.toMutableList().apply {
            if (selectedMedia != null){
                remove(selectedMedia)
            }else{
                add(media.copy(selectionIndex = state.value.selectionCounter + 1))
            }
        }

        _state.update {
            it.copy(
                selectedMedias = selectedMedias,
                selectionCounter = state.value.selectionCounter + 1
            )
        }
    }


    fun updateNewMessageScreenVisibleState(value: Boolean){
        _state.update {
            it.copy(
                showNewMessageScreenContent = value
            )
        }
    }

    fun updateInviteFriendScreenVisibleState(value: Boolean){
        _state.update {
            it.copy(
                showInviteFriendScreenContent = value
            )
        }
    }

    fun updateNoInternetDialogState(value:Boolean){
        _state.update {
            it.copy(
                showNoInternetDialog = value
            )
        }
    }

    fun onSearch(query:String){

        val newResults = if (query.isNotBlank()){ state.value.conversations.filter {
            it.user.name?.lowercase()?.contains(query.lowercase()) == true
        }} else emptyList()

        _state.update {
            it.copy(
                query = query,
                searchResult = newResults
            )
        }
    }

    fun onConversationClick(id: String,context: PlatformContext){
        _state.update {
            it.copy(
                selectedConversationId = id,
                currentInboxMessages = state.value.messages.getOrElse(id){ emptyList() }
            )
        }
        if (id.isNotBlank()) {
            getMessages(id, context)
        }
    }

    fun onCurrentTextMessageChange(value:String){
        _state.update {
            it.copy(
                currentTextMessage = value
            )
        }
    }

    fun toggleMicState(){
        _state.update {
            it.copy(
                isMicDisabled = !it.isMicDisabled
            )
        }
    }

    fun toggleSpeakerState(){
        _state.update {
            it.copy(
                isSpeakerDisabled = !it.isSpeakerDisabled
            )
        }
    }

    fun toggleMoreOptionsBottomSheet(){
        _state.update {
            it.copy(
                showMoreChatInboxOptionBottomSheet = !it.showMoreChatInboxOptionBottomSheet
            )
        }
    }

    fun toggleGiftsBottomSheet(){
        _state.update {
            it.copy(
                showGiftsBottomSheet = !it.showGiftsBottomSheet
            )
        }
    }

    fun updateDeleteConversationConfirmationDialogState(value:Boolean){
        _state.update {
            it.copy(
                showDeleteConversationConfirmationDialog = value
            )
        }
    }

    fun updateBlockConfirmationDialogState(value: Boolean){
        _state.update {
            it.copy(
                showBlockConfirmationDialog = value
            )
        }
    }

    fun updateUnblockConfirmationDialogState(value: Boolean){
        _state.update {
            it.copy(
                showUnblockConfirmationDialog = value
            )
        }
    }

    fun updateUnmatchConfirmationDialogState(value: Boolean){
        _state.update {
            it.copy(
                showUnmatchConfirmationDialog = value
            )
        }
    }


    fun updateSelectedMessageIndex(index:Int){
        _state.update {
            it.copy(
                selectedMessageIndex = index
            )
        }
    }

    fun onCallEnded(){
        _state.update {
            it.copy(
                ongoingCallTime = flow {  },
                isCalling = false,
                isRinging = true
            )
        }
    }

    fun loadProfiles(context: PlatformContext,userId: String){
        val token = getToken(context) ?: return
        screenModelScope.launch {
            chatRepo.getAllProfiles(token).collectLatest { res->
                println("the all profile res ${res.data}")
                if (res is Resource.Success && res.data != null){
                    _state.update {
                        it.copy(
                            users = res.data.filter {user-> !user.name.isNullOrBlank() && !user.medias.isNullOrEmpty() && user.id != userId }
                        )
                    }
                }
            }
        }
    }

    fun getConversations(context: PlatformContext,userId:String){
        if (state.value.isLoading) return
        val token = getToken(context) ?: return
        screenModelScope.launch {
            chatRepo.getConversations(token, userId = userId).collectLatest { res->
                if (res is Resource.Success && res.data != null){
                    val conversations = res.data.map { it.copy(
                        message = it.message?.copy(
                            formattedTimestamp = getDayLabel(it.message.timestamp)
                        )
                    ) }
                    _state.update {
                        it.copy(
                            conversations = conversations
                        )
                    }
                }

                if (res is Resource.Loading){
                    _state.update {
                        it.copy(
                            isLoading = res.isLoading
                        )
                    }
                }
            }
        }
    }

    fun onCalledPicked(){

        _state.update {

            var elapsedSeconds = 0
            it.copy(
                ongoingCallTime = flow {
                    while (elapsedSeconds <= 10*60) {
                        val minutes = elapsedSeconds / 60
                        val seconds = elapsedSeconds % 60
                        // Emit the formatted time using string templates
                        emit("${if (minutes < 10) "0" else ""}$minutes:${if (seconds < 10) "0" else ""}$seconds")
                        delay(1000L) // Wait for 1 second
                        elapsedSeconds++
                    }
                },
                isCalling = true,
                isRinging = false
            )
        }
    }

    fun blockUser(conversationId: String){
        val conversations = mutableListOf<Conversation>()
        state.value.conversations.forEach { conversation ->
            if (conversation.id == conversationId){
                conversations.add(conversation.copy(unreadCount = 0, isBlocked = true))
            }else{
                conversations.add(conversation.copy(unreadCount = 0))
            }
        }
        _state.update {
            it.copy(
                conversations = conversations
            )
        }

    }

    fun unblockUser(conversationId: String){
        val conversations = mutableListOf<Conversation>()
        state.value.conversations.forEach { conversation ->
            if (conversation.id == conversationId){
                conversations.add(conversation.copy(unreadCount = 0, isBlocked = false))
            }else{
                conversations.add(conversation.copy(unreadCount = 0))
            }
        }
        _state.update {
            it.copy(
                conversations = conversations
            )
        }

    }

    fun updateEmojiPickerState(value: Boolean){
        _state.update {
            it.copy(
                showEmojiPicker = value
            )
        }
    }

    fun removeConversationIfRequired(id: String){
        val conversation = state.value.conversations.first { it.id == id }
        if (conversation.message?.formattedTimestamp?.isBlank() == true){
            _state.update{
                it.copy(
                    conversations = state.value.conversations.toMutableList().apply {
                        remove(conversation)
                    }
                )
            }
        }
    }

    fun startNewConversationIfRequired(user: UserResponse,context: PlatformContext,navigate:(String)->Unit){
        val existingConversation = state.value.conversations.firstOrNull { it.user == user }

        println("the existing Conversation is $existingConversation")
        if (existingConversation != null){
            onConversationClick(existingConversation.id,context)
            return
        }

        val token = getToken(context) ?: return

        screenModelScope.launch {
            chatRepo.createOrGetConversationId(token = token, receiverUserId = user.id).collectLatest { res->
                when(res){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val id = res.data
                        if (!id.isNullOrBlank()){
                            _state.update{
                                it.copy(
                                    conversations = state.value.conversations.toMutableList().apply {
                                        add(
                                            Conversation(
                                                id = id,
                                                user = user,
                                                unreadCount = 0,
                                                isOnline = true,
                                                message = null,
                                                unreadUserId = null
                                            )
                                        )
                                    },
                                )
                            }
                            navigate(id)
                          //  onConversationClick(id)
                        }

                    }
                }
            }

        }

    }

    private fun getToken(context: PlatformContext):String?{
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        return prefs!!.getString(PreferencesKeys.AUTH_TOKEN)
    }

    fun updateReplyData(data: Message.ReplyData?){
        _state.update {
            it.copy(
                replyData = data
            )
        }

    }

    fun updateToastMsg(value: String){
        _state.update{
            it.copy(
                toastMsg = value
            )
        }
    }


    fun loadPreviewIfRequired(item: Message, index: Int) {

        if (item.linkPreviewData != null) return
        val urls = LinkPreviewDataService.extractUrls(item.text)

        screenModelScope.launch {

            val previewData = if(urls.isNotEmpty()) linkPreviewDataService.getLinkPreviewData(urls.first()) else null

//            _state.update {
//                it.copy(
//                    messages = it.messages.toMutableList().apply {
//                        add(index,removeAt(index).copy(previewProcessed = true, linkPreviewData = previewData))
//                    }
//                )
//            }

            println("the urls 2 is $previewData")
        }


    }

    fun updateDeleteConfirmationDialogState(value: Boolean){
        _state.update{
            it.copy(
                showDeleteMsgConfirmationDialog = value
            )
        }
    }

    fun deleteMessage(message: Message){
        if (state.value.selectedMessageIndex < 0) return
     //   val newList = state.value.messages.toMutableList()
     //   newList.remove(message)
//        _state.update {
//            it.copy(
//                messages = newList,
//                replyData = null,
//                selectedMessageIndex = -1
//            )
//        }

       // getMessages(message.conversationId)
    }

    fun updateCurrentGiftMessage(value: Message){
        _state.update{
            it.copy(
                currentGiftMessage = value,
                showConvertGiftBottomSheet = true
            )
        }
    }

    fun onGiftConverted(){
        _state.update{
            it.copy(
                currentGiftMessage = null,
                showRewardsOverlay = false,
            )
        }
    }


    fun convertGiftToHearts(message: Message){

//        val index = state.value.messages.indexOfFirst { it.conversationId == message.conversationId && message.id == it.id }
//
//
//        println("the gift trying to get converted is $message ")
//
//        if (index == -1) return
//
//
//
//        _state.update {
//            it.copy(
//                messages = it.messages.toMutableList().apply {
//                    add(index,removeAt(index).copy(giftData = message.giftData?.copy(isConverted = true)))
//                },
//                currentTextMessage = "",
//                replyData = null,
//               // currentGiftMessage = null,
//                showRewardsOverlay = true
//            )
//        }
//
//        getMessages(message.conversationId)
    }

    fun updateConvertGiftBottomSheetState(value: Boolean){
        _state.update{
            it.copy(
                showConvertGiftBottomSheet = value
            )
        }
    }

    fun updateShowRewardsOverlayState(value: Boolean){
        _state.update{
            it.copy(
                showRewardsOverlay = value
            )
        }
    }


    fun sendMessage(message: Message,context: PlatformContext,recipientId:String){
        screenModelScope.launch {

            val updatedMessage = message.copy(
                formattedTimestamp = if (message.type == Message.Type.TEXT)
                    getFormattedTime(message.timestamp)
                else
                    getDayMonthYear(message.timestamp)
            )

            chatRepo.sendMessage(SendMessage(conversationID = updatedMessage.conversationId, recipientID = recipientId, message = message.text))
           // onMessage.invoke(message.text)

            _state.update {
                val messagesList = it.messages
                    .getOrPut(message.conversationId) { emptyList() }
                    .toMutableList()
                    .apply {
                        add(updatedMessage)
                    }
                    .sortedBy {m -> m.timestamp } // Ensure chronological order

                val newMessagesMap = it.messages.apply {
                    this[message.conversationId] = messagesList
                }

                val groupedWithSeparators = groupMessagesWithSeparators(messagesList)

                it.copy(
                    messages = newMessagesMap,
                    currentInboxMessages = groupedWithSeparators,
                    currentTextMessage = "",
                    replyData = null
                )
            }


           // val urls = LinkPreviewDataService.extractUrls(message.text)

//            val previewData = if (urls.isNotEmpty()) {
//                linkPreviewDataService.getLinkPreviewData(urls.first())
//            }else null
//
//            _state.update {
//                val index = it.messages.lastIndex
//                it.copy(
//                    messages = it.messages.toMutableList().apply {
//                        add(
//                            index,
//                            removeAt(index).copy(previewProcessed = true, linkPreviewData = previewData)
//                        )
//                    }
//                )
//            }


            val conversations = mutableListOf<Conversation>()


            state.value.conversations.forEach { conversation ->
                if (conversation.id == message.conversationId) {
                    conversations.add(
                        conversation.copy(
                            unreadCount = 0,
                            message = message.copy(formattedTimestamp = getFormattedTime1(message.timestamp))
                        )
                    )
                } else {
                    conversations.add(conversation.copy(unreadCount = 0))
                }
            }

            _state.update {
                it.copy(
                    conversations = conversations
                )
            }
            resetMediaPickerState()
         //   getMessages(message.conversationId,context)
        }
    }



    fun getMessages(conversationId:String,context: PlatformContext){

        if (state.value.isLoading) return

        println("the conversationId : $conversationId")
        val messages = state.value.messages.getOrElse(conversationId){emptyList()}.sortedBy { it.timestamp }

        _state.update {
            it.copy(
                currentInboxMessages = messages
            )
        }

        if (messages.isNotEmpty()){
            return
        }

        println("Reached here")


        val token = getToken(context) ?: return

        screenModelScope.launch {
            chatRepo.getMessagesForConversation(token = token, conversationId = conversationId).collectLatest { res ->
                when(res){
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = res.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        val fetchedMessages = (res.data ?: emptyList()).map{
                            it.copy(
                                formattedTimestamp = if (it.type == Message.Type.TEXT) getFormattedTime(it.timestamp) else getDayMonthYear(it.timestamp),
                            )
                        }.sortedBy { it.timestamp }
                        val messagesWithSeparators = groupMessagesWithSeparators(fetchedMessages)

                        _state.update {
                            it.copy(
                                messages = it.messages.apply {
                                    this[conversationId] = messagesWithSeparators
                                },
                                currentInboxMessages = messagesWithSeparators
                            )
                        }
                    }
                }
            }

        }


      //  val conversations = mutableListOf<Conversation>()


//        state.value.conversations.forEach { conversation ->
//            if (conversation.id == conversationId){
//                conversations.add(conversation.copy(unreadCount = 0))
//            }else{
//                conversations.add(conversation)
//            }
//        }
    }


    private fun groupMessagesWithSeparators(messages: List<Message>): List<Message> {
        val result = mutableListOf<Message>()
        var lastDate: String? = null

        for (message in messages) {
            val dateLabel = getDayLabel(message.timestamp)

            // Only insert separator if this date is different from the previous one
            if (dateLabel != lastDate) {
                result.add(
                    message.copy(
                        text = "",
                        separatorText = dateLabel
                    )
                )
                lastDate = dateLabel
            }

            // Always clear separatorText from the actual message
            result.add(message.copy(separatorText = ""))
        }

        return result
    }

    private fun getDayMonthYear(timestamp: String) : String{
        // Convert the timestamp to an Instant
        val instant = if (timestamp.toLongOrNull() != null) Instant.fromEpochMilliseconds(timestamp.toLong()) else Instant.parse(timestamp)

        // Convert the Instant to LocalDateTime using the system's default timezone
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date


        val formattedTime = "${date.dayOfMonth} ${getMonthAbbreviation(date.month.name)} ${date.year}"

        return formattedTime
    }


    private fun getDayLabel(timestamp: String): String {
        val instant = if (timestamp.toLongOrNull() != null) Instant.fromEpochMilliseconds(timestamp.toLong()) else Instant.parse(timestamp)

        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        return when (date) {
            currentDate -> "Today"
            currentDate.minus(DatePeriod(days = 1)) -> "Yesterday"
            else -> {
                val month = date.month.getDisplayName()
                val day = date.dayOfMonth
                if (date.year != currentDate.year) {
                    "$day $month ${date.year}" // Include year
                } else {
                    "$day $month" // Omit year
                }
            }
        }
    }


    private fun Month.getDisplayName(): String {
        return when (this) {
            Month.JANUARY -> "Jan"
            Month.FEBRUARY -> "Feb"
            Month.MARCH -> "Mar"
            Month.APRIL -> "Apr"
            Month.MAY -> "May"
            Month.JUNE -> "June"
            Month.JULY -> "July"
            Month.AUGUST -> "Aug"
            Month.SEPTEMBER -> "Sept"
            Month.OCTOBER -> "Oct"
            Month.NOVEMBER -> "Nov"
            Month.DECEMBER -> "Dec"
            else -> ""
        }
    }

    private fun getMonthAbbreviation(month: String): String {
        val months = mapOf(
            "JANUARY" to "Jan", "FEBRUARY" to "Feb", "MARCH" to "Mar", "APRIL" to "Apr",
            "MAY" to "May", "JUNE" to "Jun", "JULY" to "Jul", "AUGUST" to "Aug",
            "SEPTEMBER" to "Sep", "OCTOBER" to "Oct", "NOVEMBER" to "Nov", "DECEMBER" to "Dec"
        )
        return months[month.uppercase()] ?: "Invalid Month"
    }

    // 12:00pm
    private fun getFormattedTime(timestamp:String) : String{

        // Convert the timestamp to an Instant
        val instant = if (timestamp.toLongOrNull() != null) Instant.fromEpochMilliseconds(timestamp.toLong()) else Instant.parse(timestamp)

        // Convert the Instant to LocalDateTime using the system's default timezone
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        // Extract hour and minute from LocalDateTime
        val hour = dateTime.hour
        val minute = dateTime.minute

        // Determine AM or PM
        val period = if (hour < 12) "am" else "pm"

        // Convert to 12-hour format
        val formattedHour = if (hour % 12 == 0) 12 else hour % 12

        // Format the time as hh:mm AM/PM using string interpolation
        val formattedTime = "${formattedHour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}$period"

        return formattedTime

    }

    // 12:00 PM
    private fun getFormattedTime1(timestamp:String) : String{

//        val dateTime = LocalDateTime.parse(timestamp)

        // Convert the timestamp to an Instant
        val instant = if (timestamp.toLongOrNull() != null) Instant.fromEpochMilliseconds(timestamp.toLong()) else Instant.parse(timestamp)
//
//        // Convert the Instant to LocalDateTime using the system's default timezone
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        // Extract hour and minute from LocalDateTime
        val hour = dateTime.hour
        val minute = dateTime.minute

        // Determine AM or PM
        val period = if (hour < 12) "AM" else "PM"

        // Convert to 12-hour format
        val formattedHour = if (hour % 12 == 0) 12 else hour % 12

        // Format the time as hh:mm AM/PM using string interpolation
        val formattedTime = "${formattedHour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $period"

        return formattedTime

    }

    fun getCurrentMonthAndDate(): String {
        // Get the current date
        val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

        // Extract month and day
        val month = currentDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3) // "Sep"
        val day = currentDate.dayOfMonth

        // Format as "Sep 3"
        return "$month $day"
    }




}