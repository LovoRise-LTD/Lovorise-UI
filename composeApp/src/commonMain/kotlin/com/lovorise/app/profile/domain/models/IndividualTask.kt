package com.lovorise.app.profile.domain.models

import androidx.compose.ui.graphics.Color

data class IndividualTask(
    val task:String,
    val coins:Int,
    val buttonInfo: TextButtonInfo,
    val taskProgress: TaskProgress?
){
    companion object{
        val EnabledBtnColor = Color(0XFFF33358)
        val EnabledBtnTxtColor = Color.White
        val DisabledBtnColor = Color(0XFFEAECF0)
        val DisabledDarkBtnColor = Color(0XFF24282B)
        val DisabledTxtBtnColor = Color(0XFF98A2B3)
        val DisabledDarkTxtBtnColor = Color(0XFF98A2B3)
    }
}
