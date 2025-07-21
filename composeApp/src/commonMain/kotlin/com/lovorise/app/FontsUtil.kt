package com.lovorise.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import coinui.composeapp.generated.resources.Poppins_Bold
import coinui.composeapp.generated.resources.Poppins_Light
import coinui.composeapp.generated.resources.Poppins_Medium
import coinui.composeapp.generated.resources.Poppins_Regular
import coinui.composeapp.generated.resources.Poppins_SemiBold
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.SFPRODISPLAYREGULAR
import org.jetbrains.compose.resources.Font

@Composable
fun PoppinsFontFamily() = FontFamily(
    Font(Res.font.Poppins_Light, weight = FontWeight.Light),
    Font(Res.font.Poppins_Regular, weight = FontWeight.Normal),
    Font(Res.font.Poppins_Medium, weight = FontWeight.Medium),
    Font(Res.font.Poppins_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.Poppins_Bold, weight = FontWeight.Bold)
)

@Composable
fun SFProDisplayFontFamily() = FontFamily(
    Font(Res.font.SFPRODISPLAYREGULAR, weight = FontWeight.Normal)
)

