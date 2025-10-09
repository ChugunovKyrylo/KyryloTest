package com.kyrylo.gifs.ui.grid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrylo.gifs.domain.repository.GifsRepository
import com.kyrylo.gifs.ui.grid.data.GridStateMapper
import com.kyrylo.gifs.ui.grid.models.GridState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GridViewModel @Inject constructor(
    private val repository: GifsRepository,
    private val gridStateMapper: GridStateMapper
) : ViewModel() {

    private val _state = MutableStateFlow(GridState())
    val state = _state.map { gridStateMapper.map(it) }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.map { it.query }
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { q ->
                    _state.value = _state.value.copy(currentPage = 0)
                    loadQuery(q)
                }
        }
    }

    private var processPaging = false

    fun requestPaging() {
        if (processPaging) return
        processPaging = true
        viewModelScope.launch(Dispatchers.IO) {
            val query = _state.value.query
            val newCurrentPage = _state.value.currentPage + 1
            _state.value = _state.value.copy(currentPage = newCurrentPage)
            loadQuery(query)
        }.invokeOnCompletion { processPaging = false }
    }

    fun onChangeQuery(q: String) {
        _state.value = _state.value.copy(query = q)
    }

    private suspend fun loadQuery(query: String) {
        try {
            val state = _state.value
            val pagingState = state.copy(paging = true)
            _state.value = pagingState
            if (query.isEmpty()) {
                _state.value = pagingState.copy(gifs = emptyList(), paging = false)
            } else {
                val pageSize = state.pageSize
                val page = state.currentPage
                val offset = page * pageSize
                val response =
                    repository.getGifs(query = query, pageSize = pageSize, offset = offset)
                val newGifs = pagingState.gifs.toMutableList()
                newGifs.addAll(response.data)
                _state.value = pagingState.copy(gifs = newGifs, paging = false)
            }
        } catch (_: CancellationException) {

        } catch (e: Exception) {
            _state.value = _state.value.copy(
                error = true,
                errorMessage = e.message,
                paging = false
            )
        }
    }

}