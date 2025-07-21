package com.lovorise.app.accounts.presentation.signup.age.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextToolbar
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
import com.lovorise.app.accounts.presentation.signup.email.components.EmptyTextToolbar
import kotlinx.coroutines.delay


@Composable
fun DobInputField(
    modifier: Modifier = Modifier,
    dobText: String,
    dobLength: Int = 8,
    shouldShowCursor: Boolean = true,
    shouldCursorBlink: Boolean = true,
    onDobModified: (String, Boolean) -> Unit,
    isDarkMode: Boolean
) {

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    BoxWithConstraints {

        val screenWidth = maxWidth

        CompositionLocalProvider(
            LocalTextToolbar provides EmptyTextToolbar
        ) {
            BasicTextField(
                modifier = modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                value = TextFieldValue(dobText, selection = TextRange(dobText.length)),
                onValueChange = {
                    if (it.text.length <= dobLength) {
                        onDobModified.invoke(it.text, true)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                decorationBox = {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        repeat(dobLength) { index ->
                            DobCharacterContainer(
                                index = index,
                                text = dobText,
                                shouldShowCursor = shouldShowCursor,
                                shouldCursorBlink = shouldCursorBlink,
                                enabledBorderColor = Color(0xffF33358),
                                disabledBorderColor = Color(0xffEAECF0),
                                containerHeight = if (screenWidth < 345.dp) 33.dp else 36.dp,
                                containerWidth = if (screenWidth < 345.dp) 35.23.dp else 38.23.dp,
                                label = when (index) {
                                    in 0..1 -> 'D'
                                    in 2..3 -> 'M'
                                    else -> 'Y'
                                },
                                isTextFieldFocused = isFocused,
                                isDarkMode = isDarkMode
                            )
                            Spacer(modifier = Modifier.weight(if (index == 1 || index == 3) 7f else 1f))
                        }
                    }

                }
            )
        }
    }


}


@Composable
fun DobCharacterContainer(
    index: Int,
    text: String,
    shouldShowCursor: Boolean,
    shouldCursorBlink: Boolean,
    enabledBorderColor: Color,
    disabledBorderColor: Color,
    containerWidth: Dp,
    containerHeight: Dp,
    label: Char,
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
            .height(containerHeight)
            .width(containerWidth)
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
            text = character.ifBlank { if (!isFocused) label.toString() else "" },
            color = if (character.isBlank()) Color(0xffEAECF0) else Color(if (isDarkMode) 0xffEAECF0 else 0xff344054),
            textAlign = TextAlign.Center,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            lineHeight = 21.sp,
            fontSize = 14.sp
        )


        // Display cursor when focused
        AnimatedVisibility(visible = isFocused && cursorVisible.value) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(1.5.dp)
                    .height(14.dp) // Adjust height according to your design
                    .background(Color.Black)
            )
        }
    }
}

