package com.lovorise.app.swipe.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.lovorise.app.swipe.domain.SwipeProfileUser
import com.lovorise.app.swipe.presentation.toPx
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt


enum class SwipeDialogType{
    LEFT_SWIPE_DIALOG, RIGHT_SWIPE_DIALOG, SKIP_TEMPORARY_DIALOG
}

enum class SwipeType{
    INTERESTED, NOT_INTERESTED
}

@Composable
fun SwipeCard(
    profile: SwipeProfileUser,
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    sensitivityFactor: Float = 2f,
    swipeEnabled: Boolean = true,
    presentedDialogs:List<SwipeDialogType>,
    onPresentedDialog:(SwipeDialogType) ->Unit,
    skipTemporary: Boolean,
    like: Boolean,
    dislike:Boolean,
    onSkippedTemporary : ()->Unit,
    onCancelSkip : () ->Unit,
    offsetY:Float,
    globalOffsetX:Float?,
    updateOffsetY:(Float,String)->Unit,
    updateOffsetX:(Float,String)->Unit,
    currentlyVisibleId:String,
    content: @Composable (Boolean) -> Unit,
) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
   // var offsetY by remember { mutableStateOf(0f) }
    val density = LocalDensity.current.density
    val animatedOffsetX by animateFloatAsState(targetValue = globalOffsetX ?: offsetX)
    val animatedOffsetY by animateFloatAsState(targetValue = offsetY)
    val rotation by animateFloatAsState(targetValue = offsetX / 10)
  //  val scale by animateFloatAsState(targetValue = 1f - (abs(offsetX) / 4000))
    var showInterestedDialog by rememberSaveable { mutableStateOf(false) }
    var showNotInterestedDialog by rememberSaveable { mutableStateOf(false) }
    var showSkipTempDialog by rememberSaveable { mutableStateOf(false) }
    var isInterestedDialogPresented by rememberSaveable { mutableStateOf(false) }
    var isNotInterestedDialogPresented by rememberSaveable { mutableStateOf(false) }
    var isSkipTempDialogDialogPresented by rememberSaveable { mutableStateOf(false) }
    var cardHeight by rememberSaveable { mutableFloatStateOf(0f) }
    var cardWidth by rememberSaveable { mutableFloatStateOf(0f) }

    LaunchedEffect(presentedDialogs){
        if (presentedDialogs.contains(SwipeDialogType.RIGHT_SWIPE_DIALOG)){
            isInterestedDialogPresented = true
        }
        if (presentedDialogs.contains(SwipeDialogType.LEFT_SWIPE_DIALOG)){
            isNotInterestedDialogPresented = true
        }
        if (presentedDialogs.contains(SwipeDialogType.SKIP_TEMPORARY_DIALOG)){
            isSkipTempDialogDialogPresented = true
        }
    }


    val tempSkip : suspend () -> Unit = {
        updateOffsetY(cardHeight / 4,profile.user.id)
        delay(100L)
        updateOffsetY(cardHeight / 3,profile.user.id)
        delay(100L)
        updateOffsetY(cardHeight / 2,profile.user.id)
        delay(100L)
        updateOffsetY(cardHeight,profile.user.id)
        onSkippedTemporary()
    }

    val likeSwipe : suspend () -> Unit = {
        updateOffsetX(cardWidth / 4,profile.user.id)
        delay(10L)
        updateOffsetX(cardWidth / 3,profile.user.id)
        delay(10L)
        updateOffsetX(cardWidth / 2,profile.user.id)
        delay(10L)
        updateOffsetX(cardWidth,profile.user.id)
        onSwipeRight()
    }


    val dislikeSwipe : suspend () -> Unit = {
        updateOffsetX(-cardWidth / 4,profile.user.id)
        delay(10L)
        updateOffsetX(-cardWidth / 3,profile.user.id)
        delay(10L)
        updateOffsetX(-cardWidth / 2,profile.user.id)
        delay(10L)
        updateOffsetX(-cardWidth,profile.user.id)
        onSwipeLeft()
    }


    LaunchedEffect(like){
        if(like && currentlyVisibleId == profile.user.id) {
            if (!isInterestedDialogPresented) {
                showInterestedDialog = true
            } else {
                likeSwipe()
            }
        }
    }

    LaunchedEffect(dislike){
        if(dislike && currentlyVisibleId == profile.user.id) {
            if (!isNotInterestedDialogPresented) {
                showNotInterestedDialog = true
            } else {
                dislikeSwipe()
            }
        }
    }



    LaunchedEffect(skipTemporary) {
        if(skipTemporary && currentlyVisibleId == profile.user.id) {
            if (!isSkipTempDialogDialogPresented) {
                showSkipTempDialog = true
            } else {
                tempSkip()
            }
        }
    }


    val scope = rememberCoroutineScope()

    BoxWithConstraints(modifier = Modifier) {
        cardWidth = constraints.maxWidth.toFloat()
        cardHeight = maxHeight.toPx()
        val swipeThreshold = cardWidth * 0.1f
        Box(
            modifier = Modifier
                .then(
                    if (swipeEnabled) Modifier
                        .offset {
                            IntOffset(
                                animatedOffsetX.roundToInt(),
                                animatedOffsetY.roundToInt()
                            )
                        }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(onDragEnd = {

                                if (abs(offsetX) > swipeThreshold) {
                                    if (offsetX > 0) {
                                        if (!isInterestedDialogPresented) {
                                            showInterestedDialog = true
                                        } else {
                                            scope.launch {
                                                delay(40)
                                                offsetX = cardWidth
                                                onSwipeRight.invoke()
                                            }

                                        }
                                    } else {
                                        if (!isNotInterestedDialogPresented) {
                                            showNotInterestedDialog = true
                                        } else {
                                            scope.launch {
                                                delay(40)
                                                offsetX = -cardWidth
                                                onSwipeLeft.invoke()
                                            }
                                        }
                                    }
                                }
                                else {
                                    // Reset the card position if swipe threshold not met
                                    offsetX = 0f
                                   // updateOffsetX(0f,profile.user.id)
                                }


                                // onDrag(false)
                            }
                            ) { _, dragAmount ->
                                offsetX += (dragAmount / density) * sensitivityFactor
//                            println("the drag value $offsetX")
                                // onDrag(true)
                            }
                        }
                        .graphicsLayer(
                            translationX = animatedOffsetX,
                            rotationZ = rotation,
                            translationY = animatedOffsetY
//                        scaleX = scale,
//                        scaleY = scale
                        )
                    else Modifier
                )
        ) {

            Box(
                Modifier.fillMaxSize().then(
                    if (offsetX != 0f) Modifier.clip(RoundedCornerShape(10.dp)) else if (offsetY != 0f) Modifier.clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)) else Modifier
                )
            ) {
                content(offsetY != 0f)

                if (showInterestedDialog) {
                    InterestedOrNotInterestedConfirmationDialog(
                        onPositive = {
                            showInterestedDialog = false
                            isInterestedDialogPresented = true
                            offsetX = cardWidth
                            onSwipeRight.invoke()
                            onPresentedDialog(SwipeDialogType.RIGHT_SWIPE_DIALOG)

                        },
                        onCancel = {
                            showInterestedDialog = false
                            isInterestedDialogPresented = true
                            offsetX = 0f
                            onPresentedDialog(SwipeDialogType.RIGHT_SWIPE_DIALOG)
                        },
                        isInterested = true
                    )
                }

                if (showNotInterestedDialog) {
                    InterestedOrNotInterestedConfirmationDialog(
                        onPositive = {
                            showNotInterestedDialog = false
                            isNotInterestedDialogPresented = true
                            offsetX = -cardWidth
                            onSwipeLeft.invoke()
                            onPresentedDialog(SwipeDialogType.LEFT_SWIPE_DIALOG)
                        },
                        onCancel = {
                            showNotInterestedDialog = false
                            isNotInterestedDialogPresented = true
                            offsetX = 0f
                            onPresentedDialog(SwipeDialogType.LEFT_SWIPE_DIALOG)
                        },
                        isInterested = false
                    )
                }

                if (showSkipTempDialog) {
                    InterestedOrNotInterestedConfirmationDialog(
                        onPositive = {
                            scope.launch {
                                showSkipTempDialog = false
                                isSkipTempDialogDialogPresented = true
                                updateOffsetY(cardHeight,profile.user.id)
                                tempSkip()
                                onPresentedDialog(SwipeDialogType.SKIP_TEMPORARY_DIALOG)
                            }
                        },
                        onCancel = {
                            scope.launch {
                                showSkipTempDialog = false
                                isSkipTempDialogDialogPresented = true
                                updateOffsetY(0f, profile.user.id)
                                onCancelSkip()
                                onPresentedDialog(SwipeDialogType.SKIP_TEMPORARY_DIALOG)
                            }
                        },
                        isInterested = false,
                        isSkip = true
                    )
                }

                if (offsetX > 200f || offsetX < -200f) {
                    SwipeLeftRightIndication(isLeftSwipe = offsetX < 0)
                }

                if (animatedOffsetY >= 1f) {
                    SkipTemporaryIndication()
                }
            }

        }
    }
}

