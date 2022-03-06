package lesson5.task1

import Runner
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import perform

private fun callFindSumOfTwo(input: Pair<List<Int>, Int>) = findSumOfTwo(input.first, input.second)

private fun Runner<Pair<List<Int>, Int>, Pair<Int, Int>>.assertIndices(
    input: Pair<List<Int>, Int>,
    suchNumbersShouldExist: Boolean,
) {
    val (numbers, sum) = input
    val output = run(input)

    assertEquals(suchNumbersShouldExist, output != -1 to -1, "Input: $input")
    assertTrue(output.first < numbers.size, "Input: $input")
    assertTrue(output.second < numbers.size, "Input: $input")

    if (suchNumbersShouldExist) {
        assertNotEquals(output.first, output.second, "Input: $input")
        assertEquals(sum, numbers[output.first] + numbers[output.second], "Input: $input")
    }
}

class CustomTests {
    @Test
    fun findSumOfTwo() = Runner(::callFindSumOfTwo).perform {
        // Ensure my util functions work properly by
        // checking the existing test still works the same
        assertResult(
            emptyList<Int>() to 1,
            Pair(-1, -1)
        )
        assertResult(
            listOf(1, 2, 3) to 4,
            Pair(0, 2),
        )
        assertResult(
            listOf(1, 2, 3) to 6,
            Pair(-1, -1),
        )

        // According to the task, the list of
        // numbers must not contain n <= 0, but
        // the sum we are looking for _may_ be <= 0.
        // The algorithm should always return (-1, -1)
        // in such cases, but we should check that.
        assertResult(listOf(1, 2, 3) to -10, -1 to -1)

        // We should get _different_ numbers:
        assertResult(listOf(5, 5) to 10, 0 to 1)

        // Check if there is a number that equals the sum
        assertResult(listOf(10, 5, 3) to 10, -1 to -1)
        assertResult(listOf(10, 5, 5) to 10, 1 to 2)

        // Based on the task description both of these should be valid.
        // This is an example of how we may want to write some tests
        // based on contradicting or unclear requirements
//        assertResult(listOf(3, 5, 5, 7) to 10, 0 to 3)
//        assertResult(listOf(3, 5, 5, 7) to 10, 1 to 2)

        // We do, however, want to check if the algorithm works
        // properly in cases where multiple answers are acceptable
        // (what if it just breaks in such cases?)
        assertIndices(listOf(3, 5, 5, 7) to 10, true)

        // Ensure huge numbers work (the program doesn't hand due to
        // time/memory issues)
        assertResult(listOf(Int.MAX_VALUE, 0) to Int.MAX_VALUE, 0 to 1)

        // [FAULT FOUND]
        // Check overflow.
        // NB: Technically, MIN_VALUE is still the sum of MAX_VALUE and 1,
        // but only from the perspective of the implementation detail, not maths.
        assertResult(listOf(Int.MAX_VALUE, Int.MAX_VALUE, 1) to Int.MIN_VALUE, -1 to -1)
    }
}
