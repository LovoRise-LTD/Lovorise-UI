package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_back_icon_white
import coinui.composeapp.generated.resources.ic_capture_image
import coinui.composeapp.generated.resources.ic_flash_off
import coinui.composeapp.generated.resources.ic_flash_on
import coinui.composeapp.generated.resources.ic_gallery
import coinui.composeapp.generated.resources.ic_record_video
import coinui.composeapp.generated.resources.ic_switch_camera_face
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.camera.FlashMode
import com.lovorise.app.libs.camera.IosImageView
import com.lovorise.app.noRippleClickable
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.roundToInt

enum class CaptureRecordPromptAction{
    SIXTY_SEC_VIDEO,FIFTEEN_SEC_VIDEO,PHOTO,PROMPT
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CaptureRecordOverlayItems(
    screenWidth:Dp,
    onToggleFlash:()->Unit,
    flashMode:FlashMode,
    onToggleCameraFace:()->Unit,
   // profileUrl:String,
    onCaptureClick:() -> Unit,
    promptAction: CaptureRecordPromptAction,
    onPromptAction:(CaptureRecordPromptAction)->Unit,
    onBackIconClick:()->Unit,
    onProfileIconClick:()->Unit,
    recordTimer:String,
    isRecording:Boolean,
    onStopRecording:()->Unit,
    onStartRecording:(Int)->Unit,
    onGalleryIconClick:()->Unit,
    imagePath:Any,
    onRecordingDone:()->Unit
) {

    val items = listOf("60s","15s","Photo","Prompt")

    val density = LocalDensity.current
    var boxWidth by remember { mutableFloatStateOf(0f) }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val coroutineScope = rememberCoroutineScope()

    val anchors = remember(boxWidth) {
        val diff = boxWidth - with(density) { 335.dp.toPx() }
        val ratio = boxWidth / with(density) { 335.dp.toPx() }
        DraggableAnchors {
            "60s" at 390f + (diff/ratio)
            "15s" at 254f + (diff/ratio)
            "Photo" at getDefaultAnchorPosition(screenWidth)
            "Prompt" at -95f + (diff/ratio)
        }
    }

    LaunchedEffect(recordTimer){
        if ((recordTimer == "1:00" && promptAction == CaptureRecordPromptAction.SIXTY_SEC_VIDEO) || (recordTimer == "0:15" && promptAction == CaptureRecordPromptAction.FIFTEEN_SEC_VIDEO)){
            onRecordingDone()
        }
    }


    val state = remember {
        AnchoredDraggableState(
            initialValue = "Photo", // Start with the first anchor
            anchors = anchors,
            decayAnimationSpec = decayAnimationSpec,
            snapAnimationSpec = tween(),
            positionalThreshold = { totalDistance: Float -> totalDistance * 0.3f },
            velocityThreshold = { 0.3f },
            confirmValueChange = { _: String -> !isRecording }
        )
    }

    SideEffect {
        if (!isRecording) state.updateAnchors(anchors)
    }


    LaunchedEffect(state.currentValue){
        val action = getPromptAction(state.currentValue)
        if (action == CaptureRecordPromptAction.PROMPT) {
            delay(100L)
            onPromptAction(action)
        }else{
            onPromptAction(action)
        }
    }

    Column(Modifier.fillMaxSize().onGloballyPositioned {
        boxWidth = it.size.width.toFloat()
    }, horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.height(20.dp))

        Row(
            Modifier.fillMaxWidth()
                .padding(start = 19.dp, end = 11.dp),
        ) {

            Box(Modifier.height(33.29.dp).width(34.67.dp).noRippleClickable(onBackIconClick), contentAlignment = Alignment.Center) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_back_icon_white),
                    contentDescription = null,
                    modifier = Modifier.width(18.dp).height(16.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            if (isRecording && recordTimer.isNotBlank()) {
                Row(
                    modifier = Modifier.height(33.29.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(10.dp).background(Color(0xffF33358), CircleShape))
                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = recordTimer,
                        fontFamily = PoppinsFontFamily(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        lineHeight = 24.sp
                    )
                }
                Spacer(Modifier.weight(1f))
            }


            Column(Modifier.width(38.dp), verticalArrangement = Arrangement.spacedBy(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(15.dp))
                Box(Modifier.size(24.dp).noRippleClickable(onToggleFlash), contentAlignment = Alignment.Center){
                    Image(
                        imageVector = vectorResource(if(flashMode == FlashMode.ON) Res.drawable.ic_flash_on else Res.drawable.ic_flash_off),
                        contentDescription = null,
                        modifier = Modifier.width(17.14.dp).height(20.dp)
                    )
                }

                Box(Modifier.size(24.dp).noRippleClickable(onToggleCameraFace), contentAlignment = Alignment.Center) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_switch_camera_face),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (!isRecording) {
                    AsyncImage(
                        model = "https://images.pexels.com/photos/712521/pexels-photo-712521.jpeg?auto=compress&cs=tinysrgb&w=800",
                        contentDescription = null,
                        modifier = Modifier.size(38.dp)
                            .clip(CircleShape).noRippleClickable(onProfileIconClick),
                        contentScale = ContentScale.Crop
                    )
                }




            }



        }


        Spacer(Modifier.weight(1f))

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        IntOffset(
                            x = state
                                .requireOffset()
                                .roundToInt(),
                            y = 0
                        )
                    }
                    .then(
                        if (isRecording) Modifier else Modifier.anchoredDraggable(
                            state = state,
                            orientation = Orientation.Horizontal
                        )
                    ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

//                Spacer(Modifier.weight(1.06f))

                items.forEach { item ->
                    CaptureRecordPromptActionItem(
                        title = item,
                        isSelected = item == state.currentValue,
                        onClick = {
                            if (!isRecording) {
                                coroutineScope.launch {
                                    state.snapTo(item)
                                }
                            }
                        }
                    )
                }

//                Spacer(Modifier.weight(1f))


            }

            Spacer(Modifier.height(18.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(26.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Spacer(Modifier.weight(2.434f))

                if ((promptAction == CaptureRecordPromptAction.PHOTO || promptAction == CaptureRecordPromptAction.SIXTY_SEC_VIDEO || promptAction == CaptureRecordPromptAction.FIFTEEN_SEC_VIDEO) && !isRecording) {
                    Image(
                        imageVector = vectorResource(if (promptAction == CaptureRecordPromptAction.PHOTO) Res.drawable.ic_capture_image else Res.drawable.ic_record_video),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp).noRippleClickable{ if (promptAction == CaptureRecordPromptAction.PHOTO) onCaptureClick() else onStartRecording(if(promptAction == CaptureRecordPromptAction.SIXTY_SEC_VIDEO) 60 else 15)}

                    )
                }else if ((promptAction == CaptureRecordPromptAction.SIXTY_SEC_VIDEO || promptAction == CaptureRecordPromptAction.FIFTEEN_SEC_VIDEO) && isRecording){
                    Box(
                        modifier = Modifier.size(56.dp).noRippleClickable(onStopRecording)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ){
                        Box(Modifier.size(16.dp).background(Color(0xffF33358), RoundedCornerShape(2.dp)))
                    }
                }

                Spacer(Modifier.width(26.dp))


                Box(
                    modifier = Modifier.size(56.dp).then(if (!isRecording) Modifier.background(Color(0xffEAECF0), CircleShape).noRippleClickable(onGalleryIconClick) else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isRecording) {
                        if (imagePath.instanceOf(String::class) && imagePath != ""){
                            AsyncImage(
                                modifier = Modifier.size(56.dp).clip(CircleShape),
                                model = imagePath,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                        else if (!isAndroid() && !imagePath.instanceOf(String::class)){
                            IosImageView(imgData = imagePath)
                        }
                        else {
                            Image(
                                imageVector = vectorResource(Res.drawable.ic_gallery),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))
            }

        }


        Spacer(Modifier.height(30.dp))
    }

}

fun getPromptAction(item:String) : CaptureRecordPromptAction {
    return when (item) {
        "Photo" -> CaptureRecordPromptAction.PHOTO
        "60s" -> CaptureRecordPromptAction.SIXTY_SEC_VIDEO
        "15s" -> CaptureRecordPromptAction.FIFTEEN_SEC_VIDEO
        else -> CaptureRecordPromptAction.PROMPT
    }
}

//fun getPromptActionValue(promptAction: CaptureRecordPromptAction) : String{
//    return when(promptAction){
//        CaptureRecordPromptAction.PHOTO -> "Photo"
//        CaptureRecordPromptAction.SIXTY_SEC_VIDEO -> "60s"
//        CaptureRecordPromptAction.FIFTEEN_SEC_VIDEO -> "15s"
//        CaptureRecordPromptAction.PROMPT -> "Prompt"
//    }
//}


fun getDefaultAnchorPosition(width:Dp) : Float{
    return if (width < 320.dp) 95f
    else if (width in 320.dp .. 330.dp) 100f
    else if (width in 331.dp .. 335.dp) 105f
    else if (width in 336.dp .. 339.dp) 110f
    else if (width in 340.dp .. 345.dp) 118f
    else if (width in 346.dp .. 354.dp) 129f
    else if (width in 355.dp .. 369.dp) 163f
    else if (width in 370.dp .. 375.dp) 176f
    else if (width in 376.dp .. 383.dp) 189f
    else if (width in 384.dp .. 390.dp) 213f
    else if (width in 391.dp .. 394.dp) 225f
    else if (width in 395.dp .. 400.dp) 230f
    else 235f

}

@Composable
fun CaptureRecordPromptActionItem(title:String,isSelected:Boolean,onClick:()->Unit) {

    Box(Modifier
        .height(25.dp)
        //.wrapContentWidth()
        .noRippleClickable(onClick)
        .then(
            if (isSelected) Modifier
                .background(Color(0xffF33358), RoundedCornerShape(50))
            else Modifier
        ),
        contentAlignment = Alignment.Center
    ){
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = title,
            lineHeight = 21.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = PoppinsFontFamily(),
            letterSpacing = 0.2.sp,
            color = Color.White
        )
    }

}