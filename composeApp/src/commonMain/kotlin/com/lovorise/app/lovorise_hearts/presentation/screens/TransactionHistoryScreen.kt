package com.lovorise.app.lovorise_hearts.presentation.screens


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_filter
import coinui.composeapp.generated.resources.ic_heart_small
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.months
import coinui.composeapp.generated.resources.no_transactions_yet
import coinui.composeapp.generated.resources.transaction_history
import coinui.composeapp.generated.resources.transaction_tabs
import coinui.composeapp.generated.resources.your_transaction_history_is_empty
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.lovorise_hearts.domain.model.TransactionData
import com.lovorise.app.lovorise_hearts.presentation.PurchaseScreenModel
import com.lovorise.app.lovorise_hearts.presentation.components.FilterTransactionBottomSheetContent
import com.lovorise.app.lovorise_hearts.presentation.components.TransactionDetailsBottomSheetContent
import com.lovorise.app.lovorise_hearts.presentation.states.TransactionScreenState
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.edit_profile.hideWithCompletion
import com.lovorise.app.profile_visitors.components.CustomTabRow
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class TransactionHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val purchaseScreenModel = navigator.koinNavigatorScreenModel<PurchaseScreenModel>()
        val transactionScreenState by purchaseScreenModel.transactionState.collectAsState()

        val context = LocalPlatformContext.current
        val months = stringArrayResource(Res.array.months)
        LaunchedEffect(true){
            if (transactionScreenState.transactions.isEmpty()){
                purchaseScreenModel.loadAllTransactions(context,months)
            }
        }

        TransactionHistoryScreenContent(
            onBack = {
                navigator.pop()
            },
            isDarkMode = isDarkMode,
            transactionScreenState = transactionScreenState,
            purchaseScreenModel = purchaseScreenModel
        )
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreenContent(onBack:()->Unit, isDarkMode:Boolean,transactionScreenState: TransactionScreenState,purchaseScreenModel: PurchaseScreenModel) {


    val context = LocalPlatformContext.current

    val transactionTabs = stringArrayResource(Res.array.transaction_tabs)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val transactionDetailsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


    val pagerState = rememberPagerState { 3 }

    val coroutineScope = rememberCoroutineScope()

    var selectedTransaction by remember { mutableStateOf<TransactionData?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().background(if (isDarkMode) BASE_DARK else Color.White)
    ) {


        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()


        Column(
            modifier = Modifier.fillMaxSize().weight(1f)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(57.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier.size(24.dp)
                        .noRippleClickable(onBack),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        tint = if (isDarkMode) Color.White else Color.Black,
                        modifier = Modifier.width(16.dp).height(12.dp),
                        imageVector = vectorResource(Res.drawable.ic_left),
                        contentDescription = "back"
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = stringResource(Res.string.transaction_history),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color(0xff101828)
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    tint = if (isDarkMode) Color.White else Color.Black,
                    modifier = Modifier.size(24.dp).noRippleClickable { purchaseScreenModel.updateTransactionFilterSheetState(false) },
                    imageVector = vectorResource(Res.drawable.ic_filter),
                    contentDescription = "back"
                )

            }

            Spacer(Modifier.height(16.dp))

            CustomTabRow(
                titles = transactionTabs,
                tabIndex = pagerState.currentPage,
                count1 =  null,
                count2 = null,
                paddingValues = PaddingValues(top = 12.dp, start = 16.dp, end = 16.dp),
                onTabSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.height(8.dp))

            HorizontalPager(
                state = pagerState
            ){



                LazyColumn{

                   (if (pagerState.currentPage == 0) transactionScreenState.transactionsWithCategory else if (pagerState.currentPage == 1) transactionScreenState.incomingTransactionsWithCategory else transactionScreenState.outgoingTransactionsWithCategory ).forEach {transactionsWithCategory ->

                       if (transactionsWithCategory.transactions.isNotEmpty()) {
                            stickyHeader {
                                TransactionHeaderMonthYear(transactionsWithCategory.category, isDarkMode = isDarkMode)
                            }
                            items(transactionsWithCategory.transactions) { item ->
                                TransactionItem(item, isDarkMode = isDarkMode, showDivider = item != transactionsWithCategory.transactions.last(), onClick = {
                                    selectedTransaction = item
                                })
                            }
                        }else{
                            item {
                                NoTransactionsYet(isDarkMode = isDarkMode)
                            }

                        }
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

    if (selectedTransaction != null) {
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = transactionDetailsSheetState,
            onDismissRequest = {
                selectedTransaction = null
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {

            TransactionDetailsBottomSheetContent(
                isDarkMode = isDarkMode,
                onCancel = {
                    transactionDetailsSheetState.hideWithCompletion(coroutineScope){
                        selectedTransaction = null
                    }
                },
                transactionData = selectedTransaction!!
            )
        }
    }

    if (transactionScreenState.showTransactionFilterSheet) {
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                purchaseScreenModel.updateTransactionFilterSheetState(false)
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            FilterTransactionBottomSheetContent(
                isDarkMode = isDarkMode,
                onApply = {
                    sheetState.hideWithCompletion(coroutineScope){
                        purchaseScreenModel.updateTransactionFilterSheetState(false)
                    }
                }
            )


        }
    }




}




@Composable
fun NoTransactionsYet(isDarkMode: Boolean) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(Modifier.weight(1f))
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(Res.string.no_transactions_yet),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
        )

        Spacer(Modifier.height(10.dp))

        Text(
            modifier = Modifier.padding(horizontal = 40.dp),
            textAlign = TextAlign.Center,
            text = stringResource(Res.string.your_transaction_history_is_empty),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
        )
        
        
        
        
        
        
        
        



        Spacer(Modifier.weight(2f))

    }
}

@Composable
fun TransactionItem(item:TransactionData,isDarkMode: Boolean,showDivider:Boolean,onClick:()-> Unit){


    Row(Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 12.dp).noRippleClickable(onClick), verticalAlignment = Alignment.Top) {
        Column(Modifier.fillMaxWidth().weight(1f)) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = if (isDarkMode) Color.White else Color(0xff101828),
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = item.formattedDate,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = if (item.type == TransactionData.Type.INCOMING) "+${item.change}" else "-${item.change}",
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color(if (item.type == TransactionData.Type.INCOMING) 0xff12B76A else 0xffD92D20),
            letterSpacing = 0.14.sp,
            lineHeight = 16.52.sp,
            fontFamily = PoppinsFontFamily(),
        )

        Spacer(Modifier.width(4.dp))
        Image(
            imageVector = vectorResource(Res.drawable.ic_heart_small),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
    if (showDivider){
        Box(Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(Color(0xffEAECF0))
        )
    }


}


@Composable
fun TransactionHeaderMonthYear(date:String,isDarkMode: Boolean) {

    Box(
        Modifier.fillMaxWidth()
            .height(37.dp)
            .background(if (isDarkMode) CARD_BG_DARK else Color(0xffF9E9EC)),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = date,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
        )
    }
    
}