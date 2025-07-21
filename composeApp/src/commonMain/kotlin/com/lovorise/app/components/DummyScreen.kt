package com.lovorise.app.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_rollback
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.getImagesFromDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource


class DummyScreen : Screen{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        DummyScreenContent(
            navigateToRewardsScreen = {
              //  navigator.push(LoginRewardsScreen(it))
            },
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DummyScreenContent(
    navigateToRewardsScreen: (Int) -> Unit
) {

    val sheetState =  rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    var showToast by remember {
        mutableStateOf(false)
    }

    var startFetchingImage by remember { mutableStateOf(false) }
    var images by remember { mutableStateOf(listOf<String>()) }
    val context = LocalPlatformContext.current

    LaunchedEffect(startFetchingImage){
        if (startFetchingImage){
            getImagesFromDevice(context).collectLatest {
                images = images.toMutableList().apply {
                  //  addAll(it)
                }
                if (images.size >= 3){
                    startFetchingImage = false
                }
            }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xff908ec0))
                .padding(horizontal = 16.dp).padding(it),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Spacer(modifier = Modifier.height(30.dp))



            BacktrackCardWithArrow()

            Button(
                onClick = {
                    navigateToRewardsScreen(100)
                }
            ){

                Text("Claim login rewards")

            }


            Button(
                onClick = {
                    navigateToRewardsScreen(200)
                }
            ){

                Text("Premium login rewards")

            }


            Button(
                onClick = {
                    showToast = true
                }
            ){

                Text("Show Toast")

            }

            Button(
                onClick = {
                    showBottomSheet = true
                }
            ){

                Text("Show Backtrack bottom sheet")

            }

            Button(onClick = {
                if (!startFetchingImage) {
                    startFetchingImage = true
                }
            }){
                Text("Get all images")
            }

            if (startFetchingImage){
                CircularProgressIndicator()
            }
            else{
                LazyColumn {
                    items(images){
                        Text("$images")
                        Spacer(Modifier.height(3.dp))
                    }
                }
            }






        }

        AnimatedVisibility(
            modifier = Modifier.padding(top = 20.dp).safeContentPadding().padding(horizontal = 16.dp).height(36.dp),
            visible = showToast,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Toast(text = "This is toast string")

            LaunchedEffect(Unit) {
                delay(2000) // Hide after 2 seconds
                showToast = false
            }

        }

        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    showBottomSheet = false
                },

                shape = RoundedCornerShape(topStartPercent = 6, topEndPercent = 6),
                dragHandle = null,
                //   modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)

            ) {

                BacktrackSheetContent(
                    onCancelClick = {
                        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                )

            }
        }
    }






}


@Composable
fun BacktrackCardWithArrow() {


    val density = LocalDensity.current
    val arrowHeight = 6.dp

    val bubbleShape = remember {
        getBubbleShape(
            density = density,
            cornerRadius = 15.dp,
            arrowWidth = 9.dp,
            arrowHeight = arrowHeight,
            arrowOffset = 19.dp
        )
    }

    Column {

        Image(imageVector = vectorResource(Res.drawable.ic_rollback), contentDescription = null, modifier = Modifier.height(20.dp).width(27.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(96.dp)
                .width(178.dp)
                .background(Color(0xffF33358),bubbleShape),
        ) {

            Column(
                modifier = Modifier.fillMaxSize().padding(top = 16.5.dp, start = 14.5.dp, end = 14.5.dp, bottom = 12.5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Made a mistake?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    lineHeight = 21.sp,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp
                )
                Text(
                    text = "Use backtrack to view\nthe previous person",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    lineHeight = 18.sp,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp
                )
            }
        }
    }


}
