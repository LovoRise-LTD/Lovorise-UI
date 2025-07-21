package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add_a_comment
import coinui.composeapp.generated.resources.express_your_feelings_by_sending_gift
import coinui.composeapp.generated.resources.ic_gift_birthday_cake
import coinui.composeapp.generated.resources.ic_gift_bouquet
import coinui.composeapp.generated.resources.ic_gift_candle
import coinui.composeapp.generated.resources.ic_gift_champagne
import coinui.composeapp.generated.resources.ic_gift_cheers_champagne_glasses
import coinui.composeapp.generated.resources.ic_gift_cherries
import coinui.composeapp.generated.resources.ic_gift_chocolate_bar
import coinui.composeapp.generated.resources.ic_gift_cocktail
import coinui.composeapp.generated.resources.ic_gift_crown
import coinui.composeapp.generated.resources.ic_gift_cupcake
import coinui.composeapp.generated.resources.ic_gift_dog
import coinui.composeapp.generated.resources.ic_gift_fire
import coinui.composeapp.generated.resources.ic_gift_ghost
import coinui.composeapp.generated.resources.ic_gift_heart_on_fire
import coinui.composeapp.generated.resources.ic_gift_heart_simple
import coinui.composeapp.generated.resources.ic_gift_heart_with_ribbon
import coinui.composeapp.generated.resources.ic_gift_party_popper
import coinui.composeapp.generated.resources.ic_gift_peach
import coinui.composeapp.generated.resources.ic_gift_ring
import coinui.composeapp.generated.resources.ic_gift_rocket
import coinui.composeapp.generated.resources.ic_gift_rose
import coinui.composeapp.generated.resources.ic_gift_strawberry
import coinui.composeapp.generated.resources.ic_gift_teddy
import coinui.composeapp.generated.resources.ic_gift_wrapped_hamper
import coinui.composeapp.generated.resources.ic_heart_small
import coinui.composeapp.generated.resources.send
import coinui.composeapp.generated.resources.stand_out_with_gifts
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SendGiftsBottomSheetContent(
    isDarkMode:Boolean,
    onSend:(GiftItem) -> Unit,
    profileImg:String
) {

    var comment by rememberSaveable{ mutableStateOf("") }

    val gifts = listOf(
        GiftItem(imageRes = Res.drawable.ic_gift_rose, animationRes = "files/rose.json", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_heart_simple, animationRes = "", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_heart_with_ribbon, animationRes = "", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_wrapped_hamper, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_rocket, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_ring, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_champagne, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_birthday_cake, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_bouquet, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_fire, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_crown, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_dog, animationRes = "", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_teddy, animationRes = "files/teddy.json", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_cocktail, animationRes = "", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_cheers_champagne_glasses, animationRes = "files/champagne_glasses.json", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_ghost, animationRes = "", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_cupcake, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_heart_on_fire, animationRes = "", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_cherries, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_strawberry, animationRes = "", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_chocolate_bar, animationRes = "", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_peach, animationRes = "", hearts = 50),
        GiftItem(imageRes = Res.drawable.ic_gift_candle, animationRes = "", hearts = 100),
        GiftItem(imageRes = Res.drawable.ic_gift_party_popper, animationRes = "files/cone.json", hearts = 100),

    )

    var selectedGiftIndex by rememberSaveable{ mutableStateOf(0) }


    BoxWithConstraints(
        modifier = Modifier
            .background(if (isDarkMode) BASE_DARK else Color.White)
            .fillMaxWidth()
            .fillMaxHeight(0.86f)
    ) {
        val width = maxWidth

        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
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

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.stand_out_with_gifts),
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) Color.White else Color(0xff121213),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = stringResource(Res.string.express_your_feelings_by_sending_gift),
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(24.dp))

                LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy((width-32.dp-(104.dp*3))/2), verticalArrangement = Arrangement.spacedBy(10.dp)){

                    itemsIndexed(gifts) { index, gift ->
                        GiftGridItem(gift, onClick = {selectedGiftIndex = index}, isSelected = index == selectedGiftIndex)
                    }

                }


                Spacer(Modifier.height(24.dp))

                AddCommentSection(
                    profileImg = profileImg,
                    onCommentChange = {
                        if(it.length <= 120){
                            comment = it
                        }
                    },
                    comment = comment,
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(24.dp))

                ButtonWithText(
                    text = stringResource(Res.string.send),
                    onClick = {
                        val gift = gifts.getOrNull(selectedGiftIndex)
                        if (comment.isNotBlank() && gift != null){
                            onSend(gift.copy(comment = comment))
                        }
                    },
                    bgColor = if (comment.isNotBlank()) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (comment.isNotBlank()) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                )


                Spacer(Modifier.height(16.dp))




            }

            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .background(if (isDarkMode) BASE_DARK else Color.White)
            )
        }


    }

}


@Composable
fun GiftGridItem(item: GiftItem,onClick:()->Unit,isSelected:Boolean) {

    Column(
        modifier = Modifier
            .size(104.dp)
            .border(width = 1.dp, color = Color(if (isSelected) 0xffF33358 else 0xffEAECF0), shape = RoundedCornerShape(8.dp))
            .noRippleClickable(onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            imageVector = vectorResource(item.imageRes),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .height(23.dp)
                .background(Color(0xffF9E9EC), RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(Modifier.width(4.dp))
            Image(
                imageVector = vectorResource(Res.drawable.ic_heart_small),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Spacer(Modifier.width(2.dp))

            Text(
                text = item.hearts.toString(),
                fontFamily = PoppinsFontFamily(),
                color = Color(0xff101828),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.2.sp
            )
            Spacer(Modifier.width(4.dp))

        }
    }

}

@Composable
fun AddCommentSection(profileImg: String,comment: String,onCommentChange:(String)->Unit,isDarkMode: Boolean) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            modifier = Modifier.size(40.dp).background(Color(0xffF9E9EC), CircleShape).clip(CircleShape),
            model = profileImg,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            clipToBounds = true
        )

        Spacer(Modifier.width(15.dp))

        Box(Modifier.fillMaxWidth().weight(1f).background(if (isDarkMode) CARD_BG_DARK else Color(0xffF2F4F7).copy(alpha = 0.6f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.CenterStart){
            BasicTextField(
                cursorBrush = SolidColor(if (isDarkMode) DISABLED_LIGHT else Color(0xff111111)),
                value = comment,
                onValueChange = onCommentChange,
                maxLines = 2,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
               // singleLine = true,
                decorationBox = { padding ->
                    if (comment.isBlank()) {
                        Text(
                            text = stringResource(Res.string.add_a_comment),
                            color = if (isDarkMode) DISABLED_TEXT_DARK else Color(0xff475467),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                    padding()
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.2.sp,
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                )
            )
        }


    }
}







data class GiftItem(
    val imageRes:DrawableResource,
    val hearts:Int,
    val animationRes:String = "",
    val isRedeemed:Boolean = false,
    val comment:String = "",
    val senderName:String = ""
)
