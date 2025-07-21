package com.lovorise.app.profile.domain.models

data class TextButtonInfo(
    val text:String,
    val isEnabled:Boolean = true,
//    val bgColor:Color,
//    val textColor:Color,
    val onClick:() -> Unit
)
