package com.lovorise.app.libs.emojipicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
actual fun EmojiPicker(onPicked:(String)->Unit,height:Dp){

//    AndroidView(
//        factory = { context ->
////            val themedContext = ContextThemeWrapper(context, R.style.EmojiPickerViewStyle)
////
////            EmojiPickerView(themedContext)
////                .apply {
////
////                    // setting row $columns - Optional
//////                    emojiGridColumns = 9
//////                    emojiGridRows = 6f
////                    // set pick listener
////                    setOnEmojiPickedListener { item ->
////                        onPicked(item.emoji)
////                    }
////
////                }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(height)
//          //  .verticalScroll(rememberScrollState())
//
//    )




}