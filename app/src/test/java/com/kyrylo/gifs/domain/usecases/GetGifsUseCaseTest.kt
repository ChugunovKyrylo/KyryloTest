package com.kyrylo.gifs.domain.usecases

import com.kyrylo.gifs.data.FakeRepository
import com.kyrylo.gifs.domain.repository.GifsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetGifsUseCaseTest {

    private lateinit var getGifsUseCase: GetGifsUseCase
    private lateinit var gifsRepository: GifsRepository

    @Before
    fun setUp() {
        gifsRepository = FakeRepository()
        getGifsUseCase= GetGifsUseCase(gifsRepository)
    }

    @Test(expected = IllegalStateException::class)
    fun `if negative offset throws exception`(): Unit = runBlocking {
        getGifsUseCase.invoke(query = "afas", pageSize = 25, offset = -5)
    }

    @Test(expected = IllegalStateException::class)
    fun `if negative pageSize throws exception`(): Unit = runBlocking {
        getGifsUseCase.invoke(query = "afas", pageSize = -50, offset = 0)
    }

    @Test(expected = IllegalStateException::class)
    fun `if zero pageSize throws exception`(): Unit = runBlocking {
        getGifsUseCase.invoke(query = "afas", pageSize = 0, offset = 0)
    }

    @Test(expected = IllegalStateException::class)
    fun `if empty query throws exception`(): Unit = runBlocking {
        getGifsUseCase.invoke(query = "", pageSize = 25, offset = 0)
    }

}