package com.lovorise.app.profile.presentation.edit_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.accept
import coinui.composeapp.generated.resources.decline
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_location_small
import coinui.composeapp.generated.resources.no_unlock_requests_at_the_moment
import coinui.composeapp.generated.resources.unlock_requests
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun UnlockMediaRequestBottomSheetContent(isDarkMode:Boolean,onCancel:()->Unit,data: List<UnlockUserRequestData>) {


    @Composable
    fun UnlockUserRequestItem(data: UnlockUserRequestData) {

        Column(Modifier.padding(16.dp)) {

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AsyncImage(
                    model = data.imageUrl,
                    contentScale = ContentScale.Crop,
                    clipToBounds = true,
                    modifier = Modifier.size(48.dp).clip(CircleShape),
                    contentDescription = null
                )

                Column {
                    Text(
                        text = data.name,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp,
                        fontSize = 16.sp,
                        color = if (isDarkMode) Color.White else Color.Black
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Icon(
                            tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                            imageVector = vectorResource(Res.drawable.ic_location_small),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )

                        Text(
                            text = data.location,
                            fontFamily = PoppinsFontFamily(),
                            letterSpacing = 0.2.sp,
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            CustomDivider(isDarkMode = isDarkMode)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ButtonWithText(
                    text = stringResource(Res.string.accept),
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    textColor = Color.White,
                    bgColor = Color(0xffF33358)
                )
                ButtonWithText(
                    text = stringResource(Res.string.decline),
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    textColor = Color(0xff344054),
                    bgColor = Color(0xffEAECF0)
                )
            }
        }


    }


    BoxWithConstraints {
        Column(
            modifier = Modifier.fillMaxHeight(if (data.isEmpty()) 0.6f else 0.9f)
        ) {

            Column(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Box(Modifier.height(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .height(2.dp)
                            .width(40.dp)
                            .background(Color(0xff667085))
                    )
                }

                Spacer(Modifier.height(8.dp))


                Row(
                    modifier = Modifier
                        .height(24.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(24.dp).noRippleClickable(onCancel), contentAlignment = Alignment.CenterStart){
                        Icon(
                            tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                            imageVector = vectorResource(Res.drawable.ic_left),
                            contentDescription = null,
                            modifier = Modifier.height(12.dp).width(16.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (data.isEmpty()) stringResource(Res.string.unlock_requests) else "Total ${data.size} requests",
                        fontFamily = PoppinsFontFamily(),
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff101828),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.2.sp,
                        fontSize = 16.sp
                    )
                }

                if (data.isEmpty()){
                    Spacer(Modifier.height(40.dp))

                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.weight(1f))

                        Text(
                            text = stringResource(Res.string.no_unlock_requests_at_the_moment),
                            fontFamily = PoppinsFontFamily(),
                            letterSpacing = 0.2.sp,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.weight(3f))

                    }
                }else{
                    Spacer(Modifier.height(24.dp))

                    LazyColumn {
                        items(data){
                            UnlockUserRequestItem(it)
                        }
                        item {
                            Spacer(Modifier.height(16.dp))
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }





            }

            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .background(if (isDarkMode) Color.Black else Color.White)
            )


        }
    }
}

data class UnlockUserRequestData(
    val imageUrl:String,
    val name:String,
    val location:String
)