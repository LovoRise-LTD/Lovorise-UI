package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.link_og_tag.LinkPreviewData
import com.lovorise.app.noRippleClickable

@Composable
fun LinkPreviewCard(previewData: LinkPreviewData,onClick:()->Unit) {

    var errorLoadingImage by remember { mutableStateOf(false) }

    Row(Modifier.fillMaxWidth().background(Color(0xfff68ea2)).height(IntrinsicSize.Min).noRippleClickable(onClick)) {


        Box(Modifier.fillMaxHeight().width(2.dp).align(Alignment.CenterVertically).background(Color.White, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)))


        Column(Modifier.padding(vertical = 8.dp, horizontal = 8.dp) ) {

          //  Spacer(Modifier.height(4.dp))

            if (!previewData.siteName.isNullOrBlank()) {
                Text(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 21.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color.White,
                    text = previewData.siteName,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!previewData.title.isNullOrBlank()) {
                Text(
                    fontWeight = FontWeight.Normal,
                    lineHeight = 21.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color.White,
                    text = previewData.title,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(2.dp))

            if (!previewData.description.isNullOrBlank()) {
                Text(
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color.White,
                    text = previewData.description,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!previewData.imageUrl.isNullOrBlank() && !errorLoadingImage) {
                AsyncImage(
                    modifier = Modifier.height(147.dp).fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                    model = previewData.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onError = {
                        errorLoadingImage = true
                    }
                )
            }

         //   Spacer(Modifier.height(8.dp))

        }

    }

}