package com.kyrylo.gifs.presentation.grid

sealed interface GridAction {

    object SendError : GridAction

    object CloseApp : GridAction

}