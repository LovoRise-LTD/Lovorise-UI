package com.lovorise.app.chat.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_nav_4
import coinui.composeapp.generated.resources.messages
import coinui.composeapp.generated.resources.search
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.chat.presentation.components.ConversationListItem
import com.lovorise.app.chat.presentation.components.EmptyConversation
import com.lovorise.app.chat.presentation.components.HeaderSection
import com.lovorise.app.chat.presentation.components.MatchesAndLikesSection
import com.lovorise.app.chat.presentation.components.NotificationSection
import com.lovorise.app.chat.presentation.components.TextWithUnreadCount
import com.lovorise.app.components.SearchTextField
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.invite.InviteScreenModel
import com.lovorise.app.libs.connectivity.ConnectivityViewModel
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.reels.presentation.reels_create_upload_view.screens.Loader
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class ChatScreen(private val navigator:Navigator,private val profileScreenModel: ProfileScreenModel,private val chatScreenModel:ChatScreenModel,private val tabsScreenModel: TabsScreenModel) : Tab {


    @Composable
    override fun Content() {

        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val inviteScreenModel = navigator.koinNavigatorScreenModel<InviteScreenModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val connectivityViewModel = koinInject<ConnectivityViewModel>()
        val accountsViewModel = koinInject<AccountsViewModel>()
        val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()
        val context = LocalPlatformContext.current
        val accountsState by accountsViewModel.state.collectAsState()
        val state by chatScreenModel.state.collectAsState()

        LaunchedEffect(true){
            chatScreenModel.onConversationClick("",context)
            if (state.users.isEmpty()){
                chatScreenModel.loadProfiles(context,accountsState.user?.id ?: "")
            }
            if (state.conversations.isEmpty() && !accountsState.user?.id.isNullOrBlank()){
                chatScreenModel.getConversations(context,accountsState.user?.id!!)
                chatScreenModel.connectToWebsocket(context)
            }
        }


        ChatScreenContent(
            isDarkMode = isDarkMode,
            chatScreenModel = chatScreenModel,
            navigateToMatches = {
                navigator.push(MatchesScreen())
            },
            navigateToLikes = {
                navigator.push(LikesScreen())
            },
            navigateToChatInboxScreen = {
                chatScreenModel.onConversationClick(it,context)
                navigator.push(ChatInboxScreen(it))
            },
            navigateToNotifications = {
                navigator.push(NotificationsScreen())
            },
            isConnected = isConnected,
            inviteScreenModel = inviteScreenModel,
            tabsScreenModel = tabsScreenModel
        )
    }

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 3u,
            title = "Chat",
            icon = painterResource(Res.drawable.ic_nav_4)
        )
}



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChatScreenContent(isDarkMode:Boolean,chatScreenModel: ChatScreenModel,navigateToMatches:()->Unit,navigateToLikes: () -> Unit,navigateToChatInboxScreen:(String)->Unit,navigateToNotifications:()->Unit,isConnected:Boolean,inviteScreenModel:InviteScreenModel,tabsScreenModel: TabsScreenModel) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    val state by chatScreenModel.state.collectAsState()
    val context = LocalPlatformContext.current




    AnimatedContent(state.showNewMessageScreenContent, transitionSpec = {
        if (!targetState) {
            // Entering: slide in from left
            // Exiting: slide out to left
            slideInHorizontally(animationSpec = tween(durationMillis = 200)) { width -> -width } with slideOutHorizontally { width -> width }
        } else {
            // Returning: slide in from right
            // Exiting: slide out to right
            slideInHorizontally(animationSpec = tween(durationMillis = 200)) { width -> width } with slideOutHorizontally { width -> -width }
        }.using(
            SizeTransform(clip = false)
        )
    }) {
        if (!it) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
            ) {

                Spacer(Modifier.height(5.dp))

                HeaderSection(
                    isDarkMode = isDarkMode,
                    isChatScreenOptionsVisible = state.isChatOptionsVisible,
                    onToggleChatScreenOptions = chatScreenModel::toggleChatOptionVisibility,
                    onNewMessage = {
                        chatScreenModel.toggleChatOptionVisibility()
                        chatScreenModel.updateNewMessageScreenVisibleState(true)
                        //navigateToNewMessageScreen()
                    },
                    onSelectChats = {
                        chatScreenModel.toggleChatOptionVisibility()
                    },
                    onReadAll = {
                        chatScreenModel.toggleChatOptionVisibility()
                    },
                    isConnected = isConnected
                )

                Spacer(Modifier.height(5.dp))

                Box(Modifier.padding(horizontal = 16.dp)) {
                    SearchTextField(
                        label = stringResource(Res.string.search),
                        onQueryChange = chatScreenModel::onSearch,
                        query = state.query,
                        roundedCornerPercent = 23,
                        cursorColor = SolidColor(Color(0xffF33358)),
                        isDarkMode = isDarkMode,
                        addBgInDarkMode = false
                    )
                }

                Spacer(Modifier.height(11.dp))

                MatchesAndLikesSection(
                    blurLike = true,
                    likes = state.users.take(10),
                    onLikesClick = {},
                    onSeeAllClick = {},
                    onLikeItemClick = { user->
                        chatScreenModel.startNewConversationIfRequired(user, context){id->
                            navigateToChatInboxScreen(id)
                        }
                    },
                    isDarkMode = isDarkMode,
                    matchesCount = 0
                )

                Spacer(Modifier.height(6.dp))

                Box(
                    Modifier.fillMaxWidth().height(41.dp).padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    TextWithUnreadCount(
                        stringResource(Res.string.messages),
                        state.conversations.sumOf {c-> c.unreadCount },
                        isDarkMode = isDarkMode
                    )
                }


//        Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
//            ConversationListItem(
//                conversation = state.teamLovorise,
//                showDivider = true,
//                onClick = {
//                    navigateToChatInboxScreen(state.teamLovorise)
//                }
//            )
//        }


                if (!state.isLoading && (state.conversations.isEmpty() || (state.searchResult.isEmpty() && state.query.isNotBlank()))) {
                    EmptyConversation(isDarkMode)
                } else {
                    if (state.isLoading){
                        CircularLoader()
                    }else{
                        LazyColumn(Modifier.padding(horizontal = 16.dp)) {
                            item {
                                NotificationSection(
                                    body = "Sent a unlock image request!",
                                    time = "Today",
                                    unreadCount = 3,
                                    onClick = navigateToNotifications,
                                    isDarkMode = isDarkMode
                                )
                            }
                            itemsIndexed((state.searchResult.ifEmpty { state.conversations }).sortedByDescending { c -> c.message?.timestamp }) { index, item ->
                                if (item.message?.formattedTimestamp?.isNotBlank() == true) {
                                    ConversationListItem(
                                        conversation = item,
                                        showDivider = index != state.conversations.lastIndex,
                                        onClick = {
                                            //  chatScreenModel.onConversationClick(item.id)
                                            navigateToChatInboxScreen(item.id)
                                        },
                                        isDarkMode = isDarkMode
                                    )
                                }
                            }
                            item {
                                Spacer(Modifier.height(40.dp))
                            }
                        }
                    }
                }
            }
        } else {
            NewMessageContent(
                isDarkMode = isDarkMode,
                chatScreenModel = chatScreenModel,
                onBack = {
                    chatScreenModel.updateNewMessageScreenVisibleState(false)
                },
                inviteScreenModel = inviteScreenModel,
                tabsScreenModel = tabsScreenModel
            )
        }
    }


}

