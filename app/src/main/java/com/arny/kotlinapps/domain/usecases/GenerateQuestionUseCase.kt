package com.arny.kotlinapps.domain.usecases

import com.arny.kotlinapps.domain.entity.Question
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min

class GenerateQuestionUseCase {
    private companion object {
        const val MIN_SUM_VALUE = 2
        const val MIN_VISIBLE_VALUE = 1
        const val COUNT_OF_OPTIONS = 6
    }

    operator fun invoke(maxSumValue: Int): Question {
        val random = ThreadLocalRandom.current()
        val sum = random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = random.nextInt(MIN_VISIBLE_VALUE, sum)
        val rightNumber = sum - visibleNumber

        val setOfOptions = HashSet<Int>()
        setOfOptions.add(rightNumber)

        val from = max(rightNumber - COUNT_OF_OPTIONS, MIN_VISIBLE_VALUE)
        val to = min(maxSumValue, rightNumber + COUNT_OF_OPTIONS)

        while (setOfOptions.size < COUNT_OF_OPTIONS) {
            setOfOptions.add(random.nextInt(from, to))
        }

        return Question(
            sum = sum,
            visibleNumber = visibleNumber,
            options = setOfOptions.toList().shuffled()
        )
    }
}