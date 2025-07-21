package com.lovorise.app.settings.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.block_user_message
import coinui.composeapp.generated.resources.blocked_people
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.do_you_want_to_unblock
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.unblock
import coinui.composeapp.generated.resources.your_block_list_empty
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.BlockedUser
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class BlockedUsersScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()

        BlockedUsersScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            accountsViewModel = accountsViewModel
        )

    }
}


@Composable
fun BlockedUsersScreenContent(
    isDarkMode:Boolean,
    goBack:()->Unit,
    accountsViewModel: AccountsViewModel
) {

    var blockedUsers by remember { mutableStateOf( emptyList<BlockedUser>()) }
    val accountsState by accountsViewModel.state.collectAsState()

    val context = LocalPlatformContext.current

    LaunchedEffect(true){
        accountsViewModel.getBlockedUsers(context, onSuccess = {
            blockedUsers = it
        })
    }




    var unblockItem by remember { mutableStateOf<BlockedUser?>(null) }

    Column(modifier = Modifier) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()


        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderWithTitleAndBack(title = stringResource(Res.string.blocked_people), onBack = goBack, isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            if (blockedUsers.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(blockedUsers) { item ->
                        BlockedUserItem(
                            item = item,
                            onUnblock = {
                                unblockItem = item
                            },
                            isDarkMode = isDarkMode
                        )
                    }


                }
            }else{
                if (!accountsState.isLoading) {
                    NoBlockedUsers(isDarkMode = isDarkMode)
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

    if (unblockItem != null){
        UnBlockConfirmDialog(
            name = unblockItem?.name ?: "",
            onUnBlock = {
                unblockItem?.let {
                    accountsViewModel.unblockUser(it.id,context)
                    blockedUsers = blockedUsers.toMutableList().apply {
                        remove(it)
                    }
                }
                unblockItem = null
            },
            onCancel = {
                unblockItem = null
            },
            isDarkMode = isDarkMode
        )
    }

    if (accountsState.isLoading){
        CircularLoader(true)
    }
}

@Composable
fun BlockedUserItem(item: BlockedUser, onUnblock:()->Unit,isDarkMode: Boolean) {

    Row(
        modifier = Modifier.height(56.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = item.imageUrl,
            modifier = Modifier.size(40.dp).background(Color(0xffEAECF0), CircleShape).clip(CircleShape),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(10.dp))

        Text(
            text = item.name,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 20.sp,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff101828)
        )

        Spacer(Modifier.weight(1f))


        Box(Modifier.height(32.dp).background(Color(0xffF33358), RoundedCornerShape(50)).noRippleClickable(onUnblock), contentAlignment = Alignment.Center){
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(Res.string.unblock),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 16.sp,
                color = Color.White
            )
        }


    }
    
}

@Composable
fun NoBlockedUsers(isDarkMode: Boolean) {

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(Res.string.your_block_list_empty),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 20.sp,
            color = if (isDarkMode) Color.White else Color(0xff101828),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.block_user_message),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 20.sp,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            textAlign = TextAlign.Center
        )

    }

}



@Composable
fun UnBlockConfirmDialog(name:String,onUnBlock:()->Unit,onCancel:()->Unit, isDarkMode: Boolean) {

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp)
                .background( if (isDarkMode) CARD_BG_DARK else Color.White, shape = RoundedCornerShape(15.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                // .padding(top = 29.dp, bottom = 22.dp),
                verticalArrangement = Arrangement.Top,
                //  horizontalAlignment = Alignment.CenterStart
            ) {

                Spacer(Modifier.height(16.dp))
                Box(Modifier.size(24.dp).align(Alignment.End).noRippleClickable(onCancel), contentAlignment = Alignment.Center) {
                    Icon(
                        tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff667085),
                        imageVector = vectorResource(Res.drawable.ic_xmark),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.unblock),
                    color = if(isDarkMode) Color.White else Color(0xff101828),
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(2.dp))


                Text(
                    text = buildAnnotatedString {

                        withStyle(SpanStyle(fontWeight = FontWeight.Normal)){
                            append("${stringResource(Res.string.do_you_want_to_unblock)} ")
                        }

                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)){
                            append("$name?")
                        }

                    },
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 21.sp,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp
                )





                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)

                ) {


                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = Color((0xffD0D5DD)),
                                shape = RoundedCornerShape(40)
                            )
                            .noRippleClickable(onCancel),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = if (isDarkMode) Color.White else Color(0xff101828)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                            .background(
                                color = Color((0xffF33358)),
                                shape = RoundedCornerShape(40)
                            )
                            .noRippleClickable(onUnBlock),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.unblock),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }






                }

                Spacer(Modifier.height(16.dp))




            }







        }



    }

}

