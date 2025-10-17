package com.kyrylo.gifs.presentation.grid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrylo.gifs.domain.repository.GifsRepository
import com.kyrylo.gifs.domain.usecases.GetGifsUseCase
import com.kyrylo.gifs.presentation.mapper.GifResponseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GridViewModel @Inject constructor(
    private val getGifs: GetGifsUseCase,
    private val gridGifResponsesMapper: GifResponseMapper
) : ViewModel() {

    private var retryPagingJob: Job? = null
    private var processPaging = false

    private val _state = MutableStateFlow(GridState())
    val state = _state.asStateFlow()

    val errorFlow = MutableSharedFlow<Unit>()

    init {
        viewModelScope.launch {
            _state.map { it.query }
                .debounce(1000)
                .distinctUntilChanged()
                .collectLatest { q ->
                    withContext(Dispatchers.IO) {
                        retryPagingJob?.cancelAndJoin()
                    }
                    _state.update {
                        it.copy(currentPage = 0, gifs = emptyList(), overflow = false)
                    }
                    loadQuery(q)
                }
        }
    }

    fun onRetryPaging() {
        val state = _state.value
        if (state.error.not() || state.isProcessPaging || state.overflow) return
        if (processPaging) return
        processPaging = true
        val query = state.query
        newRetryPagingJob {
            loadQuery(query)
        }
    }

    fun requestPaging() {
        Log.d(
            "MainActivity",
            "request Paging (not started) processPaging $processPaging. error ${_state.value.error}. overflow ${_state.value.overflow}"
        )
        if (_state.value.error) return
        if (_state.value.overflow) return
        if (processPaging) return
        processPaging = true
        newRetryPagingJob {
            Log.d("MainActivity", "request Paging")
            _state.update {
                val newCurrentPage = it.currentPage + 1
                it.copy(currentPage = newCurrentPage)
            }
            val query = _state.value.query
            loadQuery(query)
        }
    }

    private fun newRetryPagingJob(onNewJob: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            retryPagingJob?.cancelAndJoin()
        }.invokeOnCompletion {
            retryPagingJob = viewModelScope.launch {
                onNewJob()
            }
        }
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
                    getGifs(query = query, pageSize = pageSize, offset = offset)
                val gridGifItemModels = gridGifResponsesMapper.map(response.data)
                val overflow =
                    (response.pagination?.totalCount?.let { totalCount -> offset + pageSize >= totalCount }
                        ?: response.data?.isEmpty()) == true
                _state.update {
                    val newGifs = it.gifs.toMutableList()
                    newGifs.addAll(gridGifItemModels)
                    Log.d("MainActivity", "request http made. List size ${newGifs.size}. overflow $overflow")
                    it.copy(gifs = newGifs, isProcessPaging = false, error = false, overflow = overflow)
                }
            }
        } catch (e: CancellationException) {
            Log.d("MainActivity", "load query cancelled")
            throw e
        } catch (_: Exception) {
            Log.d("MainActivity", "load query exit")
            errorFlow.emit(Unit)
            _state.update {
                it.copy(
                    error = true,
                    isProcessPaging = false
                )
            }
        } finally {
            processPaging = false
        }
    }

}