package com.example.nanopost.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nanopost.data.remote.network.model.PagedResponse
import com.example.nanopost.domain.exceptions.AuthenticationException

class BasePagingSource<T : Any>(private val loadData: suspend (Int, String?) -> PagedResponse<T>) :
    PagingSource<String, T>() {

    companion object {
        private val START_PAGE_NUMBER = null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, T> {
        return try {
            val res = loadData(
                params.loadSize,
                params.key ?: START_PAGE_NUMBER
            )
            LoadResult.Page(
                data = res.items,
                prevKey = null,
                nextKey = if (res.count != params.loadSize) null else res.offset
            )
        } catch (e: AuthenticationException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, T>): String? {
        return null
    }
}