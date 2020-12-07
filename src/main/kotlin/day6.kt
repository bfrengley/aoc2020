package aoc2020.day6

import aoc2020.util.asBlocks
import aoc2020.util.loadTextResource

fun main(args: Array<String>) {
    val lines = loadTextResource("/day6.txt").lines()

    // part 1
    print("Part 1: ")
    println(
        lines.asBlocks()
            .map { block -> block.joinToString().filter { it.isLetter() }.toSet().size }.sum()
    )

    // part 2
    print("Part 2: ")
    println(
        lines.asBlocks()
            .map { block ->
                block.map { it.toSet() }
                    .reduce { sharedAnswers, answers -> sharedAnswers.intersect(answers) }
                    .size
            }.sum()
    )
}
