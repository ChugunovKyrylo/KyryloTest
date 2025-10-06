package com.kyrylo.gifs.ui.grid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrylo.gifs.domain.repository.GifsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GridVIewModel @Inject constructor(
    private val repository: GifsRepository
): ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getGifs("smile")
        }
    }

}