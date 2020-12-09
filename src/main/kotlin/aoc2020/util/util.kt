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

fun <T1, T2> Sequence<T1>.product(other: Sequence<T2>): Sequence<Pair<T1, T2>> =
    this.flatMap { v -> other.map { Pair(v, it) } }

fun <T1, T2> Iterable<T1>.product(other: Iterable<T2>): Iterable<Pair<T1, T2>> =
    this.flatMap { v -> other.map { Pair(v, it) } }

fun <T> Iterable<T>.pairs(): Iterable<Pair<T, T>> =
    this.flatMapIndexed { idx, v -> this.drop(idx + 1).map { Pair(v, it) } }
