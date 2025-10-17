package com.kyrylo.gifs.presentation.mapper

import com.kyrylo.gifs.data.FakeRepository
import com.kyrylo.gifs.domain.repository.GifsRepository
import com.kyrylo.gifs.presentation.models.GifModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GifResponseMapperTest {

    private lateinit var gifsRepository: GifsRepository
    private lateinit var mapper: GifResponseMapper

    @Before
    fun setUp() {
        gifsRepository = FakeRepository()
        mapper = GifResponseMapper()
    }

    @Test
    fun `check order is unique after mapping`() = runBlocking {
        val response = gifsRepository.getGifs(query = "smile", 25, offset = 0)
        val receivedModels = mapper.map(response.data)
        val distinctModels = receivedModels.distinctBy { it.order }
        val receivedModelsSize = receivedModels.size
        val distinctModelsSize = distinctModels.size
        assertEquals(expected = distinctModelsSize, actual = receivedModelsSize, message = "The models have the same order")
    }

    @Test
    fun `check is null response equals to empty list`() {
        val mappedList = mapper.map(null)
        val emptyList = emptyList<GifModel>()
        assertEquals(emptyList, mappedList, message = "Null response is not equal to empty list")
    }

}