package com.lovorise.app.profile_visitors.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.active
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.domain.model.User
import org.jetbrains.compose.resources.stringResource

@Composable
fun MatchAndLikeCard(
    modifier: Modifier = Modifier,
    isBlur: Boolean = false,
    user: User
) {
    Box(
        modifier = modifier
            .height(196.dp)
    ) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
        ) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(ambientColor = Color.Green, elevation = 0.dp)
                        .blur(radius = if (isBlur) 16.dp else 0.dp),
                    model = user.imageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )

                // Gradient Bottom Color
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f)
                                ),
                                startY = 100f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = if (isBlur) 14.dp else 8.dp)
        ) {

            if (user.isActive) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFF2AF287)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        text = stringResource(Res.string.active),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 6.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            if (isBlur.not()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "${user.name} ${if (user.age != 0) user.age.toString() else ""}",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}