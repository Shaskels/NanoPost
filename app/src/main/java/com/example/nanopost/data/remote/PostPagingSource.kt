package com.example.nanopost.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nanopost.data.remote.model.PostModel

class PostPagingSource(private val apiService: ApiService, private val profileId: String) :
    PagingSource<String, PostModel>() {

    companion object {
        private val START_PAGE_NUMBER = null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PostModel> {
        return try {
            val posts = apiService.getProfilePosts(
                profileId,
                params.loadSize,
                params.key ?: START_PAGE_NUMBER
            )
            LoadResult.Page(
                data = posts.items,
                prevKey = null,
                nextKey = if (posts.count != params.loadSize) null else posts.offset
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, PostModel>): String? {
        return null
    }

}