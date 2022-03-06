import java.io.File

fun File.ifExists() = when {
    this.exists() -> this
    else -> null
}

fun withFiles(input: String, run: (String, String) -> Unit): String {
    val inputName = "temp.in.txt"
    val outputName = "temp.txt"

    File(inputName).bufferedWriter().use {
        it.write(input)
    }

    return try {
        run(inputName, outputName)
        File(outputName).readLines().joinToString("\n")
    } finally {
        File(inputName).ifExists()?.delete()
        File(outputName).ifExists()?.delete()
    }
}

fun <T> T.perform(block: T.() -> Unit) {
    apply(block)
}
