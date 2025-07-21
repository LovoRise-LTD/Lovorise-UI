package com.lovorise.app.chat.presentation

import com.lovorise.app.MediaItem
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.accounts.presentation.signup.profile_upload.ProfileUploadScreenState.GalleryImageVideo
import com.lovorise.app.chat.domain.model.Conversation
import com.lovorise.app.chat.domain.model.Message
import com.lovorise.app.libs.permissions.PermissionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ChatScreenState(
    val users:List<UserResponse> = emptyList(),
    val conversations : List<Conversation> = emptyList(),
    val query : String = "",
    val searchResult: List<Conversation> = emptyList(),
    val currentTextMessage:String = "",
    val messages: HashMap<String,List<Message>> = hashMapOf(),
    val currentInboxMessages:List<Message> = emptyList(),
    val showMoreChatInboxOptionBottomSheet:Boolean = false,
    val showDeleteConversationConfirmationDialog:Boolean = false,
    val showUnmatchConfirmationDialog:Boolean = false,
    val showBlockConfirmationDialog:Boolean = false,
    val showUnblockConfirmationDialog:Boolean = false,
    val isRinging:Boolean = true,
    val isCalling:Boolean = false,
    val ongoingCallTime:Flow<String> = flow {  },
    val isMicDisabled:Boolean = false,
    val isSpeakerDisabled:Boolean = false,
  //  val showReplyButton:Boolean = false,
    val selectedMessageIndex:Int = -1,
    val replyData: Message.ReplyData? = null,
    val showEmojiPicker:Boolean = false,
    val showGiftsBottomSheet:Boolean = false,
    val selectedConversationId:String = "",
    val showDeleteMsgConfirmationDialog:Boolean = false,
    val toastMsg:String = "",
    val showConvertGiftBottomSheet:Boolean = false,
    val currentGiftMessage: Message? = null,
    val showRewardsOverlay:Boolean = false,
    val isChatOptionsVisible:Boolean = false,
    val showNoInternetDialog:Boolean = false,
    val showNewMessageScreenContent:Boolean = false,
    val showInviteFriendScreenContent:Boolean = false,
    val showMediaPicker:Boolean = false,
    val galleryPermissionState:PermissionState = PermissionState.NotDetermined,
    val galleryMedias: List<GalleryImageVideo> = emptyList(),
    val selectedMedias: List<MediaItem> = emptyList(),
    val selectionCounter : Int = 0,
    val isLoading:Boolean = false
   // val selectedEmoji:String
)