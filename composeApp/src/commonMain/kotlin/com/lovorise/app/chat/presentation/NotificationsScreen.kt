package com.lovorise.app.chat.presentation

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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.accept
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.decline
import coinui.composeapp.generated.resources.delete
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.notification
import coinui.composeapp.generated.resources.this_action_cannot_be_undone
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.presentation.components.RoundedImageWithStatus
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.noRippleClickable
import com.lovorise.app.settings.presentation.components.CustomDialogWithTextAndBodyAndActions
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class NotificationsScreen : Screen{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        NotificationsScreenContent(
            isDarkMode = isDarkMode,
            onBack = {
                navigator.pop()
            }
        )
    }
}

@Composable
fun NotificationsScreenContent(isDarkMode:Boolean,onBack:()->Unit) {

    val notifications = listOf(
        Notification(
            imageUrl = "https://img.freepik.com/free-photo/isolated-shot-joyful-brenette-young-cute-woman-laughs-joyfully-as-hears-funny-anecdote-from-friend_496169-2540.jpg?size=626&ext=jpg&ga=GA1.1.710363456.1728270703&semt=ais_hybrid",
            title = "Candice Wu",
            body = "Visited your profile",
            formattedTime = "3 hours ago",
            actionButtons = emptyList(),
            isOnline = false
        ),
        Notification(
            imageUrl = "https://img.freepik.com/free-photo/isolated-shot-joyful-brenette-young-cute-woman-laughs-joyfully-as-hears-funny-anecdote-from-friend_496169-2540.jpg?size=626&ext=jpg&ga=GA1.1.710363456.1728270703&semt=ais_hybrid",
            title = "Candice Wu",
            body = "Your unlock request has been accepted",
            formattedTime = "12 hours ago",
            isOnline = false,
            actionButtons = listOf(Notification.ActionButton(text = "View",{}, variant = Notification.ActionButton.ButtonVariant.ENABLED))
        ),
        Notification(
            imageUrl = "https://img.freepik.com/free-photo/isolated-shot-joyful-brenette-young-cute-woman-laughs-joyfully-as-hears-funny-anecdote-from-friend_496169-2540.jpg?size=626&ext=jpg&ga=GA1.1.710363456.1728270703&semt=ais_hybrid",
            title = "Candice Wu",
            body = "Liked on your profile",
            formattedTime = "3 hours ago",
            isOnline = false,
            actionButtons = emptyList()
        )

    )

    var showDeleteIndex by rememberSaveable { mutableIntStateOf(-1) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
    ) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth().height(55.dp).background(if (isDarkMode) BASE_DARK else Color.White).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.fillMaxHeight().size(24.dp)
                    .noRippleClickable(onBack),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    tint = if (isDarkMode) Color.White else Color.Black,
                    modifier = Modifier.width(18.dp).height(14.dp),
                    imageVector = vectorResource(Res.drawable.ic_left),
                    contentDescription = "back"
                )
            }

            Spacer(Modifier.width(16.dp))

            Text(
                text = stringResource(Res.string.notification),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                color = if (isDarkMode) Color.White else Color.Black
            )


        }

        CustomDivider(isDarkMode = isDarkMode)



        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .weight(1f),
        ) {

            LazyColumn {
                item {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "New",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 21.sp,
                        textAlign = TextAlign.Center,
                        color = if (isDarkMode) Color.White else Color(0xff000000)
                    )

                }

                itemsIndexed(notifications){i,it->
                    NotificationItem(
                        it,
                        showDeleteIcon = i == showDeleteIndex,
                        onSwipe = {
                            showDeleteIndex = i
                        },
                        onDelete = {
                            showDeleteConfirmationDialog = true
                        },
                        isDarkMode = isDarkMode
                    )
                }

                item {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = "Earlier",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 21.sp,
                        textAlign = TextAlign.Center,
                        color = if (isDarkMode) Color.White else Color(0xff000000)
                    )

                    NotificationItem(Notification(
                        imageUrl = "https://img.freepik.com/free-photo/isolated-shot-joyful-brenette-young-cute-woman-laughs-joyfully-as-hears-funny-anecdote-from-friend_496169-2540.jpg?size=626&ext=jpg&ga=GA1.1.710363456.1728270703&semt=ais_hybrid",
                        title = "Candice Wu",
                        body = "Sent a unlock image request",
                        formattedTime = "3 days ago",
                        isOnline = true,
                        actionButtons = listOf(Notification.ActionButton(text = "",{}, variant = Notification.ActionButton.ButtonVariant.ENABLED),Notification.ActionButton(text = "",{}, variant = Notification.ActionButton.ButtonVariant.DISABLED))
                    ), onSwipe = {},
                        isDarkMode = isDarkMode)


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
    if (showDeleteConfirmationDialog){
        CustomDialogWithTextAndBodyAndActions(
            onCancel = {
                showDeleteIndex = -1
                showDeleteConfirmationDialog = false
            },
            actionText2 = stringResource(Res.string.delete),
            actionText1 = stringResource(Res.string.cancel),
            body = buildAnnotatedString {
                append(stringResource(Res.string.this_action_cannot_be_undone))
            },
            title = stringResource(Res.string.delete),
            onAction1 = {
                showDeleteIndex = -1
                showDeleteConfirmationDialog = false
            },
            onAction2 = {
                showDeleteIndex = -1
                showDeleteConfirmationDialog = false
            },
            isDarkMode = isDarkMode
        )
    }
}

@Composable
fun NotificationItem(notification: Notification,showDeleteIcon:Boolean = false,onDelete:()->Unit = {},onSwipe:()->Unit,isDarkMode: Boolean) {


    var shouldReset by remember { mutableStateOf(false) }





    val dismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                shouldReset = true
                onSwipe()
            }
            false
        },
        positionalThreshold = {1f}
    )


    LaunchedEffect(shouldReset){
        if (shouldReset){
            dismissBoxState.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }


    SwipeToDismissBox(
        enableDismissFromStartToEnd = false,
        state = dismissBoxState,
        backgroundContent = {}
    ) {
        Column {
            Spacer(Modifier.height(16.dp))

            if (!showDeleteIcon) {
                Row(modifier = Modifier.fillMaxWidth()) {

                    RoundedImageWithStatus(
                        imageSize = 48.dp,
                        isOnline = notification.isOnline,
                        imageUrl = notification.imageUrl,
                        isDarkMode = isDarkMode
                    )

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = notification.title,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 21.sp,
                                letterSpacing = 0.2.sp,
                                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                            )
//                    Spacer(Modifier.width(10.dp))
//                    if (conversation.user.isVerified) {
//                        Image(
//                            imageVector = vectorResource(if (conversation.user.name == "Team Lovorise") Res.drawable.ic_verified_red else Res.drawable.ic_verified),
//                            contentDescription = null,
//                            modifier = Modifier.width(17.12.dp).height(16.67.dp)
//                        )
//                    }
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = notification.formattedTime,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                letterSpacing = 0.2.sp,
                                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                text = notification.body,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Normal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 14.sp,
                                lineHeight = 21.sp,
                                letterSpacing = 0.2.sp,
                                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                            )
                        }

                    }
                }
            }



            if (showDeleteIcon){
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(56.dp)
                        .background(if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0), RoundedCornerShape(4.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(Modifier.width(8.dp))

                    RoundedImageWithStatus(
                        imageSize = 48.dp,
                        isOnline = notification.isOnline,
                        imageUrl = notification.imageUrl,
                        isDarkMode = isDarkMode
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f).height(42.dp)) {

                        Text(
                            modifier = Modifier.weight(1f),
                            text = notification.title,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                        )

                        Text(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            text = notification.body,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                        )

                    }

                    Spacer(Modifier.width(10.dp))
                    Box(
                        Modifier.fillMaxHeight().requiredWidth(76.dp).background(
                            Color(0xffD92D20),
                            RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                        ).noRippleClickable(onDelete), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.delete),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            // lineHeight = 21.sp,
                            letterSpacing = 0.sp,
                            color = Color.White
                        )
                    }
                }
            }


            if (!showDeleteIcon) {
                if (notification.actionButtons.isNotEmpty()) {
                    Spacer(Modifier.height(5.dp))
                }

                if (notification.actionButtons.size == 1) {
                    Box(
                        modifier = Modifier
                            .padding(start = 60.dp)
                            .height(40.dp)
                            .background(Color(0xffF33358), shape = RoundedCornerShape(40))
                            .noRippleClickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 50.dp),
                            text = notification.actionButtons.first().text,
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
                if (notification.actionButtons.size > 1) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ButtonWithText(
                            text = stringResource(Res.string.accept),
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            textColor = Color.White,
                            bgColor = Color(0xffF33358)
                        )
                        ButtonWithText(
                            text = stringResource(Res.string.decline),
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            bgColor = if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                            textColor =  if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            CustomDivider(isDarkMode = isDarkMode)
        }
    }
}


data class Notification(
    val imageUrl:String,
    val title:String,
    val body:String,
    val formattedTime:String,
    val isOnline:Boolean,
    val actionButtons:List<ActionButton>
){
    data class ActionButton(
        val text:String,
        val onClick: () -> Unit,
        val variant:ButtonVariant
    ){
        enum class ButtonVariant(val textColor:Color,val bgColor:Color){
            ENABLED(textColor = Color(0xffFFFFFF), bgColor = Color(0xffF33358)),DISABLED(textColor = Color(0xff344054), bgColor = Color(0xffEAECF0))
        }
    }
}