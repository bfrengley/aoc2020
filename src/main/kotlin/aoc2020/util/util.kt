package aoc2020.util

fun loadTextResource(path: String) = object {}.javaClass.getResource(path).readText().trim()

fun List<String>.asBlocks(): List<List<String>> {
    var rest = this
    return sequence {
        while (rest.isNotEmpty()) {
            yield(rest.takeWhile { it.isNotEmpty() }.toList())
            rest = rest.dropWhile { it.isNotEmpty() }.drop(1)
        }
    }.toList()
}
