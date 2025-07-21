package com.lovorise.app.invite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.accepted_invitations
import coinui.composeapp.generated.resources.copied_link_to_clipboard
import coinui.composeapp.generated.resources.copy_link
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.invite_and_get_rewards
import coinui.composeapp.generated.resources.invite_friends
import coinui.composeapp.generated.resources.share_link
import coinui.composeapp.generated.resources.your_friend
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.chat.presentation.ShareInviteLink
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.DropShadow
import com.lovorise.app.components.Toast
import com.lovorise.app.libs.copy_share.copyToClipboard
import com.lovorise.app.libs.copy_share.shareText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class InviteFriendsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val screenModel = navigator.koinNavigatorScreenModel<InviteScreenModel>()

        val state by screenModel.state.collectAsState()
        val context = LocalPlatformContext.current

        LaunchedEffect(true){
            screenModel.updateInviteFriendData(context)
        }

        InviteFriendsScreenContent(
            onBack = {
                navigator.pop()
            },
            isDarkMode = isDarkMode,
            state = state
        )
    }
}

@Composable
fun InviteFriendsScreenContent(onBack:()->Unit,isDarkMode:Boolean,state: InviteScreenState) {

    val copyClipboardMsg = stringResource(Res.string.copied_link_to_clipboard)
    var toastMessage by rememberSaveable { mutableStateOf("") }
    val context = LocalPlatformContext.current


    Column(
        modifier = Modifier.fillMaxSize()
            .background(if (isDarkMode) BASE_DARK else Color.White)
    ) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()

        )
        ConnectivityToast()

        Row(
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier.fillMaxHeight().size(24.dp)
                        .noRippleClickable { onBack() },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        tint = if (isDarkMode) Color.White else Color(0xff101828),
                        modifier = Modifier.width(16.dp).height(12.dp),
                        imageVector = vectorResource(Res.drawable.ic_left),
                        contentDescription = "back"
                    )
                }

                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(Res.string.invite_friends),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828)
                    )
                }


            }


        }

        DropShadow()

        Column(
            modifier = Modifier.fillMaxSize()
                .weight(1f)
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.invite_and_get_rewards),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )


            Spacer(modifier = Modifier.height(16.dp))

            ShareInviteLink(
                isDarkMode = isDarkMode,
                onClick = {
                    state.data?.inviteUrl?.let { shareText(it, context) }
                }
            )

            Spacer(modifier = Modifier.height(23.dp))

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

                if(!state.data?.users.isNullOrEmpty()){
                    Text(
                        text = state.data?.users?.size.toString(),
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

                if (!state.data?.users.isNullOrEmpty()){
                    items(state.data?.users!!){
                        InvitedListItem(name = it.name, imageUrl = it.profileUrl,isDarkMode = isDarkMode)
                    }
                }else{
                    item {
                        NoInvitedItem(stringResource(Res.string.your_friend), isDarkMode = isDarkMode)
                    }
                }



            }


        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )

    }

    if (state.isLoading){
        CircularLoader(true)
    }

    AnimatedVisibility(
        modifier = Modifier.padding(top = 70.dp).padding(horizontal = 60.dp).height(36.dp),
        visible = toastMessage.isNotBlank(),
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Toast(text = toastMessage)

        LaunchedEffect(Unit) {
            delay(2000) // Hide after 2 seconds
            toastMessage = ""
        }

    }
}


