package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.SearchTextField
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ShareReelBottomSheetContent(
    isDarkMode:Boolean,
    onShareButtonClick:()->Unit,
    hideBottomSheet:()->Unit,
    onToastMessage:(String)->Unit
) {

    var query by remember { mutableStateOf("") }

    val linkCopiedMsg = stringResource(Res.string.link_copied)


    val allItems by remember {
        mutableStateOf(
            listOf(
                ShareProfileItem("https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg?auto=compress&cs=tinysrgb&w=800","lorib",false),
                ShareProfileItem("https://images.pexels.com/photos/1681010/pexels-photo-1681010.jpeg?auto=compress&cs=tinysrgb&w=800","noah",false),
                ShareProfileItem("https://images.pexels.com/photos/1587009/pexels-photo-1587009.jpeg?auto=compress&cs=tinysrgb&w=800","andil",false),
                ShareProfileItem("https://images.pexels.com/photos/774095/pexels-photo-774095.jpeg?auto=compress&cs=tinysrgb&w=800","lyleka",false),
                ShareProfileItem("https://images.pexels.com/photos/846741/pexels-photo-846741.jpeg?auto=compress&cs=tinysrgb&w=800","adam",false),
            )
        )
    }

    var searchResults by remember { mutableStateOf(emptyList<ShareProfileItem>()) }

    var selectedItems by remember { mutableStateOf(emptyList<String>()) }


    var isButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(selectedItems){
        isButtonEnabled = selectedItems.isNotEmpty()
    }


    LaunchedEffect(query){
        if (query.isNotBlank()){
           // delay(100L)
            val filteredItems = allItems.filter { item ->
                item.name.lowercase().contains(query.lowercase())
            }
            searchResults = filteredItems
        }else{
            searchResults = emptyList()
        }
    }


    BoxWithConstraints {
        Column(
            modifier = Modifier.then(if (maxHeight< 560.dp) Modifier.fillMaxHeight(0.9f) else Modifier)
        ) {

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
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
                    text = stringResource(Res.string.share_reel),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 20.sp,
                    color = Color(0xff101828)
                )

                Spacer(Modifier.height(16.dp))

                SearchTextField(
                    label = stringResource(Res.string.search),
                    onQueryChange = { query = it },
                    query = query,
                    roundedCornerPercent = 23
                )


                Spacer(Modifier.height(8.dp))

                if ((searchResults.isEmpty() && query.isNotBlank())) {
                    Box(Modifier.fillMaxWidth().height(240.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(Res.string.no_result_found_for_name),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 21.sp,
                            color = Color(0xff475467),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().height(240.dp),
                        //  verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(searchResults.ifEmpty { allItems }) { item ->
                            ShareProfileItemCard(
                                onCheckBoxClick = {
                                    selectedItems = if (selectedItems.contains(item.imageUrl)) {
                                        selectedItems.toMutableList().apply {
                                            remove(item.imageUrl)
                                        }
                                    } else {
                                        selectedItems.toMutableList().apply {
                                            add(item.imageUrl)
                                        }
                                    }
                                    //  allItems = allItems.toMutableList().apply { add(index,removeAt(index).copy(isChecked = !item.isChecked)) }
                                },
                                item = item.copy(
                                    isChecked = selectedItems.contains(item.imageUrl)
                                )
                            )

                        }
                    }
                }



                Spacer(modifier = Modifier.height(8.dp))

                ButtonWithText(
                    text = stringResource(Res.string.share),
                    bgColor = Color(if (isButtonEnabled) 0xffF33358 else 0xffEAECF0),
                    textColor = Color(if (isButtonEnabled) 0xffffffff else 0xff98A2B3),
                    onClick = {
                        if (isButtonEnabled) {
                            onShareButtonClick()
                        }
                    },
                    shape = RoundedCornerShape(23)
                )
                Spacer(modifier = Modifier.height(8.dp))
                CopyLinkButton(onCopy = {
                    hideBottomSheet()
                    onToastMessage(linkCopiedMsg)
                })

                Spacer(Modifier.height(16.dp))


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



@Composable
fun CopyLinkButton(onCopy:()->Unit) {

    Row(
        modifier = Modifier.height(32.dp).noRippleClickable(onCopy),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(Modifier.size(32.dp).background(Color(0xffF2F4F7), CircleShape), contentAlignment = Alignment.Center){
            Image(
                imageVector = vectorResource(Res.drawable.ic_copy_link),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(Modifier.width(8.dp))

        Text(
            text = stringResource(Res.string.copy_link),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 21.sp,
            color = Color(0xff101828)
        )

    }

}

@Composable
fun ShareProfileItemCard(onCheckBoxClick:()->Unit,item: ShareProfileItem) {
    Row(
        modifier = Modifier.height(48.dp).noRippleClickable(onCheckBoxClick).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = item.imageUrl,
            modifier = Modifier.size(32.dp).clip(CircleShape),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = item.name,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 24.sp,
            color = Color(0xff101828)
        )

        Spacer(Modifier.weight(1f))

        Checkbox(
            checked = item.isChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xffF33358),
                checkmarkColor = Color.White,
                uncheckedColor = Color(0xffD0D5DD)
            ),
            onCheckedChange = null,
            modifier = Modifier.size(16.dp)
        )



    }
}




data class ShareProfileItem(
    val imageUrl:String,
    val name:String,
    val isChecked:Boolean
)