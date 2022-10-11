package com.hyperion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.hyperion.domain.model.DomainVideoPartial
import com.hyperion.domain.repository.InnerTubeRepository

class HomeViewModel(
    private val repository: InnerTubeRepository
) : ViewModel() {
    val videos = Pager(PagingConfig(10)) {
        object : PagingSource<String, DomainVideoPartial>() {
            override suspend fun load(params: LoadParams<String>) = try {
                val response = repository.getRecommendations(params.key)

                LoadResult.Page(
                    data = response.videos,
                    prevKey = null,
                    nextKey = response.continuation
                )
            } catch (e: Exception) {
                e.printStackTrace()

                LoadResult.Error(e)
            }

            override fun getRefreshKey(state: PagingState<String, DomainVideoPartial>): String? = null
        }
    }.flow.cachedIn(viewModelScope)
}