package com.lovorise.app.chat.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.block
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.convert_to_hearts
import coinui.composeapp.generated.resources.converted_gift
import coinui.composeapp.generated.resources.delete
import coinui.composeapp.generated.resources.delete_conversation
import coinui.composeapp.generated.resources.gift_from
import coinui.composeapp.generated.resources.ic_double_checks_red
import coinui.composeapp.generated.resources.ic_move_to_bottom_inbox
import coinui.composeapp.generated.resources.message_deleted
import coinui.composeapp.generated.resources.offline
import coinui.composeapp.generated.resources.return_to_the_call
import coinui.composeapp.generated.resources.sure_want_to_block
import coinui.composeapp.generated.resources.sure_want_to_delete_messages
import coinui.composeapp.generated.resources.sure_want_to_unblock
import coinui.composeapp.generated.resources.sure_want_to_unmatch
import coinui.composeapp.generated.resources.unblock
import coinui.composeapp.generated.resources.unblock_this_user
import coinui.composeapp.generated.resources.unmatch
import coinui.composeapp.generated.resources.you_are_offline_connect_to_internet
import coinui.composeapp.generated.resources.you_matched_with
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.chat.domain.model.Conversation
import com.lovorise.app.chat.domain.model.Message
import com.lovorise.app.chat.presentation.components.ChatInboxHeader
import com.lovorise.app.chat.presentation.components.ChatInboxMoreOptionsBottomSheetContent
import com.lovorise.app.chat.presentation.components.ClearChatDialog
import com.lovorise.app.chat.presentation.components.ConvertGiftsBottomSheetContent
import com.lovorise.app.chat.presentation.components.DeleteChatDialog
import com.lovorise.app.chat.presentation.components.DeleteMessageConfirmationDialog
import com.lovorise.app.chat.presentation.components.MediaPickerBottomSheetContent
import com.lovorise.app.chat.presentation.components.MessageBox
import com.lovorise.app.chat.presentation.components.MessageReplyButton
import com.lovorise.app.chat.presentation.components.NoInternetDialog
import com.lovorise.app.chat.presentation.components.SendGiftsBottomSheetContent
import com.lovorise.app.chat.presentation.components.SendNormalMessageTextField
import com.lovorise.app.chat.presentation.components.SentReceivedDate
import com.lovorise.app.components.RewardsOverlay
import com.lovorise.app.libs.connectivity.ConnectivityViewModel
import com.lovorise.app.libs.permissions_compose.BindEffect
import com.lovorise.app.libs.permissions_compose.rememberPermissionsControllerFactory
import com.lovorise.app.lovorise_hearts.presentation.screens.TransactionHeaderMonthYear
import com.lovorise.app.noRippleClickable
import com.lovorise.app.settings.presentation.components.CustomDialogWithTextAndBodyAndActions
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import io.github.alexzhirkevich.compottie.LottieCancellationBehavior
import io.github.alexzhirkevich.compottie.LottieCompositionResult
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject


class ChatInboxScreen(private val conversationId:String) : Screen {


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountsState by accountsViewModel.state.collectAsState()

        val connectivityViewModel = koinInject<ConnectivityViewModel>()
        val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()

        val context = LocalPlatformContext.current
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)



        val chatScreenModel = navigator.koinNavigatorScreenModel<ChatScreenModel>()

        val state by chatScreenModel.state.collectAsState()

        val conversation = state.conversations.firstOrNull { it.id == conversationId } ?: return

        LaunchedEffect(true){
            chatScreenModel.onInit(controller, context)
        }

        ChatInboxScreenContent(
            isDarkMode = isDarkMode,
            onBack = {
                chatScreenModel.onCurrentTextMessageChange("")
                chatScreenModel.updateSelectedMessageIndex(-1)
                chatScreenModel.updateReplyData(null)
                chatScreenModel.removeConversationIfRequired(state.selectedConversationId)
//                chatScreenModel.onConversationClick(-1)
                navigator.pop()
            },
            chatScreenModel = chatScreenModel,
            conversation = conversation,
            navigateToCallScreen = {
                navigator.push(CallScreen(chatScreenModel,conversation))
            },
            state = state,
            isConnected = isConnected,
            accountsState = accountsState
        )

    }
}
fun Modifier.positionAwareImePadding() = composed {
    var consumePadding by remember { mutableIntStateOf(0) }
    this@positionAwareImePadding
        .onGloballyPositioned { coordinates ->
            val rootCoordinate = coordinates.findRootCoordinates()
            val bottom = coordinates.positionInWindow().y + coordinates.size.height

            consumePadding = (rootCoordinate.size.height - bottom).toInt().coerceAtLeast(0)
        }
        .consumeWindowInsets(PaddingValues(bottom = with(LocalDensity.current){ consumePadding.toDp()}))
        .imePadding()
}

@OptIn(ExperimentalMaterial3Api::class, InternalVoyagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun ChatInboxScreenContent(conversation: Conversation, isDarkMode:Boolean, onBack:()->Unit, chatScreenModel: ChatScreenModel, navigateToCallScreen:()->Unit, state: ChatScreenState, isConnected: Boolean,accountsState:AccountsApiCallState) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val msgDeleted = stringResource(Res.string.message_deleted)

    var count by remember { mutableIntStateOf(state.messages.size + 1) }

    LaunchedEffect(isConnected){
        chatScreenModel.updateNoInternetDialogState(!isConnected)
    }


     val context = LocalPlatformContext.current





    val density = LocalDensity.current
    val bottomInsets = WindowInsets.ime.getBottom(density)

   // val insets = WindowInsets.ime

    // Convert height to dp
//    val keyboardHeightDp by remember {
//        derivedStateOf {
//            with(density) { insets.getBottom(density).toDp() }
//        }
//    }

   // var requiredEmojiHeight by remember { mutableStateOf(300.dp) }

    val isKeyBoardVisible by remember {
        derivedStateOf {
            bottomInsets > 0
        }
    }
    LaunchedEffect(conversation.id){
        chatScreenModel.getMessages(conversation.id, context = context)
    }

//    LaunchedEffect(keyboardHeightDp){
//        if (keyboardHeightDp > requiredEmojiHeight){
//            requiredEmojiHeight = keyboardHeightDp
//        }
//        println("the keyboard height is $keyboardHeightDp ${with(density) { bottomInsets.toDp() }}")
//    }

    val lazyListState = rememberLazyListState()

//    LaunchedEffect(state.showEmojiPicker){
//        if (state.showEmojiPicker){
//            keyboardController?.hide()
//            focusRequester.freeFocus()
//        }
//    }
//    LaunchedEffect(keyboardHeightDp){
//        if (keyboardHeightDp >= requiredEmojiHeight && state.showEmojiPicker){
//            chatScreenModel.updateEmojiPickerState(false)
//        }
//    }

    BackHandler(true){
        onBack()
    }




    // Show the button when the last visible item is not the last item in the list
    val showMoveToBottomFab by remember {
        derivedStateOf {
            // Determine if the last visible item is not the last item in the list
            val lastVisibleItemIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex < (lazyListState.layoutInfo.totalItemsCount - 1) && !isKeyBoardVisible
        }
    }

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(state.currentInboxMessages,isKeyBoardVisible){

        if (state.currentInboxMessages.isNotEmpty()) {
            println("the state is changed")
            lazyListState.scrollToItem(state.currentInboxMessages.lastIndex)
        }
    }

    val moreOptionsBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val mediaPickerBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val giftsBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val convertGiftBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val coroutineScope = rememberCoroutineScope()


    val isBlocked by remember {
        derivedStateOf {
//            if (conversation.id != 0) {
//                state.conversations.first { it.id == conversation.id }.user.isBlocked
//            } else
            false
        }
    }

    @Composable
    fun BottomTextField(showSend:Boolean){
        Box(Modifier.fillMaxWidth().positionAwareImePadding(), contentAlignment = Alignment.BottomCenter) {
            SendNormalMessageTextField(
                value = state.currentTextMessage,
                onValueChange = chatScreenModel::onCurrentTextMessageChange,
                onSend = {
                    if(state.currentTextMessage.isNotBlank() || state.selectedMedias.isNotEmpty()) {
                        count += 1
                        chatScreenModel.sendMessage(
                            Message(
                                text = state.currentTextMessage,
                                conversationId = conversation.id,
                                replyData = state.replyData,
                                id = "${Clock.System.now().toEpochMilliseconds()}",
                                medias = state.selectedMedias,
                                senderUserId = accountsState.user?.id ?: "",
                                timestamp = Clock.System.now().toEpochMilliseconds().toString()
                            ),
                            context,
                            recipientId = conversation.user.id
                        )
                    }
                },
                focusRequester = focusRequester,
                replyData = state.replyData,
                onCancelReply = {
                    chatScreenModel.updateReplyData(null)
                },
                onEmojiClick = {
                    chatScreenModel.toggleGiftsBottomSheet()
                },
                onFocus = {
                    println("on focus manager state changed")
                    chatScreenModel.updateEmojiPickerState(false)
                },
                isDarkMode = isDarkMode,
                showSend = showSend,
                selectedMediaCount = state.selectedMedias.size,
                onShowMediaPicker = {
                    chatScreenModel.checkPermissionAndLoadGalleryItems{
                        chatScreenModel.updateMediaPickerState(true)
                    }
                }
            )
        }
      //  Spacer(Modifier.height(bottomInsets))
    }





    Box {

        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
              //  .imePadding()
                .fillMaxSize()
                .noRippleClickable {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    if(state.selectedMessageIndex != -1){
                        chatScreenModel.updateSelectedMessageIndex(-1)
                    }
                    chatScreenModel.updateEmojiPickerState(false)
                }
        )
        {


            Spacer(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .fillMaxSize()
                    .weight(1f),
            ) {

                ChatInboxHeader(
                    onBack = onBack,
                    onCall = navigateToCallScreen,
                    onMoreClick = chatScreenModel::toggleMoreOptionsBottomSheet,
                    user = conversation.user,
                    isLovorise = conversation.id == "",
                    onDeleteConversation = {
                        onBack()
                    },
                    isDarkMode = isDarkMode,
                    isConnected = isConnected
                )




                if (state.isCalling){
                    Box(Modifier.fillMaxWidth().height(28.dp).background(Color(0xff12B76A)).noRippleClickable(navigateToCallScreen), contentAlignment = Alignment.Center){
                        Text(
                            text = stringResource(Res.string.return_to_the_call),
                            fontWeight = FontWeight.Normal,
                            fontFamily = PoppinsFontFamily(),
                            fontSize = 14.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 21.sp,
                            color = Color.White
                        )
                    }
                }


                BoxWithConstraints(Modifier.weight(1f)) {

                    Column {
                        Box(Modifier.fillMaxWidth().fillMaxHeight().weight(1f)) {
                            LazyColumn(
                                Modifier.fillMaxWidth().fillMaxHeight(),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                                state = lazyListState
                            ) {
                                item {
                                    if (conversation.id.isNotBlank()) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Spacer(Modifier.height(31.dp))
                                            Box(
                                                Modifier.fillMaxWidth().height(21.dp),
                                                contentAlignment = Alignment.Center
                                            ) {

                                                Text(
                                                    text = "${stringResource(Res.string.you_matched_with)} ${conversation.user.name}",
                                                    fontWeight = FontWeight.Normal,
                                                    fontFamily = PoppinsFontFamily(),
                                                    fontSize = 14.sp,
                                                    letterSpacing = 0.2.sp,
                                                    lineHeight = 21.sp,
                                                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                                                    textAlign = TextAlign.Center
                                                )
                                            }

                                            Spacer(Modifier.height(16.dp))

                                            SentReceivedDate("7 days ago", isDarkMode = isDarkMode)

                                            Spacer(Modifier.height(14.dp))
                                        }
                                    }
                                }

                                itemsIndexed(state.currentInboxMessages) { index, item ->
                                    if (item.type == Message.Type.GIFT && item.giftData != null) {
                                        val isSent = accountsState.user?.id == item.senderUserId

                                        Gift(
                                            item,
                                            onConvertGift = chatScreenModel::updateCurrentGiftMessage,
                                            isDarkMode = isDarkMode,
                                            isSent = isSent
                                        )
                                    }


                                    if (item.type == Message.Type.TEXT) {
                                        val isSent = accountsState.user?.id == item.senderUserId

                                        MessageBox(
                                            modifier = Modifier,
                                            message = item.text,
                                            timestamp = item.formattedTimestamp,
                                            showTip = index == 0 || index == state.currentInboxMessages.lastIndex && item.text.isNotBlank(),
                                            isSent = isSent,
                                            showDoubleCheckMark = index == state.currentInboxMessages.lastIndex,
                                            separatorText = item.separatorText,
                                            onLongPress = {
                                                chatScreenModel.updateSelectedMessageIndex(index)
                                            },
                                            isSelected =  state.selectedMessageIndex == index,
                                            onClick = {
                                                if (state.selectedMessageIndex != -1) {
                                                    chatScreenModel.updateSelectedMessageIndex(-1)
                                                }
                                            },
                                            replyData = item.replyData,
                                            linkPreviewData = item.linkPreviewData,
                                            onLinkClicked = {
                                                try {
                                                    uriHandler.openUri(if (it.startsWith("https")) it else "https://$it")
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                }
                                            },
                                            onSwipe = {
                                                chatScreenModel.updateSelectedMessageIndex(index)

                                                coroutineScope.launch {
                                                    val currentMessage =
                                                        state.currentInboxMessages.getOrNull(index)

                                                    val replyData = currentMessage?.let {
                                                        Message.ReplyData(
                                                            title = if (isSent) "You" else conversation.user.name ?: "",
                                                            body = it.text
                                                        )
                                                    }
                                                    chatScreenModel.updateReplyData(replyData)
                                                    chatScreenModel.updateSelectedMessageIndex(-1)
                                                    delay(200L)
                                                    focusRequester.requestFocus()
                                                    keyboardController?.show()
                                                }
                                            },
                                            isDarkMode = isDarkMode,
                                            medias = item.medias
                                        )
                                    }




                                }

                                item {
                                    Spacer(Modifier.height(8.dp))
                                }
                            }

                            if (showMoveToBottomFab) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    Box(
                                        Modifier.fillMaxWidth().height(50.dp)
                                            .padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        MoveToBottomFab(isDarkMode = isDarkMode, onClick =  {
                                            coroutineScope.launch {
                                                lazyListState.animateScrollToItem(lazyListState.layoutInfo.totalItemsCount - 1)
                                            }
                                        })
                                    }
                                }
                            }
                        }


                        if (!isBlocked && state.selectedMessageIndex == -1) {
                            BottomTextField(false)
                        } else {
                            if (isBlocked) {

                                Box(
                                    Modifier.fillMaxWidth().height(40.dp)
                                        .background(Color(0xffEAECF0))
                                        .noRippleClickable {
                                            chatScreenModel.updateUnblockConfirmationDialogState(
                                                true
                                            )
                                        }, contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(Res.string.unblock_this_user),
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = PoppinsFontFamily(),
                                        fontSize = 16.sp,
                                        letterSpacing = 0.2.sp,
                                        lineHeight = 24.sp,
                                        color = Color(0xff101828)
                                    )
                                }
                            } else if (state.selectedMessageIndex != -1) {
                                MessageReplyButton(onClick = {

                                    coroutineScope.launch {
                                        val currentMessage =
                                            state.currentInboxMessages.getOrNull(state.selectedMessageIndex)
                                        val replyData = currentMessage?.let {
                                            val isSent = accountsState.user?.id == it.senderUserId

                                            Message.ReplyData(
                                                title = if (isSent) "You" else conversation.user.name ?: "",
                                                body = it.text
                                            )
                                        }
                                        chatScreenModel.updateReplyData(replyData)
                                        chatScreenModel.updateSelectedMessageIndex(-1)
                                        delay(200L)
                                        focusRequester.requestFocus()
                                        keyboardController?.show()
                                    }
                                }, onDelete = {
                                    chatScreenModel.updateDeleteConfirmationDialogState(true)
                                },isDarkMode = isDarkMode)
                            }
                        }
                    }

                }

//                if (state.showEmojiPicker){
//                    Box(Modifier.fillMaxWidth()
//                        .height(requiredEmojiHeight), contentAlignment = Alignment.BottomCenter){
//                        Column(Modifier.fillMaxWidth().height(requiredEmojiHeight)) {
//                            EmojiPicker(height = requiredEmojiHeight, onPicked =  {
//                                chatScreenModel.onCurrentTextMessageChange("${state.currentTextMessage}$it")
//                            })
//                        }
//                    }
//                }


                if (!isBlocked && state.selectedMessageIndex == -1) {
                    Spacer(Modifier.height(12.dp))
                }


            }
//
//        if (showEmojiPicker) {
//            CustomEmojiPicker(keyboardHeightDp)
//        }


            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .background(if (isDarkMode) BASE_DARK else Color.White)
            )
        }

        AnimatedVisibility(
            modifier = Modifier.padding(top = 20.dp).safeContentPadding().padding(horizontal = 16.dp).height(36.dp),
            visible = state.toastMsg.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            //Toast(text = "state.toastMsg")

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.height(37.dp).background(
                        Color(0xff101828).copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                    contentAlignment = Alignment.Center
                ){

                    Text(
                        modifier = Modifier.padding(horizontal = 65.dp),
                        text = state.toastMsg,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        //  lineHeight = 21.sp,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.1.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LaunchedEffect(Unit) {
                delay(2000)
                chatScreenModel.updateToastMsg("")
            }

        }

    }


    if (state.showGiftsBottomSheet) {
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = giftsBottomSheetState,
            onDismissRequest = {
                chatScreenModel.toggleGiftsBottomSheet()
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            SendGiftsBottomSheetContent(
                profileImg = conversation.user.medias?.firstOrNull()?.url ?: "",
                isDarkMode = isDarkMode,
                onSend = { gift ->
                    coroutineScope.launch {
                        giftsBottomSheetState.hide()
                    }.invokeOnCompletion {
                        chatScreenModel.toggleGiftsBottomSheet()
                        if (gift.animationRes.isNotBlank()) {
                            count += 1
                            chatScreenModel.sendMessage(
                                Message(
                                    type = Message.Type.GIFT,
                                    text = gift.comment,
                                    conversationId = conversation.id,
                                    giftData = GiftData(
                                        animationPath = gift.animationRes,
                                        senderProfile = conversation.user.medias?.firstOrNull()?.url ?: "",
                                        senderName = conversation.user.name ?: "",
                                        totalHearts = gift.hearts,
                                        isConverted = false,
                                        imageRes = gift.imageRes
                                    ),
                                    timestamp = Clock.System.now().toEpochMilliseconds().toString(),
                                    senderUserId = accountsState.user?.id ?: ""
                                ), context = context, recipientId = conversation.user.id
                            )
                        }

                    }
                }
            )
        }
    }

    if (state.showConvertGiftBottomSheet && state.currentGiftMessage != null){

        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = convertGiftBottomSheetState,
            onDismissRequest = {
                chatScreenModel.updateConvertGiftBottomSheetState(false)
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {

            ConvertGiftsBottomSheetContent(
                isDarkMode = isDarkMode,
                onCancel = {
                    coroutineScope.launch {
                        convertGiftBottomSheetState.hide()
                    }.invokeOnCompletion {
                        chatScreenModel.updateConvertGiftBottomSheetState(false)
                    }
                },
                onConvert = {msg->
                    coroutineScope.launch {
                        convertGiftBottomSheetState.hide()
                    }.invokeOnCompletion {
                        chatScreenModel.updateConvertGiftBottomSheetState(false)
                        chatScreenModel.convertGiftToHearts(msg)
                    }
                },
                message = state.currentGiftMessage
            )
        }
    }

    if (state.showRewardsOverlay && state.currentGiftMessage?.giftData?.totalHearts != null){
        RewardsOverlay(hearts = state.currentGiftMessage.giftData.totalHearts - 20, message = stringResource(Res.string.converted_gift), onClick = {
            //chatScreenModel.updateShowRewardsOverlayState(false)
            chatScreenModel.onGiftConverted()
        }, totalDuration = 4000L)
    }

    if (state.showMediaPicker){
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
           // modifier = Modifier.padding(top = 120.dp),
            sheetState = mediaPickerBottomSheetState,
            onDismissRequest = {
                chatScreenModel.updateMediaPickerState(false)
            },


            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {

            MediaPickerBottomSheetContent(
                isDarkMode = isDarkMode,
                textComposable = {
                    BottomTextField(state.showMediaPicker)
                },
                onItemClicked = chatScreenModel::onMediaGridItemClicked,
                paginationState = chatScreenModel.paginationState,
                selectedItems = state.selectedMedias,
              //  isFullyExpanded = mediaPickerBottomSheetState.hasExpandedState
            )

        }
    }

    if (state.showClearChatConfirmationDialog){
        ClearChatDialog(
            onCancel = {
                chatScreenModel.updateBlockConfirmationDialogState(false)
            },
            onClear = {
                chatScreenModel.updateBlockConfirmationDialogState(false)
            },
            isDarkMode = isDarkMode
        )
    }

    if (state.showDeleteChatConfirmationDialog){
        DeleteChatDialog(
            onCancel = {
                chatScreenModel.updateBlockConfirmationDialogState(false)
            },
            onDelete = {
                chatScreenModel.updateDeleteChatConfirmationDialogState(false)
            },
            isDarkMode = isDarkMode
        )
    }


    if (state.showMoreChatInboxOptionBottomSheet){
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = moreOptionsBottomSheetState,
            onDismissRequest = {
                chatScreenModel.toggleMoreOptionsBottomSheet()
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            ChatInboxMoreOptionsBottomSheetContent(
                isDarkMode = isDarkMode,
                onUnmatch = {
                    coroutineScope.launch {
                        moreOptionsBottomSheetState.hide()
                    }.invokeOnCompletion {
                        chatScreenModel.toggleMoreOptionsBottomSheet()
                        chatScreenModel.updateUnmatchConfirmationDialogState(true)
                    }
                },
                onReport = {

                },
                onBlock = {
                    coroutineScope.launch {
                        moreOptionsBottomSheetState.hide()
                    }.invokeOnCompletion {
                        chatScreenModel.toggleMoreOptionsBottomSheet()
                        chatScreenModel.updateBlockConfirmationDialogState(true)
                    }
                },
                onDeleteChat = {
                    coroutineScope.launch {
                        moreOptionsBottomSheetState.hide()
                    }.invokeOnCompletion {
                        chatScreenModel.toggleMoreOptionsBottomSheet()
                        chatScreenModel.updateDeleteChatConfirmationDialogState(true)
                    }
                },
                onClearChat = {
                    coroutineScope.launch {
                        moreOptionsBottomSheetState.hide()
                    }.invokeOnCompletion {
                        chatScreenModel.toggleMoreOptionsBottomSheet()
                        chatScreenModel.updateClearConfirmationDialogState(true)
                    }
                }
            )
        }
    }


    if (state.showDeleteConversationConfirmationDialog){
        CustomDialogWithTextAndBodyAndActions(
            onCancel = {
                chatScreenModel.updateDeleteConversationConfirmationDialogState(false)
            },
            actionText2 = stringResource(Res.string.delete),
            actionText1 = stringResource(Res.string.cancel),
            body = buildAnnotatedString {
                append(stringResource(Res.string.sure_want_to_delete_messages))
            },
            title = stringResource(Res.string.delete_conversation),
            onAction1 = {
                chatScreenModel.updateDeleteConversationConfirmationDialogState(false)
            },
            onAction2 = {
                chatScreenModel.updateDeleteConversationConfirmationDialogState(false)
                onBack()
            },
            isDarkMode = isDarkMode
        )
    }

    if (state.showUnblockConfirmationDialog){
        CustomDialogWithTextAndBodyAndActions(
            onCancel = {
                chatScreenModel.updateUnblockConfirmationDialogState(false)
            },
            actionText2 = stringResource(Res.string.unblock),
            actionText1 = stringResource(Res.string.cancel),
            body = buildAnnotatedString {
                append("${stringResource(Res.string.sure_want_to_unblock)} ")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(conversation.user.name)
                }
                append("?")
            },
            title = stringResource(Res.string.unblock),
            onAction1 = {
                chatScreenModel.updateUnblockConfirmationDialogState(false)
            },
            onAction2 = {
                chatScreenModel.updateUnblockConfirmationDialogState(false)
                chatScreenModel.unblockUser(conversationId = conversation.id)
            },
            isDarkMode = isDarkMode
        )
    }

    if (state.showNoInternetDialog){
        NoInternetDialog(
            title = stringResource(Res.string.offline),
            message = stringResource(Res.string.you_are_offline_connect_to_internet),
            onPositiveClick = {chatScreenModel.updateNoInternetDialogState(false)},
            isDarkMode = isDarkMode
        )
    }

    if (state.showBlockConfirmationDialog){
        CustomDialogWithTextAndBodyAndActions(
            onCancel = {
                chatScreenModel.updateBlockConfirmationDialogState(false)
            },
            actionText2 = stringResource(Res.string.block),
            actionText1 = stringResource(Res.string.cancel),
            body = buildAnnotatedString {
                append("${stringResource(Res.string.sure_want_to_block)} ")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(conversation.user.name)
                }
                append("?")
            },
            title = stringResource(Res.string.block),
            onAction1 = {
                chatScreenModel.updateBlockConfirmationDialogState(false)
            },
            onAction2 = {
                chatScreenModel.updateBlockConfirmationDialogState(false)
                chatScreenModel.blockUser(conversationId = conversation.id)
            },
            isDarkMode = isDarkMode
        )
    }

    if (state.showUnmatchConfirmationDialog){
        CustomDialogWithTextAndBodyAndActions(
            onCancel = {
                chatScreenModel.updateUnmatchConfirmationDialogState(false)
            },
            actionText2 = stringResource(Res.string.unmatch),
            actionText1 = stringResource(Res.string.cancel),
            body = buildAnnotatedString {
                append("${stringResource(Res.string.sure_want_to_unmatch)} ")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(conversation.user.name)
                }
                append("?")
            },
            title = stringResource(Res.string.unmatch),
            onAction1 = {
                chatScreenModel.updateUnmatchConfirmationDialogState(false)
            },
            onAction2 = {
                chatScreenModel.updateUnmatchConfirmationDialogState(false)
                onBack()
            },
            isDarkMode = isDarkMode
        )
    }

    if(state.showDeleteMsgConfirmationDialog){
        DeleteMessageConfirmationDialog(
            onCancel = {
                chatScreenModel.updateDeleteConfirmationDialogState(false)
            },
            onDelete = {
                chatScreenModel.updateDeleteConfirmationDialogState(false)
                state.currentInboxMessages.getOrNull(state.selectedMessageIndex)?.let {
                    chatScreenModel.deleteMessage(message = it)
                    chatScreenModel.updateToastMsg(msgDeleted)
                }
            },
            name = conversation.user.name ?: "",
            isDarkMode = isDarkMode
        )
    }




}


@Composable
fun MoveToBottomFab(onClick:()->Unit,isDarkMode: Boolean) {

    Box(
        Modifier.size(40.dp).clip(CircleShape).dropShadow(CircleShape, offsetY = 1.dp, color = Color(0xff606170).copy(alpha = 0.03f))
            .noRippleClickable(onClick)) {
        Image(
            modifier = Modifier.fillMaxSize().clip(CircleShape),
            imageVector = vectorResource(Res.drawable.ic_move_to_bottom_inbox),
            contentDescription = null,
            // contentScale = ContentScale.FillBounds
        )
    }

}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun Gift(
    message: Message,
    onConvertGift:(Message)->Unit,
    isDarkMode: Boolean,
    isSent:Boolean
) {

    val data = message.giftData ?: return

    var animation by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        animation = Res.readBytes(data.animationPath).decodeToString()
    }

    val animationComposition:LottieCompositionResult =  rememberLottieComposition {
        LottieCompositionSpec.JsonString(animation)
    }




    var reload by remember { mutableStateOf(true) }
    val painter = rememberLottiePainter(
        composition = animationComposition.value,
        iterations = 1,
        clipTextToBoundingBoxes = false,
        clipToCompositionBounds = false,
        forceOffscreenRendering = false,
        isPlaying = reload,
        cancellationBehavior = LottieCancellationBehavior.OnIterationFinish
    )

    val scope = rememberCoroutineScope()




    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(8.dp))

        Box(
            Modifier.height(30.dp)
                .background( if (isDarkMode) CARD_BG_DARK else Color(0xffF2F4F7), RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Normal,
                //  lineHeight = 18.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                text ="You have ${if (isSent) "sent" else "received"} a gift for ${data.totalHearts} Hearts",
                fontSize = 12.sp,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .width(252.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xffF33358),
                    shape = RoundedCornerShape(12.dp)
                )
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                    Box(
                        Modifier
//                            .height(119.dp)
//                            .width(130.27.dp)
//                            .size(500.dp)
                            .noRippleClickable {
                                scope.launch {
                                    reload = false
                                    delay(100L)
                                    reload = true
                                }
                            }
                    ){

                        Image(
                            modifier = Modifier
//                                .background(Color.Red)
                                .graphicsLayer {
                                    scaleX = 2f
                                    scaleY = 2f
                                },
                            painter = painter,
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "Lottie animation",
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Row(Modifier.height(33.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        fontWeight = FontWeight.Medium,
                        lineHeight = 21.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = if (isDarkMode) Color.White else Color(0xff101828),
                        text = stringResource(Res.string.gift_from),
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.width(8.dp))
                    AsyncImage(
                        modifier = Modifier.size(24.dp).clip(CircleShape),
                        model = data.senderProfile,
                        contentDescription = null,
                        clipToBounds = true,
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        fontWeight = FontWeight.Medium,
                        lineHeight = 21.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = if (isDarkMode) Color.White else Color(0xff101828),
                        text = data.senderName,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(8.dp))

                Text(
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    text = message.text,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    textAlign = TextAlign.Center
                )



                if (!message.giftData.isConverted && !isSent){
                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .background(Color(0xffF33358), shape = RoundedCornerShape(8.dp))
                            .noRippleClickable{onConvertGift(message)},
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = stringResource(Res.string.convert_to_hearts,message.giftData.totalHearts-20),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            letterSpacing = 0.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }


                    Spacer(Modifier.height(4.dp))



                }else{
                    Spacer(Modifier.height(8.dp))
                }





                Row(Modifier.fillMaxWidth().height(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        fontWeight = FontWeight.Normal,
                        lineHeight = 18.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = Color(0xffF33358),
                        text = message.formattedTimestamp,
                        fontSize = 12.sp,
                        letterSpacing = 0.2.sp,
                    )
                    Spacer(Modifier.width(4.dp))
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_double_checks_red),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )


                }


            }



        }

    }
    
}

data class GiftData(
    val animationPath:String,
    val imageRes:DrawableResource,
    val totalHearts: Int,
    val senderName:String,
    val senderProfile:String,
    val isConverted:Boolean
)

fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
//    blur: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp
) = this.drawBehind {

    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint()
    paint.color = color

//    if (blur.toPx() > 0)
//        paint.asFrameworkPaint().apply {
//           // maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
//        }
//    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}