package com.lovorise.app.chat.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.accepted_invitations
import coinui.composeapp.generated.resources.ic_empty_message
import coinui.composeapp.generated.resources.ic_empty_message_dark
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_share
import coinui.composeapp.generated.resources.invite_friends
import coinui.composeapp.generated.resources.keep_exploring
import coinui.composeapp.generated.resources.new_message
import coinui.composeapp.generated.resources.share_invite_link
import coinui.composeapp.generated.resources.when_you_match_with_more_people
import coinui.composeapp.generated.resources.your_friend
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.invite.InviteScreenModel
import com.lovorise.app.invite.InvitedListItem
import com.lovorise.app.invite.NoInvitedItem
import com.lovorise.app.libs.copy_share.shareText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.PRIMARY
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@OptIn(InternalVoyagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun NewMessageContent(isDarkMode:Boolean,chatScreenModel: ChatScreenModel,onBack:()->Unit,tabsScreenModel: TabsScreenModel,inviteScreenModel: InviteScreenModel){

    val context = LocalPlatformContext.current
    LaunchedEffect(true){
        inviteScreenModel.updateInviteFriendData(context)
    }

    val chatScreenState by chatScreenModel.state.collectAsState()


    BackHandler(true){
        onBack()
    }



    AnimatedContent(chatScreenState.showInviteFriendScreenContent, transitionSpec = {
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
            Box(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .fillMaxSize(),
            ) {
                Column(
                    Modifier.fillMaxSize().padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(47.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Box(
                            modifier = Modifier.fillMaxHeight().size(24.dp)
                                .noRippleClickable(onBack),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Icon(
                                modifier = Modifier.width(18.dp).height(14.dp),
                                imageVector = vectorResource(Res.drawable.ic_left),
                                contentDescription = "back",
                                tint = if(isDarkMode) Color.White else Color.Black
                            )

                        }

                        Text(
                            text = stringResource(Res.string.new_message),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) Color.White else Color(0xff101828)
                        )


                    }
                    NoInvitedItem(
                        text = stringResource(Res.string.invite_friends),
                        textColor = if (isDarkMode) Color.White else Color(0xff101828),
                        onClick = { chatScreenModel.updateInviteFriendScreenVisibleState(true) },
                        isDarkMode = isDarkMode
                    )
                    Spacer(Modifier.height(40.dp))

                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(
                            modifier = Modifier.width(260.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                imageVector = vectorResource(if (isDarkMode) Res.drawable.ic_empty_message_dark else Res.drawable.ic_empty_message),
                                contentDescription = null,
                                modifier = Modifier.height(115.38.dp).width(99.17.dp)
                            )

                            Spacer(Modifier.height(16.dp))


                            Text(
                                text = stringResource(Res.string.when_you_match_with_more_people),
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                letterSpacing = 0.2.sp,
                                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(16.dp))

                            ButtonWithText(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(Res.string.keep_exploring),
                                textColor = Color.White,
                                bgColor = PRIMARY,
                                onClick = {
                                    tabsScreenModel.updateTab(TabsScreenModel.BottomTab.SWIPE)
                                    chatScreenModel.updateNewMessageScreenVisibleState(false)
                                }
                            )

                            Spacer(Modifier.height(30.dp))


                        }
                    }

//
//            SearchTextField(
//                label = stringResource(Res.string.search),
//                onQueryChange = chatScreenModel::onSearch,
//                query = state.query,
//                roundedCornerPercent = 23,
//                cursorColor = SolidColor(Color(0xffF33358)),
//                isDarkMode = isDarkMode,
//                addBgInDarkMode = false
//            )
                }
            }
        }else{
            Box(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .fillMaxSize(),
            ) {
                InviteFriend(isDarkMode = isDarkMode, onBack = {
                    chatScreenModel.updateInviteFriendScreenVisibleState(false)
                }, inviteScreenModel = inviteScreenModel)
            }
        }
    }



}

@OptIn(InternalVoyagerApi::class)
@Composable
fun InviteFriend(isDarkMode:Boolean,onBack:()->Unit,inviteScreenModel: InviteScreenModel){

    val inviteScreenState by inviteScreenModel.state.collectAsState()

    val context = LocalPlatformContext.current

    BackHandler(true){
        onBack()
    }

    Box(
        modifier = Modifier
            .background(if (isDarkMode) BASE_DARK else Color.White)
            .fillMaxSize(),
    ) {

        Column(Modifier.fillMaxSize()) {
            HeaderWithTitleAndBack(
                onBack = onBack,
                title = stringResource(Res.string.invite_friends),
                isDarkMode = isDarkMode,
                addShadow = true
            )
            Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
//            NoInvitedItem(stringResource(Res.string.your_friend),textColor = if(isDarkMode) Color.White else Color(0xff101828))
                Spacer(Modifier.height(8.dp))

                ShareInviteLink(
                    isDarkMode = isDarkMode,
                    onClick = {
                        inviteScreenState.data?.inviteUrl?.let { shareText(it, context) }

                    }
                )

                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = stringResource(Res.string.accepted_invitations),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                    )

                    if (!inviteScreenState.data?.users.isNullOrEmpty()) {
                        Text(
                            text = inviteScreenState.data?.users?.size.toString(),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                        )
                    }

                }


                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    if (!inviteScreenState.data?.users.isNullOrEmpty()) {
                        items(inviteScreenState.data?.users!!) {
                            InvitedListItem(
                                name = it.name,
                                imageUrl = it.profileUrl,
                                isDarkMode = isDarkMode
                            )
                        }
                    } else {
                        item {
                            NoInvitedItem(
                                stringResource(Res.string.your_friend),
                                isDarkMode = isDarkMode
                            )
                        }
                    }


                }
            }
        }

    }

}

@Composable
fun ShareInviteLink(isDarkMode: Boolean,onClick:()->Unit) {
    Row(modifier = Modifier.fillMaxWidth().height(48.dp).background(if (isDarkMode) CARD_BG_DARK else DISABLED_LIGHT, shape = RoundedCornerShape(8.dp)).noRippleClickable(onClick), verticalAlignment = Alignment.CenterVertically) {

        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.size(32.dp).background(if (isDarkMode) Color(0xff737272) else Color(0xffF8F9FC), CircleShape), contentAlignment = Alignment.Center) {
            Icon(
                tint = if (isDarkMode) Color.White else Color(0xff344054),
                imageVector = vectorResource(Res.drawable.ic_share),
                contentDescription = null,
                modifier = Modifier.size(20.dp).align(Alignment.Center)
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text = stringResource(Res.string.share_invite_link),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp,
            color = if (isDarkMode) Color.White else Color(0xff101828)
        )

    }
}
