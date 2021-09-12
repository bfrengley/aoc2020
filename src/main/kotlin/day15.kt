package aoc2020.day15

import aoc2020.util.loadTextResource


fun main(args: Array<String>) {
    val nums = loadTextResource("/day15.txt").split(',').map { it.toInt() }
    println("Part 1: ${memoryGame(nums, 2020)}")
    println("Part 2: ${memoryGame(nums, 30000000)}")
}

fun memoryGame(startNums: List<Int>, turns: Int): Int {
    val memory = startNums.take(startNums.size - 1).withIndex()
        .associate { (i, c) -> c to i + 1 }.toMutableMap()
    var n = startNums.last()

    for (turn in startNums.size until turns) {
        val lastN = n
        n = when (val lastTurn = memory[n]) {
            null -> 0
            else -> turn - lastTurn
        }
        memory[lastN] = turn
    }

    return n
}
