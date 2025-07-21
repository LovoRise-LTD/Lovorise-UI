package com.lovorise.app.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.and_auto_renew_unless_app_store_settings
import coinui.composeapp.generated.resources.one_time_payment
import coinui.composeapp.generated.resources.and_auto_renew_unless_google_play
import coinui.composeapp.generated.resources.and_pay_once_for_one_week
import coinui.composeapp.generated.resources.by_continuing_to_lovorise_premium
import coinui.composeapp.generated.resources.get_one_month_premium
import coinui.composeapp.generated.resources.get_one_week_premium
import coinui.composeapp.generated.resources.ic_ads_free_enabled
import coinui.composeapp.generated.resources.ic_advance_filters
import coinui.composeapp.generated.resources.ic_anonymous_mode
import coinui.composeapp.generated.resources.ic_checked_filled
import coinui.composeapp.generated.resources.ic_control_privacy
import coinui.composeapp.generated.resources.ic_cross_white
import coinui.composeapp.generated.resources.ic_lovorise_premium
import coinui.composeapp.generated.resources.ic_travel_ticket_feature
import coinui.composeapp.generated.resources.ic_unchecked_circle
import coinui.composeapp.generated.resources.ic_unlimited_backtrack
import coinui.composeapp.generated.resources.ic_unlimited_swipes
import coinui.composeapp.generated.resources.ic_who_liked
import coinui.composeapp.generated.resources.ic_who_viewed
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.keep_exploring
import coinui.composeapp.generated.resources.month_cap
import coinui.composeapp.generated.resources.monthly
import coinui.composeapp.generated.resources.monthly_member_plan_message
import coinui.composeapp.generated.resources.subscription
import coinui.composeapp.generated.resources.subscription_terms
import coinui.composeapp.generated.resources.subscriptions_feature_descriptions
import coinui.composeapp.generated.resources.subscriptions_feature_titles
import coinui.composeapp.generated.resources.unlock_essential_features
import coinui.composeapp.generated.resources.week
import coinui.composeapp.generated.resources.weekly
import coinui.composeapp.generated.resources.weekly_member_plan_message
import com.lovorise.app.InAppPurchase
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.chat.presentation.LikesScreen
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.iap.AppleReceiptData
import com.lovorise.app.libs.iap.GoogleReceiptData
import com.lovorise.app.libs.iap.PurchaseProductItem
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.components.TryNowButton
import com.lovorise.app.profile_visitors.ProfileVisitorsScreen
import com.lovorise.app.settings.presentation.screens.travel_ticket.TravelTicketScreen
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

data class PurchaseSubscriptionScreen(private val subscriptionType:SubscriptionType) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val accountsScreenModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val profileScreenState by profileScreenModel.state.collectAsState()
        val currentPurchasedPlan = profileScreenState.subscriptionType
        val tabsScreenModel = navigator.koinNavigatorScreenModel<TabsScreenModel>()

        val context = LocalPlatformContext.current
        val scope = rememberCoroutineScope()

        PurchaseSubscriptionScreenContent(
            isDarkMode = isDarkMode,
            onBack = {
                navigator.pop()
            },
            subscriptionType = subscriptionType,
            profileScreenModel = profileScreenModel,
            currentPurchasedPlan = currentPurchasedPlan,
            navigateToTravelScreen = {
                navigator.push(TravelTicketScreen())
            },
            navigateToVisitorsScreen = {
                navigator.push(ProfileVisitorsScreen())
            },
            navigateToLikedScreen = {
                navigator.push(LikesScreen())
            },
            navigateToSwipeScreen = {
                tabsScreenModel.updateTab(TabsScreenModel.BottomTab.SWIPE)
                navigator.popUntil {
                    it.instanceOf(HomeScreen::class)
                }
            },
            onPurchase = { subType,result ->
                val id = if (subType == SubscriptionType.WEEKLY) "com.lovorise.sub_week" else "com.lovorise.sub_monthly"

                InAppPurchase.DEFAULT?.startTransaction(PurchaseProductItem(productId = id, type = PurchaseProductItem.ProductType.SUBSCRIPTION)){ receiptData->
                    scope.launch {
                        withContext(Dispatchers.IO){
                            profileScreenModel.updateLoading(true)
                            val r = if(isAndroid()){
                                accountsScreenModel.verifyGooglePurchase(receiptData as GoogleReceiptData, context = context)
                            }else{
                                accountsScreenModel.verifyApplePurchase(receiptData as AppleReceiptData, context = context)
                            }
                            result(r)
                        }
                    }

                }

            },
            profileScreensState = profileScreenState
        )
    }
}

@Composable
fun PurchaseSubscriptionScreenContent(
    isDarkMode:Boolean,
    subscriptionType:SubscriptionType,
    onBack:()->Unit,
    profileScreenModel: ProfileScreenModel,
    currentPurchasedPlan:SubscriptionType,
    profileScreensState: ProfileScreensState,
    navigateToSwipeScreen:()->Unit,
    navigateToLikedScreen:()->Unit,
    navigateToVisitorsScreen:()->Unit,
    navigateToTravelScreen:()->Unit,
    onPurchase:(SubscriptionType,result:(Boolean)->Unit) -> Unit
) {

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    var currentSubscriptionType by remember { mutableStateOf(subscriptionType) }
    var currentPurchasedSubscriptionType by remember { mutableStateOf(currentPurchasedPlan) }
    val featureTitles = stringArrayResource(Res.array.subscriptions_feature_titles)
    val featureDescriptions = stringArrayResource(Res.array.subscriptions_feature_descriptions)

    val ctx = LocalPlatformContext.current

    val featureIcons = listOf(
        Res.drawable.ic_unlimited_swipes,
        Res.drawable.ic_control_privacy,
        Res.drawable.ic_who_liked,
        Res.drawable.ic_who_viewed,
        Res.drawable.ic_advance_filters,
        Res.drawable.ic_travel_ticket_feature,
        Res.drawable.ic_anonymous_mode,
        Res.drawable.ic_unlimited_backtrack,
        Res.drawable.ic_ads_free_enabled
    )

    LaunchedEffect(currentPurchasedSubscriptionType){
        profileScreenModel.updateSubscriptionType(currentPurchasedSubscriptionType)
    }

    val featureItems: List<SubscriptionFeatureListItem> by remember(currentSubscriptionType) {
        mutableStateOf(
            featureTitles.mapIndexed { index, title ->
                SubscriptionFeatureListItem(
                    icon = featureIcons[index],
                    title = title,
                    info = featureDescriptions[index],
                    disableText = false,
                    onTryNow = {

                        if (index == 0 || index == 4 || index == 6 || index == 7 || index == 8){
                            navigateToSwipeScreen()
                        }
                        if (index == 2){
                            navigateToLikedScreen()
                        }
                        if (index == 3){
                            navigateToVisitorsScreen()
                        }
                        if (index == 5){
                            navigateToTravelScreen()
                        }
                    }
                )
            }
        )
    }



    Column(Modifier.fillMaxSize().background(if (isDarkMode) BASE_DARK else Color.White)) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()

        Column(Modifier.fillMaxSize().weight(1f).verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier.fillMaxWidth().height(59.dp).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    Modifier.size(24.dp).noRippleClickable(onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = vectorResource(if (isDarkMode) Res.drawable.ic_cross_white else Res.drawable.ic_xmark),
                        contentDescription = null
                    )
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = stringResource(Res.string.subscription),
                    color = if (isDarkMode) Color.White else Color(0xff101828),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily()
                )
            }
            AnimatedVisibility(visible = ((currentSubscriptionType == SubscriptionType.WEEKLY && currentPurchasedSubscriptionType == SubscriptionType.WEEKLY) || (currentSubscriptionType == SubscriptionType.MONTHLY && currentPurchasedSubscriptionType == SubscriptionType.MONTHLY))){
                Column {
                    MembershipPlanMessage(
                        text = if (currentPurchasedSubscriptionType == SubscriptionType.WEEKLY) stringResource(Res.string.weekly_member_plan_message) else stringResource(Res.string.monthly_member_plan_message, profileScreensState.subscriptionData?.formatDDMMYYYY ?: "", "£ 5.99")
                    )
                }
            }







            Column {
                Spacer(Modifier.height(8.dp))
                SubscriptionIconAndText(
                    icon = Res.drawable.ic_lovorise_premium,
                    text = stringResource(Res.string.unlock_essential_features),
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(40.dp))
                BoxWithConstraints(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    val useNewLine = maxWidth <= 355.dp
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        WeeklyMonthlyPremiumCard(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                currentSubscriptionType = SubscriptionType.WEEKLY
                            },
                            type = SubscriptionType.WEEKLY,
                            isSelected = currentSubscriptionType == SubscriptionType.WEEKLY,
                            useNewLine = useNewLine
                        )
                        Spacer(Modifier.width(10.dp))
                        WeeklyMonthlyPremiumCard(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                currentSubscriptionType = SubscriptionType.MONTHLY
                            },
                            type = SubscriptionType.MONTHLY,
                            isSelected = currentSubscriptionType == SubscriptionType.MONTHLY,
                            useNewLine = useNewLine
                        )

                    }
                }

                Spacer(Modifier.height(32.dp))

               // Spacer(Modifier.height(8.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    featureItems.forEachIndexed {index,item ->
                        SubscriptionFeatureListItem(item, index = index,type = currentSubscriptionType, isPurchased = (currentSubscriptionType == SubscriptionType.WEEKLY && currentPurchasedSubscriptionType == SubscriptionType.WEEKLY) || (currentSubscriptionType == SubscriptionType.MONTHLY && currentPurchasedSubscriptionType == SubscriptionType.MONTHLY), isDarkMode = isDarkMode)
                    }
                }

                //Box(Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xffEAECF0)))
                if(currentPurchasedSubscriptionType == SubscriptionType.FREE) {
                    Spacer(Modifier.height(25.dp))
                    val annotatedString = buildAnnotatedString {
                        append(stringResource(if (currentSubscriptionType == SubscriptionType.WEEKLY)
                            Res.string.by_continuing_to_lovorise_premium
                        else Res.string.by_continuing_to_lovorise_premium))

                        append(" ")

                        // Make "Subscription Terms" clickable
                        val termsUrl = AppConstants.TERMS_AND_CONDITIONS_URL
                        pushStringAnnotation(tag = "terms", annotation = termsUrl)
                        withStyle(style = SpanStyle(color = Color(0xffF33358))) {
                            append(stringResource(Res.string.subscription_terms))
                        }
                        pop()

                        append(" ")

                        append(
                            if (currentSubscriptionType == SubscriptionType.WEEKLY) stringResource(Res.string.and_pay_once_for_one_week)
                            else if (isAndroid()) stringResource(Res.string.and_auto_renew_unless_google_play)
                            else stringResource(Res.string.and_auto_renew_unless_app_store_settings)
                        )
                    }
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .pointerInput(Unit) {
                                detectTapGestures { tapOffset ->
                                    textLayoutResult?.let { layoutResult ->
                                        val position = layoutResult.getOffsetForPosition(tapOffset)

                                        // Check for annotation at clicked position
                                        annotatedString.getStringAnnotations(tag = "terms", start = position, end = position)
                                            .firstOrNull()?.let {
                                                openUrlInCustomTab(it.item, context = ctx)
                                            }
                                    }
                                }
                            },
                        text = annotatedString,
                        fontSize = 14.sp,
                        color = Color(if (isDarkMode) 0xffEAECF0 else 0xff344054),
                        lineHeight = 21.sp,
                        letterSpacing = 0.2.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        onTextLayout = { textLayoutResult = it }
                    )
                }
                Spacer(Modifier.height(18.dp))
                //CustomDivider()


            }



        }

        AnimatedVisibility(visible =  (currentSubscriptionType != currentPurchasedSubscriptionType && currentPurchasedSubscriptionType != SubscriptionType.MONTHLY)){
            Column {
                PurchaseButtonSection(type = currentSubscriptionType, onClick = {type ->
                    onPurchase(type){
                        profileScreenModel.updateLoading(false)
                        if (it){
                            currentPurchasedSubscriptionType = type
                            profileScreenModel.updateSubscriptionData(type)
                        }
                    }
                })
            }
           // Spacer(Modifier.height(16.dp))
        }

        AnimatedVisibility(
            visible = currentSubscriptionType == currentPurchasedSubscriptionType
        ){
            Column {
                KeepExploringButton(onClick = navigateToSwipeScreen)
            }
        }


        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }

}

@Composable
fun MembershipPlanMessage(text: String) {
    Box(Modifier.fillMaxWidth().background(Color( 0xffF9E9EC))){
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp).padding(end = 2.dp),
            text = text,
            color = Color( 0xff101828),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily()
        )
    }
    Spacer(Modifier.height(24.dp))
}

@Composable
fun PurchaseButtonSection(type: SubscriptionType,onClick: (SubscriptionType) -> Unit) {
   // Spacer(Modifier.height(8.dp))

    CustomDivider()
    Spacer(Modifier.height(16.dp))
    Column(Modifier.padding(bottom = 16.dp).padding(horizontal = 16.dp)) {
        val text =
            stringResource(if (type == SubscriptionType.MONTHLY) Res.string.get_one_month_premium else Res.string.get_one_week_premium)
        val boldText =
            if (type == SubscriptionType.WEEKLY) "£ 2.99" else "£ 5.99" // The portion to bold


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xffF33358), RoundedCornerShape(12.dp))
                .noRippleClickable { onClick(type) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                fontFamily = PoppinsFontFamily(),
                text = buildAnnotatedString {
                    val startIndex = text.indexOf(boldText)
                    val endIndex = startIndex + boldText.length

                    // Add the text before the bold portion
                    withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                        append(text.substring(0, startIndex))
                    }

                    // Add the bold portion
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(boldText)
                    }

                    // Add the remaining text
                    append(text.substring(endIndex))
                },
                fontSize = 16.sp,
                color = Color.White,
                lineHeight = 19.36.sp,
                letterSpacing = (-0.3).sp
            )
        }
    }
}

@Composable
fun KeepExploringButton(onClick: () -> Unit) {
   // Spacer(Modifier.height(8.dp))
    CustomDivider()
    Spacer(Modifier.height(16.dp))
    Column(Modifier.padding(bottom = 16.dp).padding(horizontal = 16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xffF33358), RoundedCornerShape(12.dp))
                .noRippleClickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.keep_exploring),
                fontFamily = PoppinsFontFamily(),
                fontSize = 16.sp,
                color = Color.White,
                lineHeight = 19.36.sp,
                letterSpacing = (-0.3).sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

}

@Composable
fun SubscriptionFeatureListItem(item: SubscriptionFeatureListItem,type: SubscriptionType,isPurchased:Boolean,index:Int,isDarkMode: Boolean) {
    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ){
        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center){
            if (isDarkMode) {
                Icon(
                    imageVector = vectorResource(item.icon),
                    contentDescription = null,
                    tint = Color.White
                )
            }else{
                Icon(
                    imageVector = vectorResource(item.icon),
                    contentDescription = null,
                )
            }
        }
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(start = 10.dp).weight(1f,fill = false),
                    text = item.title,
                    color = Color(if (isDarkMode) 0xffFFFFFF else 0xff101828),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily()
                )
                Spacer(Modifier.width(10.dp))
                if (!item.disableText && isPurchased){
                    TryNowButton(
                        onClick = {
                            item.onTryNow(index)
                        },
                        type = type
                    )
                }

              //  Spacer(Modifier.weight(1f))

            }

            Text(
                modifier = Modifier.padding(horizontal = 12.5.dp),
                text = item.info,
                color = Color(if (isDarkMode) 0xffEAECF0 else 0xff344054),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily()
            )


        }

    }

}

data class SubscriptionFeatureListItem(
    val title:String,
    val info:String,
    val icon:DrawableResource,
    val disableText: Boolean,
    val onTryNow: (Int) -> Unit
)


@Composable
fun SubscriptionIconAndText(icon:DrawableResource,text: String,isDarkMode: Boolean) {

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 34.5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = vectorResource(icon),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )

        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            text = text,
            color = if (isDarkMode) Color.White else Color(0xff475467),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
            textAlign = TextAlign.Center
        )

    }
}


@Composable
fun WeeklyMonthlyPremiumCard(
    isSelected:Boolean,
    onClick:()->Unit,
    type: SubscriptionType,
    modifier: Modifier,
    useNewLine:Boolean
  //  selectedType:SubscriptionType
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(listOf(Color(0xffF3335D), Color(0xffF33386))),
                shape = RoundedCornerShape(8.dp)
            )
            .noRippleClickable(onClick)
    ) {

        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Spacer(Modifier.height(13.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                Image(
                    imageVector = vectorResource(if (isSelected) Res.drawable.ic_checked_filled else Res.drawable.ic_unchecked_circle),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(18.dp))
                Text(
                    text = stringResource(if (type == SubscriptionType.WEEKLY) Res.string.weekly else Res.string.monthly),
                    color = Color(0xffFFFFFF),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily()
                )


            }

            Spacer(Modifier.height(14.dp))

            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = if (type == SubscriptionType.WEEKLY) "£ 2.99/${if(useNewLine) "\n" else ""}${stringResource(Res.string.week)}" else "£ 5.99/${if(useNewLine) "\n" else ""}${stringResource(Res.string.month_cap)}",
                color = Color(0xffFFFFFF),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                lineHeight = 27.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily()
            )

            if (type == SubscriptionType.WEEKLY){
                Box(Modifier.padding(start = 10.dp), contentAlignment = Alignment.CenterStart){
                    Text(
                        text = if (useNewLine) "One-time\npayment" else stringResource(Res.string.one_time_payment),
                        color = Color(0xffFFFFFF),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        letterSpacing = 0.2.sp,
                        fontFamily = PoppinsFontFamily()
                    )
                }

            }

            Spacer(
                Modifier.height(
                    if (!useNewLine) {
                        if (type == SubscriptionType.WEEKLY) 9.dp else 27.dp
                    }else{
                        if (type == SubscriptionType.WEEKLY) 9.dp else 45.dp
                    }
                )
            )

        }

    }
    
}



enum class SubscriptionType{
    MONTHLY,WEEKLY,FREE
}