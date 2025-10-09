package com.kyrylo.gifs.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrylo.gifs.domain.repository.GifsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val gifsRepository: GifsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()
    private val id = savedStateHandle.get<String>("id") ?: ""

    init {
        viewModelScope.launch {
            gifsRepository.getGifById(id)
        }
    }

}