package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_swipe_fab_5
import coinui.composeapp.generated.resources.ic_swipe_left
import coinui.composeapp.generated.resources.ic_swipe_right
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SwipeLeftRightIndication(isLeftSwipe:Boolean) {

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 80.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isLeftSwipe){
                Spacer(Modifier.weight(1f))
                Box(Modifier.size(80.dp).background(Color(0xffEAECF0), shape = CircleShape), contentAlignment = Alignment.Center){
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_swipe_left),
                        contentDescription = null,
                        modifier = Modifier.size(21.33.dp)
                    )
                }
            }


            if (!isLeftSwipe){
                Box(Modifier.size(80.dp).background(Color(0xffF33358), shape = CircleShape), contentAlignment = Alignment.Center){
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_swipe_right),
                        contentDescription = null,
                        modifier = Modifier.size(height = 18.75.dp, width = 26.25.dp)
                    )
                }
            }


        }

    }




}


@Composable
fun SkipTemporaryIndication() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(1f))
            Box(Modifier.size(80.dp).shadow(elevation = 2.dp, CircleShape).background(Color(0xffEAAA08), CircleShape), contentAlignment = Alignment.Center) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_swipe_fab_5),
                    contentDescription = null,
                    modifier = Modifier.size(height = 28.42.dp, width = 25.14.dp)
                )
            }
            Spacer(Modifier.weight(3f))
        }
    }
}