package com.lovorise.app.accounts.presentation.signup.profile_upload
/*
import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil3.PlatformContext
import com.lovorise.app.MediaItem
import com.lovorise.app.getImagesAndVideos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class ImageVideoPagingSource(private val context: PlatformContext) : PagingSource<Int,MediaItem>() {


    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
        println("the load func called $params")
        return withContext(Dispatchers.IO){
            try {
                val page = params.key ?: 1
                val size = params.loadSize
                val items = getImagesAndVideos(context, pageNumber = page, pageSize = size)
                println("the items is ${items.size}")
                LoadResult.Page(
                    data = items,
                    prevKey = null,
                    nextKey = page + 1
                )

            }catch (e:Exception){
                e.printStackTrace()
                LoadResult.Error(e)
            }

        }
    }
}


 */