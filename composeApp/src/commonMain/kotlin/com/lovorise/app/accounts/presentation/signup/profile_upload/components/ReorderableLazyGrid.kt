package com.lovorise.app.accounts.presentation.signup.profile_upload.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import kotlinx.datetime.Clock
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReorderableLazyGrid(
    images: List<Photo>,
    countGridCell: Int = 3,
    onClickAdd:(Int)->Unit,
    onEdit:(Int)->Unit,
    onRemove:(Int)->Unit,
    onMove:(Int,Int)->Unit,
    aspectRatio:Float
) {
    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        if (images.getOrNull(to.index)?.image != null || from.index > to.index) {
            onMove(from.index, to.index)
        }
    }

    var hasMoreThanOneImages by remember { mutableStateOf(false) }

    LaunchedEffect(images){
        hasMoreThanOneImages = images.filter { it.image != null }.size >= 2
    }

    BoxWithConstraints {

        // created formula to calculate height required when we take 4:5 aspect ratio
        val height = ((5 * maxWidth + 52.dp) / 6)

        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(countGridCell),
            modifier = Modifier.sizeIn(maxHeight = height),
            contentPadding = PaddingValues(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            itemsIndexed(images, key = { i,item->val x = item.placeHolder.ifBlank { item.image.toString() + i };println("the key is $x");x }) { index, item ->

                ReorderableItem(reorderableLazyGridState, key = item.placeHolder.ifBlank { item.image.toString() + index }) { isDragging ->
                    println("the item is $item")
                    ImagePickerView(
                        modifier = Modifier.fillMaxSize().aspectRatio(aspectRatio)
                            .then(if (item.image != null && hasMoreThanOneImages) Modifier.draggableHandle() else Modifier),
                        onClickAdd = {
                            onClickAdd(index)
                        },
                        onEdit = {
                            if (item.image != null) onEdit(index)
                        },
                        onRemove = {
                            if (item.image != null) onRemove(index)
                        },
                        photo = item.copy(canEdit = index == 0),
                        isBeingDragged = isDragging,
                        canRemove = index != 0 && item.image != null
                    )
                }
            }
        }
    }
}

data class Photo(
//    val orderNum:Int,
    val imgId:String?=null,
    val thumbnailUrl:String?=null,
    val id: Int,
    val orderNum:Int,
    val placeHolder:String = "",
    val image: Any? = null,
    var canEdit: Boolean = false,
    var isActive: Boolean = false,
    val timestamp:Long = Clock.System.now().toEpochMilliseconds(),
    val isCaptured:Boolean = false,
    val type : SignedUrlMediaItem.Type = SignedUrlMediaItem.Type.IMAGE
)