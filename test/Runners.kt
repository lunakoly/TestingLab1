import org.junit.jupiter.api.Assertions.*

open class Runner<T, K>(val run: (T) -> K) {
    fun assertResult(input: T, expectedOutput: K) {
        assertEquals(expectedOutput, run(input), "Input: $input")
    }
}

class StringRunner(run: (String) -> String) : Runner<String, String>(run) {
    fun assertTrimmedText(input: String, expectedOutput: String) {
        assertResult(input.trimIndent(), expectedOutput.trimIndent())
    }
}

fun withFilesRunner(run: (String, String) -> Unit, use: StringRunner.() -> Unit) {
    StringRunner { withFiles(it, run) }.apply(use)
}
