package aoc2020.day10

import aoc2020.util.*
import kotlin.math.pow

fun main(args: Array<String>) {
    val vals = listOf(0) + loadTextResource("/day10.txt").lines().map { it.toInt() }.sorted()
        .let { it + listOf(it.last() + 3) }

    print("Part 1: ")
    println(vals.windowed(2).fold(Pair(0, 0)) { (diff1, diff3), (a, b) ->
        when (b - a) {
            1 -> Pair(diff1 + 1, diff3)
            3 -> Pair(diff1, diff3 + 1)
            else -> Pair(diff1, diff3)
        }
    }.let { it.first * it.second })

    println("Part 2: ${countArrangements(vals)}")
}

// I'm pretty sure this can be done in a single fold but I'm too tired to figure it out
fun countArrangements(jolts: List<Int>): Long {
    val followers = mutableMapOf(jolts.last() to 1L)

    for (i in jolts.indices.reversed().drop(1)) {
        val jolt = jolts[i]
        followers[jolt] = jolts.subList(i + 1, jolts.size)
            .takeWhile { it - jolt <= 3 }
            .map { followers[it]!! }
            .sum()
    }

    return followers[jolts.first()]!!
}
