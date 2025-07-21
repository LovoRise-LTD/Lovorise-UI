package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_swipe_fab_1
import coinui.composeapp.generated.resources.ic_swipe_fab_2
import coinui.composeapp.generated.resources.ic_swipe_fab_3
import coinui.composeapp.generated.resources.ic_swipe_fab_4
import coinui.composeapp.generated.resources.ic_swipe_fab_5
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SwipeActionButtons(onTemporarySkip:()->Unit,onLikeProfile:()->Unit,onDislikeProfile:()->Unit) {

    Box {
        Box(modifier = Modifier
            .noRippleClickable {  }
            .fillMaxWidth()
            .height(80.29.dp))
        Box(Modifier.padding(bottom = 25.dp).padding(horizontal = 25.dp)){
            Row(
                modifier = Modifier
                    .noRippleClickable {  }
                    .fillMaxWidth()
                    .height(55.29.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //            Spacer(Modifier.width(25.dp))
                Box(Modifier.size(35.dp).noRippleClickable(onDislikeProfile).shadow(elevation = 2.dp, CircleShape).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_swipe_fab_1),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                }

                Box(Modifier.size(55.29.dp).noRippleClickable(onLikeProfile).shadow(elevation = 2.dp, CircleShape).background(Color(0xffF33358), CircleShape), contentAlignment = Alignment.Center) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_swipe_fab_2),
                        contentDescription = null,
                        modifier = Modifier.width(23.28.dp).height(21.82.dp)
                    )
                }

                Box(
                    Modifier.size(55.29.dp).noRippleClickable {  }
                        .shadow(elevation = 2.dp, CircleShape), contentAlignment = Alignment.Center) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_swipe_fab_3),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Box(
                    Modifier.size(55.29.dp).noRippleClickable {  }.shadow(elevation = 2.dp, CircleShape).background(brush = Brush.linearGradient(listOf(
                        Color(0xff121212),
                        Color(0xff757575)
                    )), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_swipe_fab_4),
                        contentDescription = null,
                        modifier = Modifier.width(17.46.dp).height(28.57.dp)
                    )
                }

                Box(Modifier.size(35.dp).noRippleClickable(onTemporarySkip).shadow(elevation = 2.dp, CircleShape).background(Color(0xffEAAA08), CircleShape), contentAlignment = Alignment.Center) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_swipe_fab_5),
                        contentDescription = null,
                        // modifier = Modifier.size(1.dp)
                    )
                }
//            Spacer(Modifier.width(25.dp))


            }
        }
//        Spacer(Modifier.height(25.dp))
    }

}