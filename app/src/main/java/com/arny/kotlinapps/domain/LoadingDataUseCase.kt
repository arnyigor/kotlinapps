package com.arny.kotlinapps.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.util.concurrent.ThreadLocalRandom

class LoadingDataUseCase {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    suspend operator fun invoke(): String = with(dispatcher) {
        val id = ThreadLocalRandom.current().nextInt(100, 1000)
        return when (ThreadLocalRandom.current().nextInt(0, 4)) {
            0 -> {
                delay(3000)
                "Success result complete id:$id"
            }
            1 -> {
                delay(1000)
                throw RuntimeException("Fail result")
            }
            2 -> {
                delay(2000)
                "Info result id:$id"
            }
            else -> {
                delay(2000)
                error("Test data has exception")
            }
        }
    }
}