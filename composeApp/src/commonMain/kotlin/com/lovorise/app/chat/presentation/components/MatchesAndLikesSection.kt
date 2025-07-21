package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.matches
import coinui.composeapp.generated.resources.see_all
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource


@Composable
fun MatchesAndLikesSection(
    blurLike:Boolean,
    likes:List<UserResponse>,
    onSeeAllClick:()->Unit,
    onLikesClick:()->Unit,
    onLikeItemClick:(UserResponse)->Unit,
    isDarkMode:Boolean,
    matchesCount:Int
) {

    LaunchedEffect(likes){
        println("the no of users are : ${likes.map{it.name}}")
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.height(41.dp).fillMaxWidth().padding(start = 16.dp, end = 27.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TextWithUnreadCount(stringResource(Res.string.matches),matchesCount, isDarkMode = isDarkMode)

            Box(Modifier.fillMaxHeight().noRippleClickable(onSeeAllClick), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(Res.string.see_all),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color(0xff344054)
                )
            }
        }

        Spacer(Modifier.height(5.dp))

        LazyRow(Modifier.fillMaxWidth().padding(start = 16.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)){

            item {
                LikesComponent(
                    imageUrl = "",
                    isBlurred = blurLike,
                    likes = 0,
                    onClick = onLikesClick,
                    isDarkMode = isDarkMode
                )
            }

            items(likes) { item ->
                ImageAndTextWithActiveStatus(
                    imageUrl = item.medias?.firstOrNull { it.type == SignedUrlMediaItem.Type.IMAGE }?.url ?: "",
                    isOnline = true,
                    isVerified = item.isVerified ?: false,
                    text = item.name ?: "No name",
                    onClick = {onLikeItemClick(item)},
                    isDarkMode = isDarkMode
                )
            }

            item {
                Spacer(Modifier.width(16.dp))
            }

        }

    }

}