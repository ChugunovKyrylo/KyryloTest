package com.kyrylo.gifs.ui.grid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrylo.gifs.domain.repository.GifsRepository
import com.kyrylo.gifs.ui.mapper.GifResponseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GridViewModel @Inject constructor(
    private val repository: GifsRepository,
    private val gridGifResponsesMapper: GifResponseMapper
) : ViewModel() {
    private var processPaging = false

    private val _state = MutableStateFlow(GridState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.map { it.query }
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { q ->
                    _state.value = _state.value.copy(currentPage = 0, gifs = emptyList())
                    loadQuery(q)
                }
        }
    }

    fun requestPaging() {
        if (processPaging) return
        processPaging = true
        viewModelScope.launch {
            val state = _state.value
            val query = state.query
            val newCurrentPage = state.currentPage + 1
            _state.value = state.copy(currentPage = newCurrentPage)
            loadQuery(query)
        }.invokeOnCompletion { processPaging = false }
    }

    fun onChangeQuery(q: String) {
        _state.value = _state.value.copy(query = q)
    }

    private suspend fun loadQuery(query: String) {
        try {
            _state.value = _state.value.copy(isProcessPaging = true)
            if (query.isEmpty()) {
                _state.value = _state.value.copy(gifs = emptyList(), isProcessPaging = false)
            } else {
                val pageSize = _state.value.pageSize
                val page = _state.value.currentPage
                val offset = page * pageSize
                val response =
                    repository.getGifs(query = query, pageSize = pageSize, offset = offset)
                val gridGifItemModels = gridGifResponsesMapper.map(response.data)
                val newGifs = _state.value.gifs.toMutableList()
                newGifs.addAll(gridGifItemModels)
                _state.value = _state.value.copy(gifs = newGifs, isProcessPaging = false)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                error = true,
                errorMessage = e.message,
                isProcessPaging = false
            )
        }
    }

}