package lesson7.task1

import org.junit.jupiter.api.Test
import withFilesRunner

private fun generateDeleteMarkedData(): Pair<String, String> {
    val linesCount = (0..20).random()
    val expectedOutput = StringBuilder()
    val input = StringBuilder()

    repeat(linesCount) {
        if (Math.random() <= 0.5) {
            input.append("lorem ipsum\n")
            expectedOutput.append("lorem ipsum\n")
        } else {
            input.append("_lorem ipsum\n")
        }
    }

    return input.toString() to expectedOutput.toString()
}

class CustomTests {
    @Test
    fun customDeleteMarked() = withFilesRunner(::deleteMarked) {
        // Ensure my util functions work properly by
        // checking the existing test still works the same
        assertTrimmedText(
            """
            _ Не надо начинать с этой строки.
            Задачи _надо_ решать правильно,
            _хотя очень хочется решить их быстро и неправильно,
            
            и не надо при этом никуда торопиться___
            _   А я всё равно решил задачу неправильно.
    
            """,
            """
            Задачи _надо_ решать правильно,
            
            и не надо при этом никуда торопиться___
            """
        )

        // Edge case of an empty file
        assertTrimmedText("", "")
        // Ensure lines don't glue to one another
        assertTrimmedText("a\nb", "a\nb")
        // Ensure empty lines are preserved
        assertTrimmedText("a\n\nb", "a\n\nb")
        // Ensure the line is deleted along with it's \n, not just the text
        assertTrimmedText("a\n\n_b\n\nc", "a\n\n\nc")
        // Ensure "nothing breaks" if all the lines must be deleted
        assertTrimmedText("_a\n_b\n_c", "")

        // Ensure the target function doesn't simply "learn" all
        // our input data with randomly generated input data
        repeat(20) {
            val (input, expectedOutput) = generateDeleteMarkedData()
            assertTrimmedText(input, expectedOutput)
        }
    }
}
