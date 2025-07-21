package com.lovorise.app.accounts.presentation.signup.email.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import kotlinx.coroutines.delay

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpLength: Int = 6,
    shouldShowCursor: Boolean = true,
    shouldCursorBlink: Boolean = true,
    onOtpModified: (String, Boolean) -> Unit,
    isDarkMode: Boolean = false,
) {

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (otpText.length > otpLength) {
            throw IllegalArgumentException("OTP should be $otpLength digits")
        }
    }


    BoxWithConstraints {
        val screenWidth = maxWidth
        BasicTextField(
            singleLine = true,
            modifier = modifier
                //  .height(48.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
            onValueChange = {
                if (it.text.length <= otpLength) {
                    onOtpModified.invoke(it.text, it.text.length == otpLength)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            decorationBox = { inner ->
                CompositionLocalProvider(
                    LocalTextSelectionColors.provides(
                        TextSelectionColors(
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                ) {
                    Box(modifier = Modifier.drawWithContent { }) {
                        inner()
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(otpLength) { index ->
                        CharacterContainer(
                            index = index,
                            text = otpText,
                            shouldShowCursor = shouldShowCursor,
                            shouldCursorBlink = shouldCursorBlink,
                            enabledBorderColor = Color(0xffF33358),
                            disabledBorderColor = Color(0xffD0D5DD),
                            containerSize = if (screenWidth < 345.dp) 45.dp else 48.dp,
                            isTextFieldFocused = isFocused,
                            isDarkMode = isDarkMode
                        )
                    }
                }

            }
        )
    }




}

object EmptyTextToolbar: TextToolbar {
    override val status: TextToolbarStatus = TextToolbarStatus.Hidden

    override fun hide() {  }

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?,
    ) {
    }
}

@Composable
fun CharacterContainer(
    index: Int,
    text: String,
    shouldShowCursor: Boolean,
    shouldCursorBlink: Boolean,
    enabledBorderColor: Color,
    disabledBorderColor: Color,
    containerSize: Dp,
    isTextFieldFocused:Boolean,
    isDarkMode:Boolean
) {
    val isFocused = text.length == index && isTextFieldFocused
    val character = when {
        index < text.length -> text[index].toString()
        else -> ""
    }

    // Cursor visibility state
    val cursorVisible = remember { mutableStateOf(shouldShowCursor) }

    // Blinking effect for the cursor
    LaunchedEffect(key1 = isFocused) {
        if (isFocused && shouldShowCursor && shouldCursorBlink) {
            while (true) {
                delay(800) // Adjust the blinking speed here
                cursorVisible.value = !cursorVisible.value
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(containerSize)
            .border(
                width = 1.dp,
                color = when {
                    isFocused -> enabledBorderColor
                    else -> disabledBorderColor
                },
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        Text(
            text = character,
            color = if (isDarkMode) Color.White else Color(0xff101828),
            textAlign = TextAlign.Center,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            fontSize = 18.sp
        )

        // Display cursor when focused
        AnimatedVisibility(visible = isFocused && cursorVisible.value) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(1.5.dp)
                    .height(18.dp) // Adjust height according to your design
                    .background(if (isDarkMode) Color.White else Color.Black)
            )
        }
    }
}