package aoc2020.day3

import aoc2020.util.loadTextResource

data class Slope(val x: Int, val y: Int)

fun main(args: Array<String>) {
    val lines = loadTextResource("/day3.txt").lines()

    print("Part 1: ")
    println(countTrees(Slope(3, 1), lines))

    print("Part 2: ")
    println(
        listOf(Slope(1, 1), Slope(3, 1), Slope(5, 1), Slope(7, 1), Slope(1, 2))
            .fold(1) { acc, slope -> acc * countTrees(slope, lines) }
    )
}

fun countTrees(slope: Slope, map: List<String>) =
    map
        .slice(0 until map.lastIndex step slope.y)
        .fold(Pair(0, 0)) { (x, count), row ->
            Pair(
                (x + slope.x) % row.length,
                count + if (row[x] == '#') 1 else 0
            )
        }.second
