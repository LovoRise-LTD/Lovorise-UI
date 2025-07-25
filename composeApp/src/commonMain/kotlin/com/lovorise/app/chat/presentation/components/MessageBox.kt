package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.ScreenResize
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_double_checkmark
import coinui.composeapp.generated.resources.ic_tick_mark
import com.lovorise.app.MediaItem
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.domain.model.Message
import com.lovorise.app.chat.link_og_tag.LinkPreviewData
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.PRIMARY
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBox(
    modifier: Modifier,
    message: String,
    timestamp:String,
    isSent:Boolean,
    showTip:Boolean,
    showDoubleCheckMark:Boolean,
    separatorText:String,
    onLongPress:()->Unit,
    isSelected:Boolean,
    onClick:()->Unit,
    replyData: Message.ReplyData?,
    linkPreviewData:LinkPreviewData?,
    onLinkClicked: (String) -> Unit,
    onSwipe:()->Unit,
    isDarkMode: Boolean,
    medias:List<MediaItem>
) {

    LaunchedEffect(true){
        if (separatorText.isNotBlank()) {
            println("the separator $separatorText for $message")
        }
    }
    val density = LocalDensity.current
    val cornerShape = with(density) { 32.dp.toPx() }
    val arrowWidth = with(density) { 5.dp.toPx() }
    val arrowHeight = with(density) { 2.dp.toPx() }


    var bubbleHeight by remember { mutableStateOf(0.dp) }
    var bubbleWidth by remember { mutableStateOf(0.dp) }

    var shouldReset by remember { mutableStateOf(false) }

    val dismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                shouldReset = true
                onSwipe()
            } else if (value == SwipeToDismissBoxValue.StartToEnd) {
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

    Column (
        modifier = modifier,
        horizontalAlignment = if (isSent) Alignment.End else Alignment.Start
    ) {


        if (separatorText.isNotBlank()) {
            SentReceivedDate(separatorText,isDarkMode)
        }


        Box(Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .noRippleClickable { }
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
                onLongClick = onLongPress
            )
            .then(
                if (isSelected) Modifier.background(brush = Brush.linearGradient(colors = listOf(
                    Color(0xffF3335D).copy(alpha = 0.5f),
                    Color(0xffF33386).copy(alpha = 0.5f),
                )))
                else Modifier
            ),
            contentAlignment = if (isSent) Alignment.TopEnd else Alignment.TopStart
        ) {

            @Composable
            fun SentInfo(modifier: Modifier=Modifier) {
                Box{
                    Row(
                        modifier = modifier
                       //     .padding(horizontal = 16.dp)
                    ) {
//                        Spacer(Modifier.weight(1f))
                        Text(
                            fontWeight = FontWeight.Normal,
                            lineHeight = 18.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = if (isSent) Color.White else {
                                if (isDarkMode) PRIMARY else Color(
                                    0xff344054
                                )
                            },
                            text = timestamp,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )
                        if (showDoubleCheckMark && isSent) {
                            Spacer(Modifier.width(2.dp))

                            Image(
                                imageVector = vectorResource(Res.drawable.ic_double_checkmark),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                    }
                }
            }

            @Composable
            fun InboxContent() {
                Box(
                    Modifier.fillMaxWidth(0.78f),
                    contentAlignment = if (isSent) Alignment.TopEnd else Alignment.TopStart
                ) {
                    Column(
                        horizontalAlignment = if (isSent) Alignment.End else Alignment.Start
                    ) {

                        Box(modifier = Modifier.wrapContentSize()) {

                            val newBubbleHeight = bubbleHeight + if (message.isNotBlank()) 8.dp else 0.dp

                            val newBubbleWidth = bubbleWidth + 15.dp

                            if (showTip && separatorText.isBlank()) {
                                Canvas(
                                    Modifier
                                        .height(newBubbleHeight)
                                        .wrapContentWidth()
                                        .width(newBubbleWidth)
                                        .then(if (!isSent) Modifier.graphicsLayer {
                                            rotationY = 180f
                                        } else Modifier)
                                ) {
                                    val path = Path().apply {
                                        reset()

                                        moveTo(cornerShape, 0f)

                                        lineTo(size.width - cornerShape, 0f)

                                        arcTo(
                                            rect = Rect(
                                                offset = Offset(size.width - cornerShape, 0f),
                                                size = Size(cornerShape, cornerShape)
                                            ),
                                            startAngleDegrees = 270f,
                                            sweepAngleDegrees = 90f,
                                            forceMoveTo = false
                                        )

                                        lineTo(size.width, arrowHeight)

                                        lineTo(size.width, size.height - cornerShape)

                                        quadraticTo(
                                            size.width + arrowWidth / 4,  // Control X for a smoother curve
                                            size.height - arrowHeight + 2f,  // Control Y (height of the curve)
                                            size.width + arrowWidth,       // End X (arrow tip)
                                            size.height                    // End Y (base of arrow)
                                        )


                                        lineTo(-arrowWidth, size.height)


                                        arcTo(
                                            rect = Rect(
                                                offset = Offset(0f, size.height - cornerShape),
                                                size = Size(cornerShape, cornerShape)
                                            ),
                                            startAngleDegrees = 90f,
                                            sweepAngleDegrees = 90f,
                                            forceMoveTo = false
                                        )

                                        lineTo(0f, size.height)

                                        arcTo(
                                            rect = Rect(
                                                offset = Offset(0f, 0f),
                                                size = Size(cornerShape, cornerShape)
                                            ),
                                            startAngleDegrees = 180f,
                                            sweepAngleDegrees = 90f,
                                            forceMoveTo = false
                                        )
                                        close()
                                    }
                                    drawPath(
                                        path,
                                        color = if (isSent) Color(0xffF33358) else {
                                            if (isDarkMode) CARD_BG_DARK else Color(0xffF2F4F7)
                                        }
                                    )


                                }
                            } else {
                                Box(
                                    Modifier
                                        .height(newBubbleHeight)
                                        .wrapContentWidth()
                                        .width(newBubbleWidth)
                                        .background(
                                            if (isSent) Color(0xffF33358) else {
                                                if (isDarkMode) CARD_BG_DARK else Color(0xffF2F4F7)
                                            },
                                            RoundedCornerShape(16.dp)
                                        )
                                )
                            }

                            Column(
                                modifier = Modifier
                                  //  .padding(vertical = 4.dp)
                                    .wrapContentWidth()
                                    .onPlaced { size ->
                                        bubbleWidth = with(density) { size.size.width.toDp() }
                                        bubbleHeight = with(density) { size.size.height.toDp() }
                                    },
                            ) {

                                val data = medias.firstOrNull()
                                if (data != null) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.BottomEnd
                                    ) {
                                        val image = if (data.type == MediaItem.Type.IMAGE) data.image else data.thumbnail
                                        if (data.type == MediaItem.Type.VIDEO && data.videoUri != null) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1.035f)
                                                    .background(
                                                        Color(0xffEAECF0),
                                                        if (message.isNotBlank()) RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp) else RoundedCornerShape(8.dp)
                                                    ).clip(if (message.isNotBlank()) RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp) else RoundedCornerShape(8.dp))
                                                    .clipToBounds()
                                            ) {
                                                VideoPlayerComposable(
                                                    modifier = Modifier.fillMaxSize(),
                                                    playerHost = MediaPlayerHost(
                                                        mediaUrl = data.videoUri,
                                                        initialVideoFitMode = ScreenResize.FILL,
                                                    ),
                                                    playerConfig = VideoPlayerConfig(
                                                        showControls = false,
                                                        showVideoQualityOptions = false,
                                                        isDurationVisible = false,
                                                        isZoomEnabled = false,
                                                        loadingIndicatorColor = Color.Transparent,
                                                        isGestureVolumeControlEnabled = false
                                                    )
                                                )
                                            }
                                        }
                                        if (data.type == MediaItem.Type.IMAGE) {

                                            AsyncImage(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1.035f)
                                                    .background(
                                                        Color(0xffEAECF0),
                                                        if (message.isNotBlank()) RoundedCornerShape(
                                                            topEnd = 8.dp,
                                                            topStart = 8.dp
                                                        ) else RoundedCornerShape(8.dp)
                                                    ).clip(if (message.isNotBlank()) RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp) else RoundedCornerShape(8.dp))
                                                    .clipToBounds(),
                                                model = image,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop
                                            )


                                        }
                                        if (message.isBlank()) {
                                            SentInfo()
                                        }
                                    }
                                }

                                if (replyData != null) {
                                    ReplyBox(
                                        replyData,
                                        forTextField = false,
                                        isDarkMode = isDarkMode
                                    )
                                }

                                if (message.isNotBlank()) {
                                    UnderlinedLinkText(
                                        text = message,
                                        isSent = isSent,
                                        onLinkClicked = onLinkClicked,
                                        isDarkMode = isDarkMode
                                    )
                                }

//                        Text(
//                            modifier = Modifier.padding(end = 16.dp),
//                            fontWeight = FontWeight.Normal,
//                            lineHeight = 24.sp,
//                            fontFamily = PoppinsFontFamily(),
//                            color = if (isSent) Color.White else Color(0xff344054),
//                            text = message,
//                            fontSize = 14.sp,
//                        )

                                if (linkPreviewData != null && !(linkPreviewData.title == null && linkPreviewData.description == null && linkPreviewData.imageUrl == null)) {
                                    Box(
                                        Modifier.padding(start = 16.dp, end = 32.dp).wrapContentSize()
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 2.dp,
                                                    bottomStart = 2.dp,
                                                    topEnd = 8.dp,
                                                    bottomEnd = 8.dp
                                                )
                                            )
                                    ) {
                                        LinkPreviewCard(linkPreviewData, onClick = {
                                            onLinkClicked(linkPreviewData.url)
                                        })
                                    }
                                }

                                if (message.isNotBlank()) {
                                    Box(
                                        modifier = Modifier.wrapContentHeight().width(max(122.dp, bubbleWidth)),
                                        contentAlignment = Alignment.BottomEnd
                                    ) {
                                        SentInfo()
                                    }
                                }


                            }
                        }
                    }
                }
            }


            SwipeToDismissBox(
                // enableDismissFromStartToEnd = false,
                state = dismissBoxState,
                backgroundContent = {}
            ) {
                Box(Modifier.padding(horizontal = 16.dp)) {
                    if (isSelected) {
                        Row(
                            modifier = Modifier.padding(vertical = 0.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SelectionIndicator()
                            Spacer(modifier = Modifier.then(if (isSent) Modifier.weight(1f) else Modifier))
                            InboxContent()
                        }
                    } else {
                        InboxContent()
                    }
                }
            }

        }

    }


}

@Composable
fun UnderlinedLinkText(text: String,onLinkClicked:(String)->Unit,isSent: Boolean,isDarkMode: Boolean) {
    // Regex to find URLs in the text
    val urlRegex = """(?i)\b(?:https?://|www\.|)([a-z0-9-]+\.[a-z]{2,})(?:/\S*)?\b""".toRegex()

    // Build the annotated string with clickable links
    val annotatedString = buildAnnotatedString {
        var lastIndex = 0

        // Find URLs in the input text
        val matches = urlRegex.findAll(text)

//        println("the matched results ${matches.toList()}")

        matches.forEach { matchResult ->

            // Add normal text before the URL
            append(text.substring(lastIndex, matchResult.range.first))

            // Add the URL with clickable link and underline styling
            withLink(
                link = LinkAnnotation.Clickable(
                    tag = "URL",
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = if (isSent) Color.White else{ if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)},
                       //     color = Color.Blue,
                            textDecoration = TextDecoration.Underline,
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 14.sp
                        )
                    )
                ) {
                    // Handle URL click
                    println("Clicked URL: ${matchResult.value}")
                    onLinkClicked(matchResult.value)
                    // Here you can navigate or open the URL
                }
            ) {
                append(matchResult.value) // Append the clickable URL
            }

            lastIndex = matchResult.range.last + 1
        }

        // Add any remaining text after the last URL
        append(text.substring(lastIndex))
    }

    // Display the text using BasicText





    Text(
        modifier = Modifier.padding(end = 32.dp, start = 16.dp, top = 4.dp),
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        fontFamily = PoppinsFontFamily(),
        color = if (isSent) Color.White else{ if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)},
        text = annotatedString,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    )
}




@Composable
fun SelectionIndicator() {
    Box(
        Modifier
            .size(20.dp)
            .background(Color(0xffF33358), shape = CircleShape)
            .border(1.5.dp, color = Color.White, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            imageVector = vectorResource(Res.drawable.ic_tick_mark),
            modifier = Modifier.width(11.dp).height(11.dp),
            contentDescription = null
        )
    }
}


@Composable
fun SentReceivedDate(date:String,isDarkMode:Boolean) {

    Box(
        Modifier.fillMaxWidth().height(30.dp).padding(vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier.fillMaxHeight()
                .background(if(isDarkMode) CARD_BG_DARK else Color(0xffF2F4F7), RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Normal,
                //  lineHeight = 18.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                text = date,
                fontSize = 12.sp,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )
        }
    }

}
