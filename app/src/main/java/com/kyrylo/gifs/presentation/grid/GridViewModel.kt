package com.kyrylo.gifs.presentation.grid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrylo.gifs.domain.repository.GifsRepository
import com.kyrylo.gifs.presentation.mapper.GifResponseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
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
                    _state.update {
                        it.copy(currentPage = 0, gifs = emptyList())
                    }
                    loadQuery(q)
                }
        }
    }

    fun onRetryPaging() {
        val state = _state.value
        if (state.error.not() || state.isProcessPaging) return
        val query = state.query
        viewModelScope.launch {
            loadQuery(query)
        }
    }

    fun requestPaging() {
        if (processPaging) return
        processPaging = true
        if (_state.value.error) return
        viewModelScope.launch {
            Log.d("MainActivity", "request Paging")
            _state.update {
                val newCurrentPage = it.currentPage + 1
                it.copy(currentPage = newCurrentPage)
            }
            val query = _state.value.query
            loadQuery(query)
        }.invokeOnCompletion { processPaging = false }
    }

    fun onChangeQuery(q: String) {
        _state.update {
            it.copy(query = q)
        }
    }

    private suspend fun loadQuery(query: String) {
        try {
            Log.d("MainActivity", "load query entered")
            _state.update {
                it.copy(isProcessPaging = true, error = false)
            }
            if (query.isEmpty()) {
                _state.update {
                    it.copy(gifs = emptyList(), isProcessPaging = false, error = false)
                }
            } else {
                val pageSize = _state.value.pageSize
                val page = _state.value.currentPage
                val offset = page * pageSize
                val response =
                    repository.getGifs(query = query, pageSize = pageSize, offset = offset)
                val gridGifItemModels = gridGifResponsesMapper.map(response.data)
                _state.update {
                    val newGifs = it.gifs.toMutableList()
                    newGifs.addAll(gridGifItemModels)
                    it.copy(gifs = newGifs, isProcessPaging = false, error = false)
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            Log.d("MainActivity", "load query exit")
            _state.update {
                it.copy(
                    error = true,
                    isProcessPaging = false
                )
            }

        }
    }

}