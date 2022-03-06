package lesson6.task1

import Runner
import lesson4.task1.roman
import org.junit.jupiter.api.Test
import perform

val ALPHABET = setOf('A'..'Z')

val ROMAN = setOf('I', 'V', 'X', 'L', 'C', 'D', 'M')
val REPEATABLE = setOf('I', 'X', 'C', 'M')
val NON_REPEATABLE = ROMAN - REPEATABLE

private fun <T> withRandomDistance(a: T, b: T): String {
    val distance = (0..10).random()
    return "$a" + "I".repeat(distance) + "$b"
}

class CustomTests {
    @Test
    fun customFromRoman() = Runner(::fromRoman).perform {
        // Ensure my util functions work properly by
        // checking the existing test still works the same
        assertResult("I", 1)
        assertResult("MMM", 3000)
        assertResult("MCMLXXVIII", 1978)
        assertResult("DCXCIV", 694)
        assertResult("XLIX", 49)
        assertResult("Z", -1)

        // Empty string edge case
        assertResult("", -1)

        // [FAULT FOUND]
        // Ensure there are no things like IIII
        REPEATABLE.forEach {
            assertResult("$it".repeat(4), -1)
        }
        NON_REPEATABLE.forEach {
            // Random distance increases our confidence
            assertResult(withRandomDistance(it, it), -1)
        }

        // Ensure invalid letters lead to an error
        (ALPHABET - ROMAN).forEach {
            assertResult("$it", -1)
        }

        // Ensure all the possible numbers may successfully
        // be converted back and forth
        (1 until 4000).forEach {
            assertResult(roman(it), it)
        }

        // [FAULT FOUND]
        // "Bigger" letters should come first.
        // NB: The default set implementation preserves
        // the ordering
        NON_REPEATABLE.reduce { old, next ->
            assertResult("$old$next", -1)
            next
        }

        // [FAULT FOUND]
        // Forbid things like "IVI"
        NON_REPEATABLE.forEach { a ->
            REPEATABLE.forEach { b ->
                assertResult("$b$a$b", -1)
            }
        }
    }
}