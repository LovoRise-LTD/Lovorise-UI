package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lovorise.app.MediaItem
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.profile_upload.GridItems
import com.lovorise.app.accounts.presentation.signup.profile_upload.LoadingGridItem
import com.lovorise.app.ui.BASE_DARK
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import io.github.ahmad_hamwi.compose.pagination.PaginationState

@Composable
fun MediaPickerBottomSheetContent(isDarkMode:Boolean,paginationState: PaginationState<Int,MediaItem>,selectedItems:List<MediaItem>,onItemClicked:(Int)->Unit,textComposable : @Composable ()->Unit){

//    BoxWithConstraints(Modifier.fillMaxHeight(if (!isFullyExpanded) 0.55f else 0.8f).background(if (isDarkMode) BASE_DARK else Color.White)) {
    BoxWithConstraints(Modifier.fillMaxHeight(0.8f).background(if (isDarkMode) BASE_DARK else Color.White)) {
        val eachHeight = (maxWidth.value - 8).times(0.29).dp

        Column {
            Column(Modifier.fillMaxWidth().weight(1f)) {
                Box(Modifier.height(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .height(2.dp)
                            .width(40.dp)
                            .background(Color(0xff667085))
                    )
                }
                Spacer(Modifier.height(8.dp))
                PaginatedLazyVerticalGrid(
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    paginationState = paginationState,
                    newPageProgressIndicator = {
                        LoadingGridItem(height = eachHeight)
                    },
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (paginationState.allItems.isNullOrEmpty()){
                        item {
                            CircularLoader(true)
                        }
                    }else{
                        itemsIndexed(paginationState.allItems!!) { index, item ->
                            //  key(state.selectedItems){
                            GridItems(
                                selectedItems = selectedItems,
                                data = item,
                                onClick = {
                                    onItemClicked(index)
                                },
                                height = eachHeight,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            textComposable()

            Spacer(Modifier.height(14.dp))

            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .background(if (isDarkMode) BASE_DARK else Color.White)
            )
        }
    }

}