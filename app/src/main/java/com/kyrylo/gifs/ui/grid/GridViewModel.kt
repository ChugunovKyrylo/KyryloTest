package com.kyrylo.gifs.ui.grid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrylo.gifs.DataResult
import com.kyrylo.gifs.domain.repository.GifsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val repository: GifsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GridState())
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        _state.value = _state.value.copy(
            gifs = DataResult.Error(data = null, error = throwable),
            paging = false
        )
    }

    init {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
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
            Log.d("GridViewModel", "startPaging")
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
        Log.d("GridViewModel", " search = $query")
        val oldGifs = _state.value.gifs
        _state.value = _state.value.copy(
            gifs = DataResult.Received(data = oldGifs.getValueOrNull() ?: emptyList()),
            paging = true
        )
        delay(3000)
        if (query.isEmpty()) {
            _state.value = _state.value.copy(gifs = DataResult.Empty(), paging = false)
        } else {
            val pageSize = _state.value.pageSize
            val page = _state.value.currentPage
            val offset = page * pageSize
            val response = repository.getGifs(query = query, pageSize = pageSize, offset = offset)
            val prevGifs = _state.value.gifs.getValueOrNull() ?: emptyList()
            val newGifs = prevGifs + response.data
            _state.value = _state.value.copy(gifs = DataResult.Received(newGifs), paging = false)
        }
    }

}